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

        humanPlayer.placeShips()
        computerPlayer.placeShips()

        isGameOver = false
        while (!isGameOver) {
            playTurn(humanPlayer, computerPlayer)
            if (isGameOver) break
            playTurn(computerPlayer, humanPlayer)
        }
        println("Game Over!")
    }

    private fun playTurn(currentPlayer: Player, opponentPlayer: Player) {
        println("${currentPlayer::class.simpleName}'s turn:")
        val move = currentPlayer.makeMove(opponentPlayer.board)
        val result = opponentPlayer.board.receiveShot(move)
        println("${currentPlayer::class.simpleName} shoots at (${move.x}, ${move.y}) and ${if (result == CellStatus.HIT) "hits" else "misses"}!")
        checkGameOver(opponentPlayer)

        println("Current state:")
        println("${currentPlayer::class.simpleName}'s board:")
        currentPlayer.board.displayBoard(showShips = true)
        println("Opponent's board:")
        opponentPlayer.board.displayBoard(showShips = false)
    }

    private fun checkGameOver(player: Player) {
        isGameOver = player.board.grid.flatten().none { it.status == CellStatus.SHIP }
        if (isGameOver) {
            println("${player::class.simpleName} loses! All ships have been sunk.")
            println()
        }

    }
}
