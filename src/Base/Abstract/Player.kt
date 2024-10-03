package Base.Abstract

import Base.Board.Board
import Base.Coordinate

abstract class Player(val board: Board) {
    abstract fun makeMove(opponentBoard: Board): Coordinate
    abstract fun placeShips()
}