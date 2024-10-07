package base.abstract

import base.Board.Board
import base.Coordinate

abstract class Player(val board: Board) {
    abstract fun makeMove(opponentBoard: Board): Coordinate
    abstract fun placeShips()
}