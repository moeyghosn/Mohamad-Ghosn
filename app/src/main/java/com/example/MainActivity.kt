package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ArabianHeritageScaffoldBackground
import com.example.ui.screens.AllPuzzlesScreen
import com.example.ui.screens.CharacterPuzzleScreen
import com.example.ui.screens.ChroniclesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RiddleQuizScreen
import com.example.ui.screens.StoryDetailScreen
import com.example.ui.theme.ArabianMidnight
import com.example.ui.theme.ArabianNightBorder
import com.example.ui.theme.ArabianNightCard
import com.example.ui.theme.ArabianNightCardElevated
import com.example.ui.theme.ArabianNightSurface
import com.example.ui.theme.HakawatiTheme
import com.example.ui.theme.HeritageGoldLight
import com.example.ui.theme.HeritageGoldPrimary
import com.example.ui.theme.ParchmentDark
import com.example.ui.theme.ParchmentMuted
import com.example.ui.viewmodel.HakawatiViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {

    private val viewModel: HakawatiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HakawatiTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    HakawatiApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun HakawatiApp(viewModel: HakawatiViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    val showBottomBar = currentScreen is ScreenDestination.Home ||
            currentScreen is ScreenDestination.AllPuzzles ||
            currentScreen is ScreenDestination.Chronicles

    ArabianHeritageScaffoldBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                if (showBottomBar) {
                    HakawatiBottomNavigationBar(
                        currentScreen = currentScreen,
                        onNavigate = { dest -> viewModel.navigateTo(dest) }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_transition"
                ) { screen ->
                    when (screen) {
                        is ScreenDestination.Home -> HomeScreen(viewModel = viewModel)
                        is ScreenDestination.StoryDetail -> StoryDetailScreen(
                            storyId = screen.storyId,
                            viewModel = viewModel
                        )
                        is ScreenDestination.RiddleQuiz -> RiddleQuizScreen(
                            storyId = screen.storyId,
                            viewModel = viewModel
                        )
                        is ScreenDestination.CharacterPuzzle -> CharacterPuzzleScreen(
                            storyId = screen.storyId,
                            viewModel = viewModel
                        )
                        is ScreenDestination.AllPuzzles -> AllPuzzlesScreen(viewModel = viewModel)
                        is ScreenDestination.Chronicles -> ChroniclesScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun HakawatiBottomNavigationBar(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .border(
                1.dp,
                ArabianNightBorder,
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .testTag("hakawati_bottom_nav"),
        containerColor = ArabianNightSurface,
        tonalElevation = 8.dp
    ) {
        // Tab 1: Stories
        NavigationBarItem(
            selected = currentScreen is ScreenDestination.Home,
            onClick = { onNavigate(ScreenDestination.Home) },
            icon = {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "السير والقصص",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "السير والملاحم",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen is ScreenDestination.Home) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ArabianMidnight,
                selectedTextColor = HeritageGoldLight,
                indicatorColor = HeritageGoldPrimary,
                unselectedIconColor = ParchmentDark,
                unselectedTextColor = ParchmentDark
            ),
            modifier = Modifier.testTag("nav_tab_stories")
        )

        // Tab 2: Puzzles Gallery
        NavigationBarItem(
            selected = currentScreen is ScreenDestination.AllPuzzles,
            onClick = { onNavigate(ScreenDestination.AllPuzzles) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Extension,
                    contentDescription = "بازل الشخصيات",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "بازل الشخصيات",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen is ScreenDestination.AllPuzzles) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ArabianMidnight,
                selectedTextColor = HeritageGoldLight,
                indicatorColor = HeritageGoldPrimary,
                unselectedIconColor = ParchmentDark,
                unselectedTextColor = ParchmentDark
            ),
            modifier = Modifier.testTag("nav_tab_puzzles")
        )

        // Tab 3: Chronicles & Badges
        NavigationBarItem(
            selected = currentScreen is ScreenDestination.Chronicles,
            onClick = { onNavigate(ScreenDestination.Chronicles) },
            icon = {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "المآثر والأوسمة",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "سجل المآثر",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen is ScreenDestination.Chronicles) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ArabianMidnight,
                selectedTextColor = HeritageGoldLight,
                indicatorColor = HeritageGoldPrimary,
                unselectedIconColor = ParchmentDark,
                unselectedTextColor = ParchmentDark
            ),
            modifier = Modifier.testTag("nav_tab_chronicles")
        )
    }
}
