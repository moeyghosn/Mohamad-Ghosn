package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.audio.AudioAmbianceManager
import com.example.data.local.AppDatabase
import com.example.data.local.HakawatiRepository
import com.example.data.local.StoryProgressEntity
import com.example.data.model.CharacterStory
import com.example.data.model.HistoricalData
import com.example.data.model.RiddleQuestion
import com.example.data.model.UserAchievement
import com.example.data.puzzle.PuzzleGameState
import com.example.data.puzzle.PuzzleTile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Home : ScreenDestination()
    data class StoryDetail(val storyId: String) : ScreenDestination()
    data class RiddleQuiz(val storyId: String) : ScreenDestination()
    data class CharacterPuzzle(val storyId: String) : ScreenDestination()
    object Chronicles : ScreenDestination()
    object AllPuzzles : ScreenDestination()
}

class HakawatiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HakawatiRepository
    private val ambianceManager = AudioAmbianceManager()

    val allProgress: StateFlow<List<StoryProgressEntity>>

    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _isAmbiancePlaying = MutableStateFlow(false)
    val isAmbiancePlaying: StateFlow<Boolean> = _isAmbiancePlaying.asStateFlow()

    // Quiz State
    private val _currentQuizStory = MutableStateFlow<CharacterStory?>(null)
    val currentQuizStory: StateFlow<CharacterStory?> = _currentQuizStory.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswerIndex = MutableStateFlow<Int?>(null)
    val selectedAnswerIndex: StateFlow<Int?> = _selectedAnswerIndex.asStateFlow()

    private val _isAnswerSubmitted = MutableStateFlow(false)
    val isAnswerSubmitted: StateFlow<Boolean> = _isAnswerSubmitted.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _quizFinished = MutableStateFlow(false)
    val quizFinished: StateFlow<Boolean> = _quizFinished.asStateFlow()

    // Puzzle State
    private val _puzzleState = MutableStateFlow<PuzzleGameState?>(null)
    val puzzleState: StateFlow<PuzzleGameState?> = _puzzleState.asStateFlow()

    private var puzzleTimerJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = HakawatiRepository(db.storyProgressDao())
        allProgress = repository.allProgress.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun toggleAmbiance() {
        ambianceManager.toggleAmbiance { isPlaying ->
            _isAmbiancePlaying.value = isPlaying
        }
    }

    // Story Reading Progression
    fun markChapterComplete(storyId: String, chapterNumber: Int, totalChapters: Int) {
        viewModelScope.launch {
            repository.markChapterRead(storyId, chapterNumber, totalChapters)
        }
    }

    fun markStoryFullyRead(storyId: String, totalChapters: Int) {
        viewModelScope.launch {
            repository.markStoryFullyCompleted(storyId, totalChapters)
        }
    }

    // Quiz Actions
    fun startQuiz(storyId: String) {
        val story = HistoricalData.getStoryById(storyId) ?: return
        _currentQuizStory.value = story
        _currentQuestionIndex.value = 0
        _selectedAnswerIndex.value = null
        _isAnswerSubmitted.value = false
        _quizScore.value = 0
        _quizFinished.value = false
        navigateTo(ScreenDestination.RiddleQuiz(storyId))
    }

    fun selectQuizAnswer(index: Int) {
        if (_isAnswerSubmitted.value) return
        _selectedAnswerIndex.value = index
    }

    fun submitQuizAnswer() {
        if (_isAnswerSubmitted.value || _selectedAnswerIndex.value == null) return
        val story = _currentQuizStory.value ?: return
        val question = story.quizzes.getOrNull(_currentQuestionIndex.value) ?: return

        _isAnswerSubmitted.value = true
        if (_selectedAnswerIndex.value == question.correctIndex) {
            _quizScore.value += 1
        }
    }

    fun nextQuizQuestion() {
        val story = _currentQuizStory.value ?: return
        val nextIdx = _currentQuestionIndex.value + 1
        if (nextIdx < story.quizzes.size) {
            _currentQuestionIndex.value = nextIdx
            _selectedAnswerIndex.value = null
            _isAnswerSubmitted.value = false
        } else {
            _quizFinished.value = true
            viewModelScope.launch {
                repository.recordQuizScore(story.id, _quizScore.value, story.quizzes.size)
            }
        }
    }

    // Puzzle Actions
    fun initPuzzle(storyId: String, gridSize: Int = 3) {
        val story = HistoricalData.getStoryById(storyId) ?: return
        val progressList = allProgress.value
        val isStoryUnlocked = progressList.find { it.storyId == storyId }?.isPuzzleUnlocked == true

        val totalTiles = gridSize * gridSize
        // Generate a valid shuffled permutation
        val shuffledIndices = (0 until totalTiles).shuffled()
        val tiles = (0 until totalTiles).map { index ->
            PuzzleTile(id = shuffledIndices[index], currentPosition = index)
        }

        _puzzleState.value = PuzzleGameState(
            storyId = storyId,
            characterName = story.name,
            imageRes = story.imageRes,
            gridSize = gridSize,
            tiles = tiles,
            movesCount = 0,
            timeElapsedSeconds = 0,
            isCompleted = false,
            isUnlocked = isStoryUnlocked
        )

        startPuzzleTimer()
        navigateTo(ScreenDestination.CharacterPuzzle(storyId))
    }

    private fun startPuzzleTimer() {
        puzzleTimerJob?.cancel()
        puzzleTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = _puzzleState.value ?: break
                if (current.isCompleted) break
                _puzzleState.value = current.copy(
                    timeElapsedSeconds = current.timeElapsedSeconds + 1
                )
            }
        }
    }

    fun onTileClicked(slotIndex: Int) {
        val current = _puzzleState.value ?: return
        if (current.isCompleted || !current.isUnlocked) return

        val selected = current.selectedTileIndex
        if (selected == null) {
            // First selection
            _puzzleState.value = current.copy(selectedTileIndex = slotIndex)
        } else if (selected == slotIndex) {
            // Deselect
            _puzzleState.value = current.copy(selectedTileIndex = null)
        } else {
            // Swap tiles at selected and slotIndex
            val newTiles = current.tiles.toMutableList()
            val tileA = newTiles[selected]
            val tileB = newTiles[slotIndex]

            newTiles[selected] = tileB.copy(currentPosition = selected)
            newTiles[slotIndex] = tileA.copy(currentPosition = slotIndex)

            val newMoves = current.movesCount + 1
            // Check win condition: every tile's id == its currentPosition
            val isWon = newTiles.all { it.id == it.currentPosition }

            _puzzleState.value = current.copy(
                tiles = newTiles,
                selectedTileIndex = null,
                movesCount = newMoves,
                isCompleted = isWon
            )

            if (isWon) {
                puzzleTimerJob?.cancel()
                viewModelScope.launch {
                    repository.recordPuzzleWin(
                        storyId = current.storyId,
                        moves = newMoves,
                        timeSeconds = current.timeElapsedSeconds
                    )
                }
            }
        }
    }

    fun restartPuzzle() {
        val current = _puzzleState.value ?: return
        initPuzzle(current.storyId, current.gridSize)
    }

    fun getStoryProgress(storyId: String): StoryProgressEntity? {
        return allProgress.value.find { it.storyId == storyId }
    }

    fun getUserRank(): String {
        val totalCompleted = allProgress.value.count { it.isCompleted }
        val puzzlesSolved = allProgress.value.count { it.isPuzzleSolved }
        return when {
            totalCompleted >= 4 && puzzlesSolved >= 3 -> "👑 شيخ الرواة وأمير البيان"
            totalCompleted >= 3 -> "⚔️ حكواتي القبيلة المعتمد"
            totalCompleted >= 1 -> "🏺 حافظ الأثر والروايات"
            else -> "📜 راوٍ مبتدئ في مجالس الحكمة"
        }
    }

    override fun onCleared() {
        super.onCleared()
        ambianceManager.stop()
        puzzleTimerJob?.cancel()
    }
}
