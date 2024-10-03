package Base.Board

import Base.Cell
import Base.Coordinate
import Base.Enum.CellStatus
import Base.Ship

class Board(val size: Int = 10) {
    val grid: Array<Array<Cell>> = Array(size) { Array(size) { Cell() } }
    val ships: MutableList<Ship> = mutableListOf()

    fun canPlaceShip(ship: Ship): Boolean {
        for (coordinate in ship.coordinates) {
            if (coordinate.x !in 0 until size || coordinate.y !in 0 until size) {
                return false // Корабль выходит за пределы доски
            }
            if (grid[coordinate.x][coordinate.y].status != CellStatus.EMPTY) {
                return false // Место занято
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

    fun displayBoard(showShips: Boolean = false) {
        println("  " + (0 until size).joinToString(" ") { it.toString() })
        for (i in 0 until size) {
            print("$i ")
            for (j in 0 until size) {
                val cell = grid[i][j]
                val symbol = if (showShips || cell.status != CellStatus.SHIP) cell.getSymbol() else '.'
                print("$symbol ")
            }
            println()
        }
    }
}

