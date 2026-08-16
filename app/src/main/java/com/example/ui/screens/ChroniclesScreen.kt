package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Stars
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
import com.example.data.model.HistoricalData
import com.example.ui.components.ArabianDivider
import com.example.ui.components.ArabianOrnamentalCard
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
fun ChroniclesScreen(
    viewModel: HakawatiViewModel,
    modifier: Modifier = Modifier
) {
    val allProgress by viewModel.allProgress.collectAsStateWithLifecycle()
    val userRank = viewModel.getUserRank()

    val totalStories = HistoricalData.stories.size
    val completedStories = allProgress.count { it.isCompleted }
    val solvedPuzzles = allProgress.count { it.isPuzzleSolved }
    val totalQuizScore = allProgress.sumOf { it.quizHighScore }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArabianMidnight)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "سجل المآثر والأوسمة",
                    color = HeritageGoldLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                    modifier = Modifier.testTag("chronicles_back_btn")
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("chronicles_list"),
            contentPadding = PaddingValues(16.dp, bottom = 96.dp)
        ) {
            // Storyteller Profile Card
            item {
                ArabianOrnamentalCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF221636),
                    borderColor = HeritageGoldPrimary
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(HeritageGoldPrimary.copy(alpha = 0.2f))
                                .border(2.dp, HeritageGoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 32.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = userRank,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = HeritageGoldLight,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "سجل إنجازات راوي التاريخ العربي ومجمع الألغاز",
                            fontSize = 12.sp,
                            color = ParchmentMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "القصص المكتملة", fontSize = 11.sp, color = ParchmentMuted)
                                Text(text = "$completedStories / $totalStories", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = HeritageGoldPrimary)
                            }
                            Box(modifier = Modifier.size(1.dp, 30.dp).background(ArabianNightBorder))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "البازل المحلول", fontSize = 11.sp, color = ParchmentMuted)
                                Text(text = "$solvedPuzzles / $totalStories", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OasisEmerald)
                            }
                            Box(modifier = Modifier.size(1.dp, 30.dp).background(ArabianNightBorder))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "نقاط الأحجيات", fontSize = 11.sp, color = ParchmentMuted)
                                Text(text = "$totalQuizScore ⭐", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DesertAmber)
                            }
                        }
                    }
                }
            }

            // Unlocked Legendary Artifact Cards
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "بطاقات الأبطال المذهبة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeritageGoldLight
                )
                Text(
                    text = "تُفتح البطاقة المذهبة عند حل بازل الشخصية",
                    fontSize = 12.sp,
                    color = ParchmentMuted
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(HistoricalData.stories) { story ->
                        val progress = allProgress.find { it.storyId == story.id }
                        val isCardUnlocked = progress?.isPuzzleSolved == true

                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .height(220.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isCardUnlocked) Color(0xFF2B1C45) else ArabianNightCard)
                                .border(
                                    1.5.dp,
                                    if (isCardUnlocked) HeritageGoldPrimary else ArabianNightBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    if (progress?.isPuzzleUnlocked == true) {
                                        viewModel.initPuzzle(story.id)
                                    } else {
                                        viewModel.navigateTo(ScreenDestination.StoryDetail(story.id))
                                    }
                                }
                                .padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Image(
                                        painter = painterResource(id = story.imageRes),
                                        contentDescription = story.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    if (!isCardUnlocked) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xCC0E0A16)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = ParchmentDark, modifier = Modifier.size(24.dp))
                                        }
                                    }
                                }

                                Text(
                                    text = story.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCardUnlocked) HeritageGoldLight else ParchmentDark,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )

                                Surface(
                                    color = if (isCardUnlocked) OasisEmerald.copy(alpha = 0.2f) else ArabianNightSurface,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isCardUnlocked) OasisEmerald else ArabianNightBorder)
                                ) {
                                    Text(
                                        text = if (isCardUnlocked) "بطاقة مذهبة 🏆" else "مغلقة 🔒",
                                        fontSize = 10.sp,
                                        color = if (isCardUnlocked) OasisEmerald else ParchmentDark,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Heritage Achievements Badges List
            item {
                Spacer(modifier = Modifier.height(20.dp))
                ArabianDivider()
                Text(
                    text = "الأوسمة ومقامات الحكمة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeritageGoldLight
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            val achievements = listOf(
                Triple("وسام حرب البسوس", "إتمام قراءة ملحمة الزير سالم وكليب بن ربيعة", completedStories >= 2),
                Triple("وسام بطل القادسية", "حل جميع أحجيات القعقاع بن عمرو بنجاح", (allProgress.find { it.storyId == "al_qaqa" }?.quizHighScore ?: 0) >= 3),
                Triple("وسام جود حاتم الطائي", "إتمام قراءة سيرة حاتم الطائي وفتح بازل شخصيته", allProgress.find { it.storyId == "hatim_al_tai" }?.isPuzzleUnlocked == true),
                Triple("وسام خبير البازل العربي", "حل 3 شخصيات في نظام البازل التفاعلي", solvedPuzzles >= 3),
                Triple("وسام خاتم الحكايات", "قراءة جميع القصص الأربع وتجميع بطاقاتها", completedStories >= 4)
            )

            items(achievements) { (title, desc, isUnlocked) ->
                ArabianOrnamentalCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    backgroundColor = if (isUnlocked) Color(0xFF1E152E) else ArabianNightSurface,
                    borderColor = if (isUnlocked) HeritageGoldPrimary.copy(alpha = 0.5f) else ArabianNightBorder
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isUnlocked) HeritageGoldPrimary.copy(alpha = 0.2f) else ArabianNightCard)
                                .border(1.dp, if (isUnlocked) HeritageGoldPrimary else ArabianNightBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = if (isUnlocked) "🏅" else "🔒", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) HeritageGoldLight else ParchmentMuted
                            )
                            Text(
                                text = desc,
                                fontSize = 12.sp,
                                color = ParchmentDark
                            )
                        }

                        if (isUnlocked) {
                            Text(
                                text = "مكتمل ✓",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OasisEmerald
                            )
                        }
                    }
                }
            }
        }
    }
}
