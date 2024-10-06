package Base.Abstract

import Base.Board.Board
import Base.Color.AnsiColors
import Base.Coordinate
import Base.Enum.CellStatus
import Base.Enum.Orientation
import Base.Ship
import Base.Constants

class HumanPlayer(board: Board) : Player(board) {

    override fun makeMove(opponentBoard: Board): Coordinate {
        while (true) {
            try {
                println("${AnsiColors.ANSI_YELLOW}Введите координаты (например, A 0): ${AnsiColors.ANSI_RESET}")

                val input = readlnOrNull() ?: throw IllegalArgumentException("Ввод не может быть пустым")
                val (letter, xStr) = input.split(" ")

                val y = Constants.LETTER_TO_INDEX[letter.uppercase()] ?: throw IllegalArgumentException("Недопустимая координата")
                val x = xStr.toIntOrNull() ?: throw IllegalArgumentException("Недопустимая координата")

                // Проверка, стреляли ли уже в эту клетку
                if (opponentBoard.grid[x][y].status == CellStatus.MISS ||
                    opponentBoard.grid[x][y].status == CellStatus.HIT) {
                    println("${AnsiColors.ANSI_RED}Вы уже стреляли сюда! Попробуйте снова.${AnsiColors.ANSI_RESET}")
                    continue
                }

                return Coordinate(x, y)
            } catch (e: Exception) {
                println("${AnsiColors.ANSI_RED}Недопустимый ввод. Пожалуйста, попробуйте снова.${AnsiColors.ANSI_RESET}")
            }
        }
    }

    override fun placeShips() {
        println("${AnsiColors.ANSI_YELLOW}Разместите свои корабли${AnsiColors.ANSI_RESET}")
        displayBoard(board)

        for (size in Constants.SHIP_SIZES) {
            var placed = false
            while (!placed) {
                try {
                    println("${AnsiColors.ANSI_YELLOW}Введите координаты и ориентацию (H/V) для " +
                            "корабля размером $size (например, A 0 H): ${AnsiColors.ANSI_RESET}")

                    val input = readlnOrNull() ?: throw IllegalArgumentException("Недопустимая координата")
                    val (letter, xStr, orientationInput) = input.split(" ")

                    val y = Constants.LETTER_TO_INDEX[letter.uppercase()] ?: throw IllegalArgumentException("Недопустимая координата")
                    val x = xStr.toIntOrNull() ?: throw IllegalArgumentException("Недопустимая координата")

                    val orientation = when (orientationInput.uppercase()) {
                        "H" -> Orientation.HORIZONTAL
                        "V" -> Orientation.VERTICAL
                        else -> throw IllegalArgumentException("Недопустимый ввод ориентации. " +
                                "Пожалуйста, введите 'H' для горизонтального или 'V' для вертикального.")
                    }
                    val coordinates = generateCoordinates(size, Coordinate(x, y), orientation)

                    val ship = Ship(size, coordinates, orientation)
                    placed = board.placeShip(ship)

                    if (!placed) {
                        println("${AnsiColors.ANSI_RED}Невозможно разместить корабль здесь. " +
                                "Попробуйте снова.${AnsiColors.ANSI_RESET}")
                    }

                    println("${AnsiColors.ANSI_GREEN}Ваша доска:${AnsiColors.ANSI_RESET}")
                    displayBoard(board)
                } catch (e: IllegalArgumentException) {
                    println("${AnsiColors.ANSI_RED}${e.message}${AnsiColors.ANSI_RESET}")
                } catch (e: Exception) {
                    println("${AnsiColors.ANSI_RED}Произошла неожиданная ошибка. " +
                            "Пожалуйста, попробуйте снова.${AnsiColors.ANSI_RESET}")
                }
            }
        }
    }

    // ToDo: Заменить дубликат; вынести авто в отдельный модуль utils
    fun autoPlaceShips() {
//        displayBoard(board)

        for (size in Constants.SHIP_SIZES) {
            var placed = false
            while (!placed) {
                val x = (0..<board.size).random()
                val y = (0..<board.size).random()
                val orientation = if ((0..1).random() == 0)
                    Orientation.HORIZONTAL
                else
                    Orientation.VERTICAL

                val coordinates = generateCoordinates(size, Coordinate(x, y), orientation)
                val ship = Ship(size, coordinates, orientation)

                placed = board.placeShip(ship)

                if (placed) {
                    println("${AnsiColors.ANSI_GREEN}Корабль размером $size размещен автоматически.${AnsiColors.ANSI_RESET}")
                }
            }
        }

        println("${AnsiColors.ANSI_GREEN}Ваша доска после автозаполнения:${AnsiColors.ANSI_RESET}")
        displayBoard(board)
    }

    // ToDo: перенести в utils или в другой класс, но точно не должно быть в классе human
    private fun generateCoordinates(size: Int, start: Coordinate, orientation: Orientation): List<Coordinate> {
        return (0..<size).map {
            if (orientation == Orientation.HORIZONTAL) {
                Coordinate(start.x, start.y + it)
            } else {
                Coordinate(start.x + it, start.y)
            }
        }
    }

    // ToDo: Перенести в board
    private fun displayBoard(board: Board, showShips: Boolean = true) {
        val display = board.getBoardDisplay(showShips)
        for (line in display) {
            println(line)
        }
    }
}
