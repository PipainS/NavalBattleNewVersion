package Base.Abstract

import Base.Board.Board
import Base.Coordinate
import Base.Enum.Orientation
import Base.Ship

class ComputerPlayer(board: Board) : Player(board) {
    override fun makeMove(opponentBoard: Board): Coordinate {
        val x = (0 until board.size).random()
        val y = (0 until board.size).random()
        return Coordinate(x, y)
    }

    override fun placeShips() {
        val shipSizes = listOf(5, 4, 3, 3, 2)
        for (size in shipSizes) {
            var placed = false
            while (!placed) {
                val x = (0 until board.size).random()
                val y = (0 until board.size).random()
                val orientation = if ((0..1).random() == 0) Orientation.HORIZONTAL else Orientation.VERTICAL
                val coordinates = generateCoordinates(size, Coordinate(x, y), orientation)
                val ship = Ship(size, coordinates, orientation)
                placed = board.placeShip(ship)
            }
        }
        // Вывод доски компьютера для отладки (можно закомментировать в финальной версии)
//        println("Computer's board after placing ships (for debugging):")
//        displayBoard(board, true)
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

    private fun displayBoard(board: Board, showShips: Boolean) {
        val display = board.getBoardDisplay(showShips)
        for (line in display) {
            println(line)
        }
    }
}