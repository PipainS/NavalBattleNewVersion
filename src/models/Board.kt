package models

import models.enums.CellStatus
import config.Constants
import utils.AnsiColors
import utils.Utils.padEndAnsi

class Board(val size: Int = 10) {
    val grid: Array<Array<Cell>> = Array(size) { Array(size) { Cell() } }
    private val ships: MutableList<Ship> = mutableListOf()

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
                if (isShipSunk(coordinate)) {
                    markAdjacentCells(getShipCoordinates(coordinate))
                    println("Корабль уничтожен!")
                    CellStatus.HIT
                } else {
                    CellStatus.HIT
                }
            }
            else -> cell.status // Если по этой ячейке уже стреляли
        }
    }

    private fun canPlaceShip(ship: Ship): Boolean {
        for (coordinate in ship.coordinates) {
            if (coordinate.x !in 0..<size || coordinate.y !in 0..<size) {
                return false // Ship is out of board bounds
            }
            if (grid[coordinate.x][coordinate.y].status != CellStatus.EMPTY) {
                return false // Cell is already occupied
            }

            // Check adjacent cells
            for (direction in Constants.ADJACENT_DIRECTIONS) {
                val adjX = coordinate.x + direction.first
                val adjY = coordinate.y + direction.second
                if (adjX in 0..<size && adjY in 0..<size) {
                    if (grid[adjX][adjY].status == CellStatus.SHIP) {
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun getShipCoordinates(coordinate: Coordinate): List<Coordinate> {
        for (ship in ships) {
            if (coordinate in ship.coordinates) {
                return ship.coordinates
            }
        }
        return emptyList()
    }

    fun getBoardDisplay(showShips: Boolean = false): List<String> {
        val result = mutableListOf<String>()

        result.add("  ${Constants.BOARD_HEADER}") // Заголовок столбцов

        for (i in 0..<size) {
            val row = StringBuilder()
            row.append("$i ")
            for (j in 0..<size) {
                val cell = grid[i][j]
                val symbol = if (showShips || cell.status != CellStatus.SHIP) cell.getColoredSymbol() else '.'
                row.append("$symbol ")
            }
            result.add(row.toString())
        }
        return result
    }

    fun displayBoard(showShips: Boolean = true) {
        val display = getBoardDisplay(showShips)
        for (line in display) {
            println(line)
        }
    }

    companion object {
        fun displayBoards(playerBoard: Board, opponentBoard: Board, isGodMode: Boolean = false) {
            val playerDisplay = playerBoard.getBoardDisplay(showShips = true)
            val opponentDisplay = opponentBoard.getBoardDisplay(showShips = isGodMode)

            println("${AnsiColors.ANSI_GREEN}Ваша доска:${AnsiColors.ANSI_RESET}".padEndAnsi(25) +
                    "${AnsiColors.ANSI_RED}Доска противника:${AnsiColors.ANSI_RESET}")

            for (i in playerDisplay.indices) {
                println(playerDisplay[i].padEndAnsi(25) + opponentDisplay[i])
            }
        }
    }

    private fun markAdjacentCells(coordinates: List<Coordinate>) {
        for (coordinate in coordinates) {
            for (direction in Constants.ADJACENT_DIRECTIONS) {
                val adjX = coordinate.x + direction.first
                val adjY = coordinate.y + direction.second
                if (adjX in 0..<size && adjY in 0..<size) {
                    val adjacentCell = grid[adjX][adjY]
                    if (adjacentCell.status == CellStatus.EMPTY) {
                        adjacentCell.status = CellStatus.MISS // Закрашиваем соседнюю клетку
                    }
                }
            }
        }
    }

    private fun isShipSunk(coordinate: Coordinate): Boolean {
        for (ship in ships) {
            if (coordinate in ship.coordinates) {
                return ship.coordinates.all { grid[it.x][it.y].status == CellStatus.HIT }
            }
        }
        return false
    }
}
