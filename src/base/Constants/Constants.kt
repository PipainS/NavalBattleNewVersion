package base

object Constants {
    val LETTER_TO_INDEX = ('A'..'Z').mapIndexed { index, c -> c.toString() to index }.toMap()
    val SHIP_SIZES = listOf(5, 4, 3, 3, 2)
    // соседние поля
    val ADJACENT_DIRECTIONS = listOf(
        Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
        Pair(0, -1), Pair(0, 1),
        Pair(1, -1), Pair(1, 0), Pair(1, 1)
    )
    val BOARD_HEADER = ('A'..'J').joinToString(" ") { it.toString() }
}
