package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.HistoricalData
import com.example.data.puzzle.PuzzleGameState
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
fun CharacterPuzzleScreen(
    storyId: String,
    viewModel: HakawatiViewModel,
    modifier: Modifier = Modifier
) {
    val puzzleState by viewModel.puzzleState.collectAsStateWithLifecycle()
    val allProgress by viewModel.allProgress.collectAsStateWithLifecycle()
    val story = HistoricalData.getStoryById(storyId) ?: return

    val progress = allProgress.find { it.storyId == storyId }
    val isStoryCompleted = progress?.isCompleted == true || progress?.isPuzzleUnlocked == true

    var showOriginalImageDialog by remember { mutableStateOf(false) }

    // Synchronize or initialize if puzzle state not set
    LaunchedEffect(storyId, isStoryCompleted) {
        if (puzzleState == null || puzzleState?.storyId != storyId) {
            viewModel.initPuzzle(storyId)
        }
    }

    val state = puzzleState

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArabianMidnight)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "بازل ${story.name}",
                    color = HeritageGoldLight,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.StoryDetail(storyId)) },
                    modifier = Modifier.testTag("puzzle_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = HeritageGoldPrimary
                    )
                }
            },
            actions = {
                if (state?.isUnlocked == true || isStoryCompleted) {
                    IconButton(
                        onClick = { showOriginalImageDialog = true },
                        modifier = Modifier.testTag("preview_original_image_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "معاينة اللوحة الأصلية",
                            tint = HeritageGoldPrimary
                        )
                    }
                    IconButton(
                        onClick = { viewModel.restartPuzzle() },
                        modifier = Modifier.testTag("restart_puzzle_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "إعادة ترتيب البازل",
                            tint = HeritageGoldPrimary
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ArabianNightSurface)
        )

        if (!isStoryCompleted && state?.isUnlocked != true) {
            // Locked Screen State
            LockedPuzzleView(
                characterName = story.name,
                onReadStory = { viewModel.navigateTo(ScreenDestination.StoryDetail(storyId)) },
                onUnlockQuickly = {
                    viewModel.markStoryFullyRead(storyId, story.chapters.size)
                    viewModel.initPuzzle(storyId)
                }
            )
        } else if (state != null) {
            // Unlocked Game State
            ActivePuzzleGameView(
                state = state,
                onTileClick = { slotIndex -> viewModel.onTileClicked(slotIndex) },
                onShowPreview = { showOriginalImageDialog = true },
                onRestart = { viewModel.restartPuzzle() },
                onReadStory = { viewModel.navigateTo(ScreenDestination.StoryDetail(storyId)) },
                onStartQuiz = { viewModel.startQuiz(storyId) }
            )
        }
    }

    // Modal Dialog to view the complete original portrait
    if (showOriginalImageDialog) {
        AlertDialog(
            onDismissRequest = { showOriginalImageDialog = false },
            confirmButton = {
                Button(
                    onClick = { showOriginalImageDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = HeritageGoldPrimary)
                ) {
                    Text(text = "العودة للبازل", color = ArabianMidnight, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = "اللوحة الأصلية: ${story.name}",
                    color = HeritageGoldLight,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, HeritageGoldPrimary, RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = story.imageRes),
                            contentDescription = story.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "طابق القطع المجزأة لتحصل على هذه الصورة التراثية الكاملة.",
                        fontSize = 12.sp,
                        color = ParchmentMuted,
                        textAlign = TextAlign.Center
                    )
                }
            },
            containerColor = ArabianNightCard
        )
    }
}

@Composable
fun LockedPuzzleView(
    characterName: String,
    onReadStory: () -> Unit,
    onUnlockQuickly: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag("locked_puzzle_view"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF221633))
                .border(2.dp, DesertAmber, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "بازل مقفل",
                tint = DesertAmber,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "بازل $characterName مقفل 🔒",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = HeritageGoldLight
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "وفقاً لنظام تقدم حكواتي؛ لا يُفتح بازل أي شخصية إلا بعد إتمام قراءة فصول قصتها بالكامل. اقرأ سيرة الفارس للتعرف على مآثره ثم عُد لتركيب لوحته التاريخية!",
            fontSize = 14.sp,
            color = ParchmentMuted,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onReadStory,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("locked_read_story_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = HeritageGoldPrimary,
                contentColor = ArabianMidnight
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "ابدأ قراءة السيرة لفك القفل 📜", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onUnlockQuickly,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("quick_unlock_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = HeritageGoldLight),
            border = androidx.compose.foundation.BorderStroke(1.dp, ArabianNightBorder)
        ) {
            Text(text = "تخطي وقراءة سريعة (فك القفل الآن) ⚡", fontSize = 13.sp)
        }
    }
}

