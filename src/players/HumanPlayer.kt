package players

import models.Board
import utils.AnsiColors
import models.Coordinate
import models.enums.CellStatus
import models.enums.Orientation
import models.Ship
import config.Constants
import utils.ShipPlacementUtils

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
        board.displayBoard()

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
                    val coordinates = Coordinate.generateShipCoordinates(size, Coordinate(x, y), orientation)

                    val ship = Ship(size, coordinates, orientation)
                    placed = board.placeShip(ship)

                    if (!placed) {
                        println("${AnsiColors.ANSI_RED}Невозможно разместить корабль здесь. " +
                                "Попробуйте снова.${AnsiColors.ANSI_RESET}")
                    }

                    println("${AnsiColors.ANSI_GREEN}Ваша доска:${AnsiColors.ANSI_RESET}")
                    board.displayBoard()
                } catch (e: IllegalArgumentException) {
                    println("${AnsiColors.ANSI_RED}${e.message}${AnsiColors.ANSI_RESET}")
                } catch (e: Exception) {
                    println("${AnsiColors.ANSI_RED}Произошла неожиданная ошибка. " +
                            "Пожалуйста, попробуйте снова.${AnsiColors.ANSI_RESET}")
                }
            }
        }
    }

    // Авторасстановка кораблей
    fun autoPlaceShips() {
        ShipPlacementUtils.autoPlaceShips(board)

        println("${AnsiColors.ANSI_GREEN}Ваша доска после автозаполнения:${AnsiColors.ANSI_RESET}")

        board.displayBoard()
    }
}
