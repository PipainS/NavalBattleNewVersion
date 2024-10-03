package Base

import Base.Enum.CellStatus

data class Cell(var status: CellStatus = CellStatus.EMPTY) {
    fun getSymbol(): Char {
        return when (status) {
            CellStatus.EMPTY -> '.'
            CellStatus.SHIP -> 'S'
            CellStatus.HIT -> 'X'
            CellStatus.MISS -> 'O'
        }
    }
}
