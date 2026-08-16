package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CharacterStory
import com.example.data.model.HistoricalData
import com.example.data.model.StoryChapter
import com.example.ui.components.ArabianDivider
import com.example.ui.components.ArabianOrnamentalCard
import com.example.ui.components.ArabianPoetryBanner
import com.example.ui.theme.ArabianMidnight
import com.example.ui.theme.ArabianNightBorder
import com.example.ui.theme.ArabianNightCard
import com.example.ui.theme.ArabianNightCardElevated
import com.example.ui.theme.ArabianNightSurface
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    storyId: String,
    viewModel: HakawatiViewModel,
    modifier: Modifier = Modifier
) {
    val story = HistoricalData.getStoryById(storyId) ?: return
    val allProgress by viewModel.allProgress.collectAsStateWithLifecycle()
    val progress = allProgress.find { it.storyId == storyId }

    val completedChapters = progress?.completedChaptersCount ?: 0
    val isFullyCompleted = progress?.isCompleted == true || completedChapters >= story.chapters.size
    var selectedChapterIndex by remember { mutableIntStateOf(0) }

    val currentChapter = story.chapters.getOrElse(selectedChapterIndex) { story.chapters.first() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArabianMidnight)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = story.name,
                    color = HeritageGoldLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                    modifier = Modifier.testTag("story_detail_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "العودة للرئيسية",
                        tint = HeritageGoldPrimary
                    )
                }
            },
            actions = {
                // Puzzle Button direct access if unlocked
                if (progress?.isPuzzleUnlocked == true) {
                    IconButton(
                        onClick = { viewModel.initPuzzle(story.id) },
                        modifier = Modifier.testTag("story_top_puzzle_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Extension,
                            contentDescription = "بازل الشخصية",
                            tint = HeritageGoldPrimary
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ArabianNightSurface
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("story_detail_scroll"),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Character Hero Portrait Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    Image(
                        painter = painterResource(id = story.imageRes),
                        contentDescription = story.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        ArabianMidnight.copy(alpha = 0.7f),
                                        ArabianMidnight
                                    )
                                )
                            )
                    )

                    // Hero Info Badge
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                color = ArabianNightCard.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, HeritageGoldPrimary.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = story.era,
                                    color = HeritageGoldLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Surface(
                                color = ArabianNightCard.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DesertAmber.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = story.tribe,
                                    color = DesertAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = story.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ParchmentWhite
                        )
                    }
                }
            }

            // Famous Quote Banner
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    ArabianOrnamentalCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0xFF221633),
                        borderColor = HeritageGoldPrimary.copy(alpha = 0.6f)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚔️ مقولة مأثورة ⚔️",
                                fontSize = 12.sp,
                                color = HeritageGoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "«${story.famousQuote}»",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ParchmentWhite,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            // Chapter Tabs Selector
            item {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "فصول السيرة والتاريخ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeritageGoldLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(story.chapters) { index, chapter ->
                            val isSelected = selectedChapterIndex == index
                            val isChapterDone = index < completedChapters

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { selectedChapterIndex = index }
                                    .testTag("chapter_tab_$index"),
                                color = if (isSelected) HeritageGoldPrimary else if (isChapterDone) Color(0xFF241C35) else ArabianNightSurface,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) HeritageGoldLight else if (isChapterDone) OasisEmerald else ArabianNightBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isChapterDone) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = if (isSelected) ArabianMidnight else OasisEmerald,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = "الفصل ${chapter.id}",
                                        color = if (isSelected) ArabianMidnight else ParchmentWhite,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Selected Chapter Content
            item {
                ArabianOrnamentalCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("active_chapter_card"),
                    backgroundColor = ArabianNightCard
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = currentChapter.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = HeritageGoldLight
                        )
                        Text(
                            text = currentChapter.subtitle,
                            fontSize = 13.sp,
                            color = DesertAmber,
                            fontWeight = FontWeight.Medium
                        )

                        ArabianDivider(modifier = Modifier.padding(vertical = 10.dp))

                        Text(
                            text = currentChapter.content,
                            fontSize = 15.sp,
                            color = ParchmentWhite,
                            lineHeight = 26.sp,
                            textAlign = TextAlign.Justify
                        )

                        if (currentChapter.memorableQuote != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                color = Color(0xFF2D1E45),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, HeritageGoldPrimary.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "💡 ${currentChapter.memorableQuote}",
                                    fontSize = 13.sp,
                                    color = HeritageGoldLight,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        if (currentChapter.historicalContext != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "📌 إضاءة تاريخية: ${currentChapter.historicalContext}",
                                fontSize = 12.sp,
                                color = ParchmentDark,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Chapter Completion Button
                        val isThisChapterRead = selectedChapterIndex < completedChapters
                        Button(
                            onClick = {
                                val nextCount = maxOf(completedChapters, selectedChapterIndex + 1)
                                viewModel.markChapterComplete(story.id, nextCount, story.chapters.size)
                                if (selectedChapterIndex < story.chapters.size - 1) {
                                    selectedChapterIndex += 1
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("mark_chapter_read_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isThisChapterRead) OasisEmerald else HeritageGoldPrimary,
                                contentColor = if (isThisChapterRead) Color.White else ArabianMidnight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = if (isThisChapterRead) Icons.Default.CheckCircle else Icons.Default.Bookmark,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isThisChapterRead) "أتممت قراءة هذا الفصل ✓" else "إتمام قراءة الفصل والانتقال للتالي",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Poetry Section
            if (story.poems.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        story.poems.forEach { poem ->
                            poem.verses.forEach { verse ->
                                ArabianPoetryBanner(
                                    firstHemistich = verse.firstHemistich,
                                    secondHemistich = verse.secondHemistich,
                                    poetName = story.name
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }

            // Key Historical Facts Card
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "معالم وحقائق تاريخية",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeritageGoldLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    story.keyFacts.forEach { fact ->
                        ArabianOrnamentalCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            backgroundColor = ArabianNightSurface
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = fact.icon, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = fact.title,
                                        color = HeritageGoldLight,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = fact.description,
                                        color = ParchmentMuted,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Puzzle & Quiz Unlock Card at bottom of story
            item {
                ArabianOrnamentalCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("story_finish_actions_card"),
                    backgroundColor = if (isFullyCompleted) Color(0xFF261942) else ArabianNightCard,
                    borderColor = if (isFullyCompleted) HeritageGoldPrimary else ArabianNightBorder
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isFullyCompleted) "🎉 تم فك قفل بازل ${story.name}!" else "🔒 أكمل قراءة الفصول لفك قفل البازل",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFullyCompleted) HeritageGoldLight else ParchmentMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isFullyCompleted)
                                "لقد أتممت قراءة سيرة ${story.name} بنجاح! يمكنك الآن تركيب بازل الشخصية واختبار معلوماتك في الأحجية التاريخية."
                            else
                                "نظام التراث يربط تركيب صورة الشخصية بإتمام قراءتها لحفظ سيرة الفارس ووقائعه.",
                            fontSize = 13.sp,
                            color = ParchmentDark,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (isFullyCompleted) {
                                        viewModel.initPuzzle(story.id)
                                    } else {
                                        // Mark all done to test or read through
                                        viewModel.markStoryFullyRead(story.id, story.chapters.size)
                                        viewModel.initPuzzle(story.id)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("open_story_puzzle_cta"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HeritageGoldPrimary,
                                    contentColor = ArabianMidnight
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Extension, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "تركيب البازل 🧩", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewModel.startQuiz(story.id) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("open_story_quiz_cta"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DesertAmber,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "حل الأحجية ⚔️", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
