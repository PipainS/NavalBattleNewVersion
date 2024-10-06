package Base.Abstract

import Base.Board.Board
import Base.Coordinate
import Base.Enum.CellStatus
import Base.Enum.Orientation
import Base.Ship
import Base.Constants

class HumanPlayer(board: Board) : Player(board) {

    override fun makeMove(opponentBoard: Board): Coordinate {
        while (true) {
            try {
                println("Enter coordinates (e.g., A 0): ")

                val input = readlnOrNull() ?: throw IllegalArgumentException("Input cannot be null")
                val (letter, xStr) = input.split(" ")

                val y = Constants.LETTER_TO_INDEX[letter.uppercase()] ?: throw IllegalArgumentException("Invalid coordinate")
                val x = xStr.toIntOrNull() ?: throw IllegalArgumentException("Invalid coordinate")

                // Проверка, стреляли ли уже в эту клетку
                if (opponentBoard.grid[x][y].status == CellStatus.MISS ||
                    opponentBoard.grid[x][y].status == CellStatus.HIT) {
                    println("You have already shot here! Try again.")
                    continue
                }

                return Coordinate(x, y)
            } catch (e: Exception) {
                println("Invalid input. Please try again.")
            }
        }
    }

    override fun placeShips() {
        displayBoard(board)

        for (size in Constants.SHIP_SIZES) {
            var placed = false
            while (!placed) {
                try {
                    println("Enter coordinates and orientation (H/V) for ship of size $size (e.g., A 0 H): ")

                    val input = readlnOrNull() ?: throw IllegalArgumentException("Input cannot be null")
                    val (letter, xStr, orientationInput) = input.split(" ")

                    val y = Constants.LETTER_TO_INDEX[letter.uppercase()] ?: throw IllegalArgumentException("Invalid coordinate")
                    val x = xStr.toIntOrNull() ?: throw IllegalArgumentException("Invalid coordinate")

                    val orientation = if (orientationInput.uppercase() == "H") Orientation.HORIZONTAL else Orientation.VERTICAL
                    val coordinates = generateCoordinates(size, Coordinate(x, y), orientation)

                    val ship = Ship(size, coordinates, orientation)
                    placed = board.placeShip(ship)

                    if (!placed) {
                        println("Cannot place ship here. Try again.")
                    }

                    println("Your board:")
                    displayBoard(board)
                } catch (e: Exception) {
                    println("Invalid input. Please try again.")
                }
            }
        }
    }

    private fun generateCoordinates(size: Int, start: Coordinate, orientation: Orientation): List<Coordinate> {
        return (0 until size).map {
            if (orientation == Orientation.HORIZONTAL) {
                Coordinate(start.x, start.y + it)
            } else {
                Coordinate(start.x + it, start.y)
            }
        }
    }

    private fun displayBoard(board: Board, showShips: Boolean = true) {
        val display = board.getBoardDisplay(showShips)
        for (line in display) {
            println(line)
        }
    }
}
