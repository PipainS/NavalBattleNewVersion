package base

import base.Board.Board
import base.Enum.Orientation

object ShipPlacementUtils {
    fun autoPlaceShips(board: Board) {
        for (size in Constants.SHIP_SIZES) {
            var placed = false
            while (!placed) {
                val x = (0..<board.size).random()
                val y = (0..<board.size).random()

                val orientation = if ((0..1).random() == 0)
                    Orientation.HORIZONTAL
                else
                    Orientation.VERTICAL

                val coordinates = Coordinate.generateShipCoordinates(size, Coordinate(x, y), orientation)
                val ship = Ship(size, coordinates, orientation)

                placed = board.placeShip(ship)
            }
        }
    }
}