package com.example.data.model

import androidx.annotation.DrawableRes

data class CharacterStory(
    val id: String,
    val name: String,
    val title: String,
    val era: String,
    val tribe: String,
    val famousQuote: String,
    val shortBio: String,
    val overview: String,
    @DrawableRes val imageRes: Int,
    val chapters: List<StoryChapter>,
    val poems: List<ArabicPoem>,
    val quizzes: List<RiddleQuestion>,
    val keyFacts: List<HistoricalFact>,
    val puzzleGridSize: Int = 3 // 3x3 default puzzle
)

data class StoryChapter(
    val id: Int,
    val title: String,
    val subtitle: String,
    val content: String,
    val memorableQuote: String? = null,
    val historicalContext: String? = null
)

data class ArabicPoem(
    val title: String,
    val verses: List<PoemVerse>,
    val occasion: String
)

data class PoemVerse(
    val firstHemistich: String, // الصدر
    val secondHemistich: String // العجز
)

data class RiddleQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val hint: String
)

data class HistoricalFact(
    val title: String,
    val description: String,
    val icon: String = "📜"
)

data class UserAchievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val progress: Float = 0f
)
