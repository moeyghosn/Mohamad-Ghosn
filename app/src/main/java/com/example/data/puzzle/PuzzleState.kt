package com.example.data.puzzle

data class PuzzleTile(
    val id: Int,             // Original index (0..gridSize*gridSize - 1)
    val currentPosition: Int // Current slot index in the grid
)

data class PuzzleGameState(
    val storyId: String,
    val characterName: String,
    val imageRes: Int,
    val gridSize: Int = 3,
    val tiles: List<PuzzleTile> = emptyList(),
    val selectedTileIndex: Int? = null,
    val movesCount: Int = 0,
    val timeElapsedSeconds: Int = 0,
    val isCompleted: Boolean = false,
    val isUnlocked: Boolean = false
) {
    fun isTileInCorrectPosition(tile: PuzzleTile): Boolean {
        return tile.id == tile.currentPosition
    }

    val correctTilesCount: Int
        get() = tiles.count { it.id == it.currentPosition }
}
