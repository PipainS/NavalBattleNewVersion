package Base.Board

import Base.Cell
import Base.Coordinate
import Base.Enum.CellStatus
import Base.Ship

class Board(val size: Int = 10) {
    val grid: Array<Array<Cell>> = Array(size) { Array(size) { Cell() } }
    val ships: MutableList<Ship> = mutableListOf()

    fun canPlaceShip(ship: Ship): Boolean {
        val directions = listOf(
            Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
            Pair(0, -1), Pair(0, 1),
            Pair(1, -1), Pair(1, 0), Pair(1, 1)
        )

        for (coordinate in ship.coordinates) {
            if (coordinate.x !in 0 until size || coordinate.y !in 0 until size) {
                return false // Ship is out of board bounds
            }
            if (grid[coordinate.x][coordinate.y].status != CellStatus.EMPTY) {
                return false // Cell is already occupied
            }

            // Check adjacent cells
            for (direction in directions) {
                val adjX = coordinate.x + direction.first
                val adjY = coordinate.y + direction.second
                if (adjX in 0 until size && adjY in 0 until size) {
                    if (grid[adjX][adjY].status == CellStatus.SHIP) {
                        return false // Adjacent cell contains a ship
                    }
                }
            }
        }
        return true
    }


    fun placeShip(ship: Ship): Boolean {
        if (!canPlaceShip(ship)) return false

        for (coordinate in ship.coordinates) {
            grid[coordinate.x][coordinate.y].status = CellStatus.SHIP
        }
        ships.add(ship)
        return true
    }

    fun receiveShot(coordinate: Coordinate): CellStatus {
        val cell = grid[coordinate.x][coordinate.y]
        return when (cell.status) {
            CellStatus.EMPTY -> {
                cell.status = CellStatus.MISS
                CellStatus.MISS
            }
            CellStatus.SHIP -> {
                cell.status = CellStatus.HIT
                CellStatus.HIT
            }
            else -> cell.status // Если по этой ячейке уже стреляли
        }
    }

    fun getBoardDisplay(showShips: Boolean = false): List<String> {
        val result = mutableListOf<String>()
        val header = ('A'..'J').joinToString(" ") { it.toString() }
        result.add("  $header") // Заголовок столбцов
        for (i in 0 until size) {
            val row = StringBuilder()
            row.append("$i ")
            for (j in 0 until size) {
                val cell = grid[i][j]
                val symbol = if (showShips || cell.status != CellStatus.SHIP) cell.getSymbol() else '.'
                row.append("$symbol ")
            }
            result.add(row.toString())
        }
        return result
    }
}