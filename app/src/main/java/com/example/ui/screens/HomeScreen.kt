package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.StoryProgressEntity
import com.example.data.model.CharacterStory
import com.example.data.model.HistoricalData
import com.example.ui.components.ArabianDivider
import com.example.ui.components.ArabianOrnamentalCard
import com.example.ui.theme.ArabianMidnight
import com.example.ui.theme.ArabianNightBorder
import com.example.ui.theme.ArabianNightCard
import com.example.ui.theme.ArabianNightCardElevated
import com.example.ui.theme.DesertAmber
import com.example.ui.theme.HeritageGoldDark
import com.example.ui.theme.HeritageGoldLight
import com.example.ui.theme.HeritageGoldPrimary
import com.example.ui.theme.OasisEmerald
import com.example.ui.theme.ParchmentDark
import com.example.ui.theme.ParchmentMuted
import com.example.ui.theme.ParchmentWhite
import com.example.ui.viewmodel.HakawatiViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun HomeScreen(
    viewModel: HakawatiViewModel,
    modifier: Modifier = Modifier
) {
    val allProgress by viewModel.allProgress.collectAsStateWithLifecycle()
    val isAmbiancePlaying by viewModel.isAmbiancePlaying.collectAsStateWithLifecycle()
    val userRank = viewModel.getUserRank()

    val randomWisdom = remember {
        HistoricalData.dailyWisdoms.random()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Header & Storyteller Banner
        item {
            HakawatiHeroHeader(
                userRank = userRank,
                isAmbiancePlaying = isAmbiancePlaying,
                onToggleAmbiance = { viewModel.toggleAmbiance() }
            )
        }

        // Daily Arabian Wisdom
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ArabianOrnamentalCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("daily_wisdom_card"),
                    backgroundColor = Color(0xFF1E152F),
                    borderColor = HeritageGoldPrimary.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "✨", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "حكمة اليوم ومأثورات العرب",
                                color = HeritageGoldLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "✨", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = randomWisdom,
                            color = ParchmentWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }

        // Section Title: Legendary Characters
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "أساطير التاريخ والفروسية",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = HeritageGoldLight
                    )
                    Text(
                        text = "اقرأ السير الخالدة وافتح ألغاز وأحجيات أبطال العرب",
                        fontSize = 12.sp,
                        color = ParchmentMuted
                    )
                }
                Text(
                    text = "📜 ٤ سير",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DesertAmber
                )
            }
        }

        // Stories List
        items(HistoricalData.stories) { story ->
            val progress = allProgress.find { it.storyId == story.id }
            CharacterStoryCard(
                story = story,
                progress = progress,
                onReadStory = { viewModel.navigateTo(ScreenDestination.StoryDetail(story.id)) },
                onStartQuiz = { viewModel.startQuiz(story.id) },
                onOpenPuzzle = {
                    if (progress?.isPuzzleUnlocked == true) {
                        viewModel.initPuzzle(story.id)
                    } else {
                        viewModel.navigateTo(ScreenDestination.CharacterPuzzle(story.id))
                    }
                }
            )
        }

        // Quick Overview Card of Arabian Era
        item {
            ArabianDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp))
            EraOverviewBanner()
        }
    }
}

