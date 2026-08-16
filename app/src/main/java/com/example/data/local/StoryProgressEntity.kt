package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_progress")
data class StoryProgressEntity(
    @PrimaryKey val storyId: String,
    val isCompleted: Boolean = false,
    val completedChaptersCount: Int = 0,
    val isPuzzleUnlocked: Boolean = false,
    val isPuzzleSolved: Boolean = false,
    val puzzleBestMoves: Int = 0,
    val puzzleBestTimeSeconds: Int = 0,
    val quizHighScore: Int = 0,
    val quizTotalQuestions: Int = 0,
    val lastReadTimestamp: Long = System.currentTimeMillis()
)
