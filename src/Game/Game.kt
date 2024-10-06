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
        println("${AnsiColors.ANSI_GREEN}Добро пожаловать в Морской бой!${AnsiColors.ANSI_RESET}")

        println("${AnsiColors.ANSI_YELLOW}Разместите свои корабли:${AnsiColors.ANSI_RESET}")
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
                println("Во время игры произошла ошибка. Пожалуйста, попробуйте снова.")
            }
        }
        println("Игра окончена!")
    }

    private fun playTurn(currentPlayer: Player, opponentPlayer: Player) {

        val move = currentPlayer.makeMove(opponentPlayer.board)
        val result = opponentPlayer.board.receiveShot(move)

        println("${currentPlayer::class.simpleName} стреляет в (${move.x}, ${move.y}) " +
                "и ${if (result == CellStatus.HIT) "попадает" else "промахивается"}!")

        checkGameOver(opponentPlayer)
    }

    private fun checkGameOver(player: Player) {
        isGameOver = player.board.grid.flatten().none { it.status == CellStatus.SHIP }
        if (isGameOver) {
            println("${player::class.simpleName} проигрывает! Все корабли уничтожены.")
        }
    }

    private fun displayBoards(playerBoard: Board, opponentBoard: Board) {
        val playerDisplay = playerBoard.getBoardDisplay(showShips = true)
        val opponentDisplay = opponentBoard.getBoardDisplay(showShips = false)

        println("${AnsiColors.ANSI_GREEN}Ваша доска:${AnsiColors.ANSI_RESET}".padEndAnsi(25) +
                "${AnsiColors.ANSI_RED}Доска противника:${AnsiColors.ANSI_RESET}")
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