@Composable
fun ActivePuzzleGameView(
    state: PuzzleGameState,
    onTileClick: (Int) -> Unit,
    onShowPreview: () -> Unit,
    onRestart: () -> Unit,
    onReadStory: () -> Unit,
    onStartQuiz: () -> Unit
) {
    val context = LocalContext.current
    val gridSize = state.gridSize
    val totalTiles = gridSize * gridSize

    // Load and slice image bitmap into memory
    val imageSlices = remember(state.imageRes, gridSize) {
        try {
            val originalBitmap = BitmapFactory.decodeResource(context.resources, state.imageRes)
            val size = minOf(originalBitmap.width, originalBitmap.height)
            // Center crop square
            val xOffset = (originalBitmap.width - size) / 2
            val yOffset = (originalBitmap.height - size) / 2
            val squareBitmap = Bitmap.createBitmap(originalBitmap, xOffset, yOffset, size, size)

            val sliceSize = size / gridSize
            val slices = mutableListOf<Bitmap>()

            for (row in 0 until gridSize) {
                for (col in 0 until gridSize) {
                    val slice = Bitmap.createBitmap(
                        squareBitmap,
                        col * sliceSize,
                        row * sliceSize,
                        sliceSize,
                        sliceSize
                    )
                    slices.add(slice)
                }
            }
            slices
        } catch (e: Exception) {
            emptyList<Bitmap>()
        }
    }

    val minutes = state.timeElapsedSeconds / 60
    val seconds = state.timeElapsedSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    val correctCount = state.correctTilesCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("active_puzzle_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Game Dashboard: Moves, Time, Accuracy
        ArabianOrnamentalCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = ArabianNightCard
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "الحركات", fontSize = 11.sp, color = ParchmentMuted)
                    Text(
                        text = "${state.movesCount}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = HeritageGoldLight
                    )
                }

                Box(modifier = Modifier.size(1.dp, 30.dp).background(ArabianNightBorder))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "الوقت المنقضي", fontSize = 11.sp, color = ParchmentMuted)
                    Text(
                        text = formattedTime,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = DesertAmber
                    )
                }

                Box(modifier = Modifier.size(1.dp, 30.dp).background(ArabianNightBorder))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "القطع الصحيحة", fontSize = 11.sp, color = ParchmentMuted)
                    Text(
                        text = "$correctCount / $totalTiles",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = if (correctCount == totalTiles) OasisEmerald else HeritageGoldPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Instruction Tip
        Text(
            text = if (state.isCompleted) "🎉 أتممت تجميع لوحة ${state.characterName} بنجاح!" else "اضغط على القطعة الأولى ثم اضغط على الثانية لتبديل مكانهما حتى تكتمل اللوحة.",
            fontSize = 12.sp,
            color = if (state.isCompleted) OasisEmerald else ParchmentMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Puzzle Grid (3x3)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(ArabianNightSurface)
                .border(2.dp, if (state.isCompleted) OasisEmerald else HeritageGoldPrimary, RoundedCornerShape(16.dp))
                .padding(6.dp)
                .testTag("puzzle_grid")
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridSize),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                userScrollEnabled = false
            ) {
                itemsIndexed(state.tiles) { slotIndex, tile ->
                    val isSelected = state.selectedTileIndex == slotIndex
                    val isCorrectPosition = tile.id == slotIndex
                    val sliceBitmap = imageSlices.getOrNull(tile.id)

                    val infiniteTransition = rememberInfiniteTransition(label = "tile_selection")
                    val pulseBorderAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.5f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulse"
                    )

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ArabianNightCardElevated)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = when {
                                    isSelected -> HeritageGoldPrimary.copy(alpha = pulseBorderAlpha)
                                    state.isCompleted || isCorrectPosition -> OasisEmerald.copy(alpha = 0.8f)
                                    else -> ArabianNightBorder
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(enabled = !state.isCompleted) {
                                onTileClick(slotIndex)
                            }
                            .testTag("puzzle_tile_$slotIndex")
                    ) {
                        if (sliceBitmap != null) {
                            Image(
                                bitmap = sliceBitmap.asImageBitmap(),
                                contentDescription = "قطعة بازل ${tile.id + 1}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Fallback number tile if bitmap decoding pending
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${tile.id + 1}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ParchmentWhite
                                )
                            }
                        }

                        // Correct spot indicator dot
                        if (isCorrectPosition && !state.isCompleted) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(OasisEmerald)
                                    .border(1.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }

                        // Selection indicator ring
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(HeritageGoldPrimary.copy(alpha = 0.25f))
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Victory Celebration Card when solved
        AnimatedVisibility(visible = state.isCompleted) {
            ArabianOrnamentalCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("puzzle_victory_card"),
                backgroundColor = Color(0xFF261942),
                borderColor = HeritageGoldPrimary
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "👑 تهانينا يا حكواتي!", fontSize = 20.sp, fontWeight = FontWeight.Black, color = HeritageGoldLight)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "جمعت لوحة ${state.characterName} التذكارية في ${state.movesCount} حركة وخلال $formattedTime دقيقة!",
                        fontSize = 13.sp,
                        color = ParchmentWhite,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onStartQuiz,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = HeritageGoldPrimary, contentColor = ArabianMidnight)
                        ) {
                            Icon(imageVector = Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "حل الأحجية ⚔️", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onRestart,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ArabianNightCardElevated)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "إعادة البازل 🔄", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Auxiliary actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onShowPreview,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HeritageGoldLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, ArabianNightBorder)
            ) {
                Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "معاينة اللوحة", fontSize = 13.sp)
            }

            OutlinedButton(
                onClick = onReadStory,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HeritageGoldLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, ArabianNightBorder)
            ) {
                Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "سيرة الشخصية", fontSize = 13.sp)
            }
        }
    }
}
