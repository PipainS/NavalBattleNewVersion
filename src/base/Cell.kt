package base

import base.Color.AnsiColors
import base.Enum.CellStatus

data class Cell(var status: CellStatus = CellStatus.EMPTY) {
    fun getSymbol(): Char {
        return when (status) {
            CellStatus.EMPTY -> '.'
            CellStatus.SHIP -> 'S'
            CellStatus.HIT -> 'X'
            CellStatus.MISS -> 'O'
        }
    }

    fun getColoredSymbol(): String {
        return when (status) {
            CellStatus.EMPTY -> "${AnsiColors.ANSI_WHITE}.${AnsiColors.ANSI_RESET}"
            CellStatus.SHIP -> "${AnsiColors.ANSI_BLUE}S${AnsiColors.ANSI_RESET}"
            CellStatus.HIT -> "${AnsiColors.ANSI_RED}X${AnsiColors.ANSI_RESET}"
            CellStatus.MISS -> "${AnsiColors.ANSI_CYAN}O${AnsiColors.ANSI_RESET}"
        }
    }
}
