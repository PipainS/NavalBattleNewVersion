package models

import models.enums.Orientation

data class Coordinate(val x: Int, val y: Int) {

    companion object {
        fun generateShipCoordinates(size: Int, start: Coordinate, orientation: Orientation): List<Coordinate> {
            return (0..<size).map {
                if (orientation == Orientation.HORIZONTAL) {
                    Coordinate(start.x, start.y + it)
                } else {
                    Coordinate(start.x + it, start.y)
                }
            }
        }

        fun isValidCoordinate(coordinate: Coordinate, board: Board): Boolean {
            return coordinate.x in 0..<board.size && coordinate.y in 0..<board.size
        }
    }
}
