package models

import models.enums.Orientation

data class Ship(val size: Int,
                val coordinates: List<Coordinate>,
                val orientation: Orientation,
                var isSunk: Boolean = false)