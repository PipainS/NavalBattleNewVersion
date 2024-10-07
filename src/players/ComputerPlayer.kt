package players

import models.Board
import config.Constants
import models.Coordinate
import models.enums.CellStatus
import utils.ShipPlacementUtils

class ComputerPlayer(board: Board) : Player(board) {
    private var lastHit: Coordinate? = null
    private val hitQueue: MutableList<Coordinate> = mutableListOf()
    private var moveCounter: Int = 0

    override fun makeMove(opponentBoard: Board): Coordinate {
        if (hitQueue.isEmpty() && lastHit != null) {
            addSurroundingCoordinatesToQueue(opponentBoard, lastHit!!)
        }

        // Счетчик ударов
        moveCounter++
        var coordinate: Coordinate

        // Обязательное попадение
        if (hitQueue.isEmpty() && moveCounter % Constants.LUCKY_MOVE == 0) {
            coordinate = findGuaranteedHit(opponentBoard)
        } else {
            do {
                coordinate = if (hitQueue.isNotEmpty()) {
                    hitQueue.removeAt(0)
                } else {
                    val x = (0..<board.size).random()
                    val y = (0..<board.size).random()
                    Coordinate(x, y)
                }
            } while (opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.MISS ||
                opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.HIT
            )
        }

        if (opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.SHIP) {
            lastHit = coordinate
            addSurroundingCoordinatesToQueue(opponentBoard, coordinate)
        } else {
            lastHit = null
        }

        return coordinate
    }

    override fun placeShips() {
        ShipPlacementUtils.autoPlaceShips(board)
    }

    // Добавление соседних клеток в очередь, если попал в корабль, чтобы не лупить по рандомным местам
    private fun addSurroundingCoordinatesToQueue(opponentBoard: Board, coordinate: Coordinate) {
        for (direction in Constants.DIRECTIONS) {

            val newCoordinate = Coordinate(coordinate.x + direction.x, coordinate.y + direction.y)

            if (Coordinate.isValidCoordinate(newCoordinate, opponentBoard) &&
                opponentBoard.grid[newCoordinate.x][newCoordinate.y].status != CellStatus.MISS &&
                opponentBoard.grid[newCoordinate.x][newCoordinate.y].status != CellStatus.HIT)
            {
                hitQueue.add(newCoordinate)
            }
        }
    }

    // Поиск клетки, в которой находится корабль, для удачного удара
    private fun findGuaranteedHit(opponentBoard: Board): Coordinate {
        for (x in 0..<board.size) {
            for (y in 0..<board.size) {
                val coordinate = Coordinate(x, y)
                if (opponentBoard.grid[coordinate.x][coordinate.y].status == CellStatus.SHIP) {
                    return coordinate
                }
            }
        }

        // В случае, если нет доступных кораблей, вернем случайные координаты (маловероятный случай)
        val x = (0..<board.size).random()
        val y = (0..<board.size).random()
        return Coordinate(x, y)
    }
}
