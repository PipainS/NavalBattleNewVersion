package Game

import Base.Abstract.Player
import Base.Board.Board
import Base.Enum.CellStatus
import Base.Abstract.HumanPlayer
import Base.Abstract.ComputerPlayer
import Base.Color.AnsiColors

class Game {
    private val humanPlayer = HumanPlayer(Board())
    private val computerPlayer = ComputerPlayer(Board())
    private var isGameOver = false

    fun start() {
        println("${AnsiColors.ANSI_GREEN}Welcome to Battleship!${AnsiColors.ANSI_RESET}")

        println("${AnsiColors.ANSI_YELLOW}Place your ships:${AnsiColors.ANSI_RESET}")
        humanPlayer.placeShips()
        computerPlayer.placeShips()

        displayBoards(humanPlayer.board, computerPlayer.board)
        isGameOver = false
        while (!isGameOver) {
            try {
                playTurn(humanPlayer, computerPlayer)
                if (isGameOver) break
                playTurn(computerPlayer, humanPlayer)
                displayBoards(humanPlayer.board, computerPlayer.board)
            } catch (e: Exception) {
                println("An error occurred during the game. Please try again.")
            }
        }
        println("Game Over!")
    }

    private fun playTurn(currentPlayer: Player, opponentPlayer: Player) {

        val move = currentPlayer.makeMove(opponentPlayer.board)
        val result = opponentPlayer.board.receiveShot(move)

        println("${currentPlayer::class.simpleName} shoots at (${move.x}, ${move.y}) and ${if (result == CellStatus.HIT) "hits" else "misses"}!")

        checkGameOver(opponentPlayer)
    }

    private fun checkGameOver(player: Player) {
        isGameOver = player.board.grid.flatten().none { it.status == CellStatus.SHIP }
        if (isGameOver) {
            println("${player::class.simpleName} loses! All ships have been sunk.")
        }
    }

    private fun displayBoards(playerBoard: Board, opponentBoard: Board) {
        val playerDisplay = playerBoard.getBoardDisplay(showShips = true)
        val opponentDisplay = opponentBoard.getBoardDisplay(showShips = false)

        println("${AnsiColors.ANSI_GREEN}Your board:${AnsiColors.ANSI_RESET}".padEndAnsi(25) +
                "${AnsiColors.ANSI_RED}Opponent's board:${AnsiColors.ANSI_RESET}")
        for (i in playerDisplay.indices) {
            println(playerDisplay[i].padEndAnsi(25) + opponentDisplay[i])
        }
    }

    private fun String.padEndAnsi(totalLength: Int, padChar: Char = ' '): String {
        val strippedLength = this.replace(Regex("\u001B\\[[;\\d]*m"), "").length
        val padLength = totalLength - strippedLength
        return this + padChar.toString().repeat(padLength.coerceAtLeast(0))
    }
}
