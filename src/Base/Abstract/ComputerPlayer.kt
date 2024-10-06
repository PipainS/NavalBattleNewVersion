package Base.Abstract

import Base.Board.Board
import Base.Coordinate
import Base.Enum.CellStatus
import Base.Enum.Orientation
import Base.Ship

class ComputerPlayer(board: Board) : Player(board) {
    // ToDo: Сделать ходы более реальными
    override fun makeMove(opponentBoard: Board): Coordinate {
        var coordinate: Coordinate
        do {
            val x = (0..<board.size).random()
            val y = (0..<board.size).random()
            coordinate = Coordinate(x, y)
        } while (opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.MISS ||
                 opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.HIT)

        return coordinate
    }


    override fun placeShips() {
        val shipSizes = listOf(5, 4, 3, 3, 2)
        for (size in shipSizes) {
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
            }
        }

        // Вывод доски компьютера для отладки (можно закомментировать в финальной версии)
//        println("Доска компьютера после расстановки кораблей (для отладки):")
//        displayBoard(board)
    }

    private fun generateCoordinates(size: Int, start: Coordinate, orientation: Orientation): List<Coordinate> {
        return (0..<size).map {
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