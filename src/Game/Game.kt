package Game

import Base.Abstract.Player
import Base.Board.Board
import Base.Enum.CellStatus
import Base.Abstract.HumanPlayer
import Base.Abstract.ComputerPlayer

class Game {
    private val humanPlayer = HumanPlayer(Board())
    private val computerPlayer = ComputerPlayer(Board())
    private var isGameOver = false

    fun start() {
        println("Welcome to Battleship!")

        println("Place your ships:")
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
        println("${currentPlayer::class.simpleName}'s turn:")

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

        println("Your board:".padEnd(25) + "Opponent's board:")
        for (i in playerDisplay.indices) {
            println(playerDisplay[i].padEnd(25) + opponentDisplay[i])
        }
    }
}
