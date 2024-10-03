package Base.Abstract

import Base.Board.Board
import Base.Coordinate
import Base.Enum.Orientation
import Base.Ship

class HumanPlayer(board: Board) : Player(board) {
    override fun makeMove(opponentBoard: Board): Coordinate {
        println("Your board:")
        board.displayBoard(showShips = true)
        println("Opponent's board:")
        opponentBoard.displayBoard(showShips = false)

        println("Enter coordinates (x y): ")
        val input = readLine()
        val (x, y) = input!!.split(" ").map { it.toInt() }
        return Coordinate(x, y)
    }

    override fun placeShips() {
        val shipSizes = listOf(5, 4, 3, 3, 2) // Пример размеров кораблей
        for (size in shipSizes) {
            var placed = false
            while (!placed) {
                println("Enter coordinates and orientation (H/V) for ship of size $size (x y H/V): ")
                val input = readLine()
                val (x, y, orientationInput) = input!!.split(" ")
                val orientation = if (orientationInput.uppercase() == "H") Orientation.HORIZONTAL else Orientation.VERTICAL
                val coordinates = generateCoordinates(size, Coordinate(x.toInt(), y.toInt()), orientation)
                val ship = Ship(size, coordinates, orientation)
                placed = board.placeShip(ship)
                if (!placed) {
                    println("Cannot place ship here. Try again.")
                }
                println("Your board:")
                board.displayBoard(showShips = true)
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
}

