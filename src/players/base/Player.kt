package players.base

import models.Board
import models.Coordinate

abstract class Player(val board: Board) {
    abstract fun makeMove(opponentBoard: Board): Coordinate
    abstract fun placeShips()
}