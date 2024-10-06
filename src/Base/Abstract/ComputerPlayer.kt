package Base.Abstract

import Base.Board.Board
import Base.Coordinate
import Base.Enum.CellStatus
import Base.Enum.Orientation
import Base.Ship
import Base.Constants

class ComputerPlayer(board: Board) : Player(board) {
    private var lastHit: Coordinate? = null
    private val hitQueue: MutableList<Coordinate> = mutableListOf()

    override fun makeMove(opponentBoard: Board): Coordinate {
        if (hitQueue.isEmpty() && lastHit != null) {
            addSurroundingCoordinatesToQueue(opponentBoard, lastHit!!)
        }

        var coordinate: Coordinate
        do {
            coordinate = if (hitQueue.isNotEmpty()) {
                hitQueue.removeAt(0)
            } else {
                val x = (0..<board.size).random()
                val y = (0..<board.size).random()
                Coordinate(x, y)
            }
        } while (opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.MISS ||
            opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.HIT)

        if (opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.SHIP) {
            lastHit = coordinate
            addSurroundingCoordinatesToQueue(opponentBoard, coordinate)
        } else {
            lastHit = null
        }

        return coordinate
    }

    override fun placeShips() {
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
            }
        }
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

    private fun addSurroundingCoordinatesToQueue(opponentBoard: Board, coordinate: Coordinate) {
        val directions = listOf(
            Coordinate(-1, 0), // Вверх
            Coordinate(1, 0),  // Вниз
            Coordinate(0, -1), // Влево
            Coordinate(0, 1)   // Вправо
        )

        for (direction in directions) {
            val newCoordinate = Coordinate(coordinate.x + direction.x, coordinate.y + direction.y)
            if (isValidCoordinate(newCoordinate, opponentBoard) &&
                opponentBoard.grid[newCoordinate.x][newCoordinate.y].status != CellStatus.MISS &&
                opponentBoard.grid[newCoordinate.x][newCoordinate.y].status != CellStatus.HIT) {
                hitQueue.add(newCoordinate)
            }
        }
    }

    private fun isValidCoordinate(coordinate: Coordinate, board: Board): Boolean {
        return coordinate.x in 0..<board.size && coordinate.y in 0..<board.size
    }
}
