package com.example.data.local

import kotlinx.coroutines.flow.Flow

class HakawatiRepository(private val dao: StoryProgressDao) {

    val allProgress: Flow<List<StoryProgressEntity>> = dao.getAllProgress()

    fun getStoryProgress(storyId: String): Flow<StoryProgressEntity?> {
        return dao.getProgressForStory(storyId)
    }

    suspend fun markChapterRead(storyId: String, currentChapter: Int, totalChapters: Int) {
        val existing = dao.getProgressForStoryDirect(storyId) ?: StoryProgressEntity(
            storyId = storyId,
            completedChaptersCount = currentChapter
        )
        val newCompletedCount = maxOf(existing.completedChaptersCount, currentChapter)
        val isStoryFinished = newCompletedCount >= totalChapters

        val updated = existing.copy(
            completedChaptersCount = newCompletedCount,
            isCompleted = existing.isCompleted || isStoryFinished,
            isPuzzleUnlocked = existing.isPuzzleUnlocked || isStoryFinished,
            lastReadTimestamp = System.currentTimeMillis()
        )
        dao.upsertProgress(updated)
    }

    suspend fun markStoryFullyCompleted(storyId: String, totalChapters: Int) {
        dao.markStoryCompleted(storyId, totalChapters)
    }

    suspend fun recordPuzzleWin(storyId: String, moves: Int, timeSeconds: Int) {
        val existing = dao.getProgressForStoryDirect(storyId)
        if (existing == null) {
            dao.upsertProgress(
                StoryProgressEntity(
                    storyId = storyId,
                    isPuzzleSolved = true,
                    puzzleBestMoves = moves,
                    puzzleBestTimeSeconds = timeSeconds
                )
            )
        } else {
            dao.recordPuzzleSolved(storyId, moves, timeSeconds)
        }
    }

    suspend fun recordQuizScore(storyId: String, score: Int, total: Int) {
        val existing = dao.getProgressForStoryDirect(storyId)
        if (existing == null) {
            dao.upsertProgress(
                StoryProgressEntity(
                    storyId = storyId,
                    quizHighScore = score,
                    quizTotalQuestions = total
                )
            )
        } else {
            dao.recordQuizScore(storyId, score, total)
        }
    }
}
