package core

import players.base.Player
import models.Board
import models.Board.Companion.displayBoards
import models.enums.CellStatus
import players.HumanPlayer
import players.ComputerPlayer
import utils.AnsiColors
import utils.DisplayUtils
import utils.Utils

class Game {
    private val humanPlayer = HumanPlayer(Board())
    private val computerPlayer = ComputerPlayer(Board())
    private var isGameOver = false
    private var isGodMode = false  // флаг для режима "god mode"

    fun start() {
        DisplayUtils.displayWelcomeMessage()

        // Стартовая навигация
        while (true) {
            DisplayUtils.displayGameOptions()
            val choice = readlnOrNull()
            when (choice) {
                "1" -> break
                "2" -> DisplayUtils.displayRules()
                else -> println("${AnsiColors.ANSI_RED}Неверный выбор. " +
                        "Пожалуйста, выберите 1 или 2.${AnsiColors.ANSI_RESET}")
            }
        }

        // Старт игры
        println("${AnsiColors.ANSI_YELLOW}Хотите ли вы, чтобы ваши корабли были " +
                "автоматически размещены? (да/нет): ${AnsiColors.ANSI_RESET}")

        val (humanAutoPlaceInput, godMode) = Utils.readValidatedInput(checkForGodMode = true)
        isGodMode = godMode

        if (humanAutoPlaceInput == "да" || humanAutoPlaceInput == "yes") {
            humanPlayer.autoPlaceShips() // Автоматически размещение кораблей
        } else {
            humanPlayer.placeShips() // ручное заполнение кораблей
        }

        // размещение кораблей для компьютера
        computerPlayer.placeShips()

        println("\n${AnsiColors.ANSI_CYAN}Игра начинается ${AnsiColors.ANSI_RESET}")
        displayBoards(humanPlayer.board, computerPlayer.board, isGodMode)

        isGameOver = false
        while (!isGameOver) {
            try {
                playTurn(currentPlayer = humanPlayer, opponentPlayer = computerPlayer)

                if (isGameOver) break

                playTurn(currentPlayer = computerPlayer, opponentPlayer = humanPlayer)

                displayBoards(humanPlayer.board, computerPlayer.board, isGodMode)

            } catch (e: Exception) {
                println("${AnsiColors.ANSI_RED}Во время игры произошла ошибка. " +
                        "Пожалуйста, попробуйте снова.${AnsiColors.ANSI_RESET}")
            }
        }
        // Финальный результат игры, чтобы все видели результат
        displayBoards(humanPlayer.board, computerPlayer.board, true)
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
        isGameOver = player.board.grid.flatten().none { it.status == CellStatus.SHIP } //none проверяет, не выполняется ли условие для всех элементов списк
        if (isGameOver) {
            println("${player::class.simpleName} проигрывает! Все корабли уничтожены.")
        }
    }
}
