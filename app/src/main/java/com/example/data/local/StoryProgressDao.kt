package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryProgressDao {

    @Query("SELECT * FROM story_progress")
    fun getAllProgress(): Flow<List<StoryProgressEntity>>

    @Query("SELECT * FROM story_progress WHERE storyId = :storyId LIMIT 1")
    fun getProgressForStory(storyId: String): Flow<StoryProgressEntity?>

    @Query("SELECT * FROM story_progress WHERE storyId = :storyId LIMIT 1")
    suspend fun getProgressForStoryDirect(storyId: String): StoryProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: StoryProgressEntity)

    @Query("UPDATE story_progress SET isCompleted = 1, isPuzzleUnlocked = 1, completedChaptersCount = :chaptersCount, lastReadTimestamp = :timestamp WHERE storyId = :storyId")
    suspend fun markStoryCompleted(storyId: String, chaptersCount: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE story_progress SET isPuzzleSolved = 1, puzzleBestMoves = CASE WHEN puzzleBestMoves == 0 OR :moves < puzzleBestMoves THEN :moves ELSE puzzleBestMoves END, puzzleBestTimeSeconds = CASE WHEN puzzleBestTimeSeconds == 0 OR :timeSec < puzzleBestTimeSeconds THEN :timeSec ELSE puzzleBestTimeSeconds END WHERE storyId = :storyId")
    suspend fun recordPuzzleSolved(storyId: String, moves: Int, timeSec: Int)

    @Query("UPDATE story_progress SET quizHighScore = CASE WHEN :score > quizHighScore THEN :score ELSE quizHighScore END, quizTotalQuestions = :total WHERE storyId = :storyId")
    suspend fun recordQuizScore(storyId: String, score: Int, total: Int)
}
