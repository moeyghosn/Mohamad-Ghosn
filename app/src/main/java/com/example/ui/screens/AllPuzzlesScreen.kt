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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun AllPuzzlesScreen(
    viewModel: HakawatiViewModel,
    modifier: Modifier = Modifier
) {
    val allProgress by viewModel.allProgress.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArabianMidnight)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "بازل أبطال التاريخ العربي",
                    color = HeritageGoldLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                    modifier = Modifier.testTag("all_puzzles_back_btn")
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .testTag("all_puzzles_grid"),
            contentPadding = PaddingValues(16.dp, bottom = 96.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(HistoricalData.stories) { story ->
                val progress = allProgress.find { it.storyId == story.id }
                val isUnlocked = progress?.isPuzzleUnlocked == true || progress?.isCompleted == true
                val isSolved = progress?.isPuzzleSolved == true

                ArabianOrnamentalCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            if (isUnlocked) {
                                viewModel.initPuzzle(story.id)
                            } else {
                                viewModel.navigateTo(ScreenDestination.CharacterPuzzle(story.id))
                            }
                        }
                        .testTag("puzzle_grid_item_${story.id}"),
                    backgroundColor = if (isUnlocked) ArabianNightCard else Color(0xFF1B1425),
                    borderColor = if (isSolved) OasisEmerald else if (isUnlocked) HeritageGoldPrimary else ArabianNightBorder
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = painterResource(id = story.imageRes),
                                contentDescription = story.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            if (!isUnlocked) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xDD0E0A16)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = DesertAmber,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "أكمل القراءة لفك القفل",
                                            color = HeritageGoldLight,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else if (isSolved) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(6.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(OasisEmerald)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "محلول ✓",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = story.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) HeritageGoldLight else ParchmentDark,
                            maxLines = 1
                        )

                        Text(
                            text = story.title,
                            fontSize = 11.sp,
                            color = if (isUnlocked) DesertAmber else ParchmentDark,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isSolved && progress != null) {
                            val timeSec = progress.puzzleBestTimeSeconds
                            val formatted = String.format("%02d:%02d", timeSec / 60, timeSec % 60)
                            Text(
                                text = "أفضل رقم: ${progress.puzzleBestMoves} حركة ($formatted)",
                                fontSize = 10.sp,
                                color = OasisEmerald,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = if (isUnlocked) "جاهز للتركيب 🧩" else "مغلق بنظام التقدم 🔒",
                                fontSize = 10.sp,
                                color = if (isUnlocked) HeritageGoldPrimary else ParchmentDark
                            )
                        }
                    }
                }
            }
        }
    }
}
