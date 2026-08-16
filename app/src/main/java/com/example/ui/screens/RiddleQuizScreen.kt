package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ArabianDivider
import com.example.ui.components.ArabianOrnamentalCard
import com.example.ui.theme.ArabianMidnight
import com.example.ui.theme.ArabianNightBorder
import com.example.ui.theme.ArabianNightCard
import com.example.ui.theme.ArabianNightCardElevated
import com.example.ui.theme.ArabianNightSurface
import com.example.ui.theme.DesertAmber
import com.example.ui.theme.DesertTerracotta
import com.example.ui.theme.HeritageGoldDark
import com.example.ui.theme.HeritageGoldLight
import com.example.ui.theme.HeritageGoldPrimary
import com.example.ui.theme.OasisEmerald
import com.example.ui.theme.ParchmentDark
import com.example.ui.theme.ParchmentMuted
import com.example.ui.theme.ParchmentWhite
import com.example.ui.viewmodel.HakawatiViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiddleQuizScreen(
    storyId: String,
    viewModel: HakawatiViewModel,
    modifier: Modifier = Modifier
) {
    val story by viewModel.currentQuizStory.collectAsStateWithLifecycle()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
    val selectedAnswerIndex by viewModel.selectedAnswerIndex.collectAsStateWithLifecycle()
    val isAnswerSubmitted by viewModel.isAnswerSubmitted.collectAsStateWithLifecycle()
    val quizScore by viewModel.quizScore.collectAsStateWithLifecycle()
    val quizFinished by viewModel.quizFinished.collectAsStateWithLifecycle()

    var showHint by remember { mutableStateOf(false) }

    val currentStory = story ?: return
    val totalQuestions = currentStory.quizzes.size
    val currentQuestion = currentStory.quizzes.getOrNull(currentQuestionIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArabianMidnight)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "أحجيات ${currentStory.name}",
                    color = HeritageGoldLight,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.StoryDetail(storyId)) },
                    modifier = Modifier.testTag("quiz_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = HeritageGoldPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ArabianNightSurface)
        )

        if (quizFinished) {
            // Quiz Result Screen
            QuizResultView(
                score = quizScore,
                total = totalQuestions,
                characterName = currentStory.name,
                onRestart = { viewModel.startQuiz(storyId) },
                onReadStory = { viewModel.navigateTo(ScreenDestination.StoryDetail(storyId)) },
                onOpenPuzzle = { viewModel.initPuzzle(storyId) }
            )
        } else if (currentQuestion != null) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .testTag("quiz_body")
            ) {
                // Question Counter & Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الأحجية رقم ${currentQuestionIndex + 1} من $totalQuestions",
                        color = HeritageGoldLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "النقاط: $quizScore ⭐",
                        color = DesertAmber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = HeritageGoldPrimary,
                    trackColor = ArabianNightBorder
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Question Card
                ArabianOrnamentalCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = ArabianNightCard
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚔️ سؤال من وقائع السيرة",
                                color = DesertAmber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Hint Button
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { showHint = !showHint }
                                    .testTag("hint_btn"),
                                color = Color(0xFF33254B),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = HeritageGoldPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "تلميح",
                                        color = HeritageGoldLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentQuestion.question,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ParchmentWhite,
                            lineHeight = 26.sp
                        )

                        AnimatedVisibility(visible = showHint) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                Surface(
                                    color = Color(0xFF281C3F),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, HeritageGoldPrimary.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "💡 ${currentQuestion.hint}",
                                        color = HeritageGoldLight,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options List
                currentQuestion.options.forEachIndexed { optIndex, optionText ->
                    val isSelected = selectedAnswerIndex == optIndex
                    val isCorrect = optIndex == currentQuestion.correctIndex

                    val optionBgColor = when {
                        isAnswerSubmitted && isCorrect -> OasisEmerald.copy(alpha = 0.25f)
                        isAnswerSubmitted && isSelected && !isCorrect -> DesertTerracotta.copy(alpha = 0.25f)
                        isSelected -> HeritageGoldPrimary.copy(alpha = 0.2f)
                        else -> ArabianNightCard
                    }

                    val optionBorderColor = when {
                        isAnswerSubmitted && isCorrect -> OasisEmerald
                        isAnswerSubmitted && isSelected && !isCorrect -> DesertTerracotta
                        isSelected -> HeritageGoldPrimary
                        else -> ArabianNightBorder
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(optionBgColor)
                            .border(1.dp, optionBorderColor, RoundedCornerShape(14.dp))
                            .clickable(enabled = !isAnswerSubmitted) {
                                viewModel.selectQuizAnswer(optIndex)
                            }
                            .padding(16.dp)
                            .testTag("quiz_option_$optIndex")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected || (isAnswerSubmitted && isCorrect)) optionBorderColor else ArabianNightSurface)
                                    .border(1.dp, optionBorderColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isAnswerSubmitted) {
                                    if (isCorrect) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    } else if (isSelected) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                } else {
                                    Text(
                                        text = "${optIndex + 1}",
                                        color = if (isSelected) ArabianMidnight else ParchmentMuted,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optionText,
                                color = if (isSelected || (isAnswerSubmitted && isCorrect)) ParchmentWhite else ParchmentMuted,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Explanation when submitted
                if (isAnswerSubmitted) {
                    Spacer(modifier = Modifier.height(14.dp))
                    ArabianOrnamentalCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0xFF1E172F),
                        borderColor = OasisEmerald.copy(alpha = 0.6f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "📜 إيضاح من سيرة التاريخ:",
                                color = HeritageGoldLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentQuestion.explanation,
                                color = ParchmentWhite,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom Action: Submit or Next Question
                if (!isAnswerSubmitted) {
                    Button(
                        onClick = { viewModel.submitQuizAnswer() },
                        enabled = selectedAnswerIndex != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_answer_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HeritageGoldPrimary,
                            contentColor = ArabianMidnight,
                            disabledContainerColor = ArabianNightCardElevated,
                            disabledContentColor = ParchmentDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "تأكيد الإجابة", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            showHint = false
                            viewModel.nextQuizQuestion()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("next_question_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HeritageGoldPrimary,
                            contentColor = ArabianMidnight
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (currentQuestionIndex + 1 < totalQuestions) "الأحجية التالية ➔" else "عرض النتيجة الختامية 🏆",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizResultView(
    score: Int,
    total: Int,
    characterName: String,
    onRestart: () -> Unit,
    onReadStory: () -> Unit,
    onOpenPuzzle: () -> Unit
) {
    val percentage = if (total > 0) (score.toFloat() / total * 100).toInt() else 0

    val evaluation = when {
        percentage >= 90 -> "شيخ الفطنة والرواة 👑"
        percentage >= 60 -> "فارس الحكمة والأثر ⚔️"
        else -> "باحث عن علوم الأجداد 📜"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag("quiz_result_view"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(HeritageGoldPrimary.copy(alpha = 0.2f))
                .border(2.dp, HeritageGoldPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = if (percentage >= 60) "🏆" else "📜", fontSize = 42.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "اكتملت أحجيات $characterName",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = HeritageGoldLight
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = evaluation,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DesertAmber
        )

        Spacer(modifier = Modifier.height(16.dp))

        ArabianOrnamentalCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = ArabianNightCard
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "الإجابات الصحيحة", fontSize = 12.sp, color = ParchmentMuted)
                    Text(text = "$score / $total", fontSize = 22.sp, fontWeight = FontWeight.Black, color = OasisEmerald)
                }

                Box(modifier = Modifier.size(1.dp, 40.dp).background(ArabianNightBorder))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "نسبة الإتقان", fontSize = 12.sp, color = ParchmentMuted)
                    Text(text = "$percentage%", fontSize = 22.sp, fontWeight = FontWeight.Black, color = HeritageGoldPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onOpenPuzzle,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("result_open_puzzle_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = HeritageGoldPrimary,
                contentColor = ArabianMidnight
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Extension, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "الانتقال إلى بازل الشخصية 🧩", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onRestart,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ArabianNightCardElevated),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "إعادة الأحجية", fontSize = 13.sp)
            }

            Button(
                onClick = onReadStory,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ArabianNightCardElevated),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "مراجعة السيرة", fontSize = 13.sp)
            }
        }
    }
}