@Composable
fun HakawatiHeroHeader(
    userRank: String,
    isAmbiancePlaying: Boolean,
    onToggleAmbiance: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF281742),
                        Color(0xFF1B112B),
                        ArabianMidnight
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    colors = listOf(
                        HeritageGoldPrimary.copy(alpha = 0.8f),
                        HeritageGoldDark.copy(alpha = 0.3f),
                        HeritageGoldPrimary.copy(alpha = 0.8f)
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(HeritageGoldPrimary.copy(alpha = 0.2f))
                            .border(1.5.dp, HeritageGoldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏺", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "تطبيق حَكَوَاتِي",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = HeritageGoldLight
                        )
                        Text(
                            text = userRank,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DesertAmber
                        )
                    }
                }

                // Audio Ambiance Synthesizer Button
                IconButton(
                    onClick = onToggleAmbiance,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isAmbiancePlaying) HeritageGoldPrimary else ArabianNightCardElevated)
                        .border(1.dp, HeritageGoldPrimary, CircleShape)
                        .size(42.dp)
                        .testTag("ambiance_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isAmbiancePlaying) Icons.Default.MusicNote else Icons.Default.MusicOff,
                        contentDescription = "أنغام الحكواتي التراثية",
                        tint = if (isAmbiancePlaying) ArabianMidnight else HeritageGoldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "مرحباً بك في ديوان الحكايات؛ حيث تُروى ملاحم الفرسان، وتُستحضر مكارم الأخلاق، وتُختبر الفطنة بالأحجيات وألغاز الشخصيات.",
                fontSize = 14.sp,
                color = ParchmentMuted,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun CharacterStoryCard(
    story: CharacterStory,
    progress: StoryProgressEntity?,
    onReadStory: () -> Unit,
    onStartQuiz: () -> Unit,
    onOpenPuzzle: () -> Unit
) {
    val isCompleted = progress?.isCompleted == true
    val isPuzzleUnlocked = progress?.isPuzzleUnlocked == true
    val isPuzzleSolved = progress?.isPuzzleSolved == true
    val chaptersCount = story.chapters.size
    val readCount = progress?.completedChaptersCount ?: 0
    val progressFraction = if (chaptersCount > 0) readCount.toFloat() / chaptersCount else 0f

    ArabianOrnamentalCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("story_card_${story.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Character Image with Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = story.imageRes),
                    contentDescription = story.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark & Gold Vignette Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x990E0A16),
                                    ArabianNightCard
                                )
                            )
                        )
                )

                // Tribe & Era Tag
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Surface(
                        color = ArabianMidnight.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HeritageGoldPrimary.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = story.tribe,
                            color = HeritageGoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                // Completion Badge
                if (isCompleted) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        Surface(
                            color = OasisEmerald.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "تمت القراءة",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Character Name & Title at the bottom of the image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = story.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = ParchmentWhite
                    )
                    Text(
                        text = story.title,
                        fontSize = 13.sp,
                        color = HeritageGoldLight,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Body Content
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = story.shortBio,
                    fontSize = 13.sp,
                    color = ParchmentMuted,
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Reading Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تقدم القراءة ($readCount/$chaptersCount فصول)",
                        fontSize = 11.sp,
                        color = ParchmentDark
                    )
                    Text(
                        text = "${(progressFraction * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeritageGoldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = HeritageGoldPrimary,
                    trackColor = ArabianNightBorder
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons Row: Read Story / Quiz / Character Puzzle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. Read Story Button
                    Button(
                        onClick = onReadStory,
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("read_story_btn_${story.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HeritageGoldPrimary,
                            contentColor = ArabianMidnight
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isCompleted) "إعادة القراءة" else "قراءة السيرة",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 2. Riddle / Quiz Button
                    OutlinedButton(
                        onClick = onStartQuiz,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quiz_btn_${story.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = HeritageGoldLight
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ArabianNightBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = DesertAmber
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "الأحجية",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // 3. Puzzle Button (Locked if story not finished!)
                    Button(
                        onClick = onOpenPuzzle,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("puzzle_btn_${story.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPuzzleUnlocked) Color(0xFF3B2856) else Color(0xFF1E182A),
                            contentColor = if (isPuzzleUnlocked) HeritageGoldLight else ParchmentDark
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isPuzzleUnlocked) HeritageGoldPrimary.copy(alpha = 0.5f) else Color(0xFF332747)
                        )
                    ) {
                        Icon(
                            imageVector = if (isPuzzleUnlocked) Icons.Default.Extension else Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = if (isPuzzleSolved) OasisEmerald else if (isPuzzleUnlocked) HeritageGoldPrimary else ParchmentDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPuzzleSolved) "حللت البازل" else if (isPuzzleUnlocked) "البازل 🧩" else "مغلق 🔒",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EraOverviewBanner() {
    ArabianOrnamentalCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        backgroundColor = Color(0xFF170F24)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🏛️", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "عن ديوان الحكايات والتراث",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeritageGoldLight
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "يجمع التطبيق بين روعة التراث العربي الأصيل وجمال التصميم الحديث. احرص على قراءة الفصول بعناية لتتمكن من حل الأحجيات وفك أقفال بازل الشخصيات التذكارية!",
                fontSize = 13.sp,
                color = ParchmentMuted,
                lineHeight = 20.sp
            )
        }
    }
}
