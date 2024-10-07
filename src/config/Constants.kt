package config

import models.Coordinate

object Constants {
    // элиас по числам и буквам(в словаре) Ключи: Это строки; Значения: Это индексы
    //    ("A" to 0,
    //    "B" to 1,
    //    "C" to 2,) ...
    val LETTER_TO_INDEX = ('A'..'Z').mapIndexed { index, c -> c.toString() to index }.toMap()

    // Список кораблей с их размером
    val SHIP_SIZES = listOf(5, 4, 3, 2, 2)

    // соседние поля
    val ADJACENT_DIRECTIONS = listOf(
        Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
        Pair(0, -1), Pair(0, 1),
        Pair(1, -1), Pair(1, 0), Pair(1, 1)
    )

    // Соседние поля для выстрелов, если компьютер попал в цель
    val DIRECTIONS = listOf(
        Coordinate(-1, 0), // Вверх
        Coordinate(1, 0),  // Вниз
        Coordinate(0, -1), // Влево
        Coordinate(0, 1)   // Вправо
    )

    // Каждая третья атака удачная
    const val LUCKY_MOVE = 3

    // header для таблиц
    val BOARD_HEADER = ('A'..'J').joinToString(" ") { it.toString() }
}
