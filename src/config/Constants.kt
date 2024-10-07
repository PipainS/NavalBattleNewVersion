package config

import models.Coordinate

object Constants {
    val LETTER_TO_INDEX = ('A'..'Z').mapIndexed { index, c -> c.toString() to index }.toMap()
    val SHIP_SIZES = listOf(5, 4, 3, 2, 2)
    // соседние поля
    val ADJACENT_DIRECTIONS = listOf(
        Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
        Pair(0, -1), Pair(0, 1),
        Pair(1, -1), Pair(1, 0), Pair(1, 1)
    )
    val DIRECTIONS = listOf(
        Coordinate(-1, 0), // Вверх
        Coordinate(1, 0),  // Вниз
        Coordinate(0, -1), // Влево
        Coordinate(0, 1)   // Вправо
    )
    const val LUCKY_MOVE = 3
    val BOARD_HEADER = ('A'..'J').joinToString(" ") { it.toString() }
}
