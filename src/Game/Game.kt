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
    private var isGodMode = false  // Добавляем флаг для режима "глаза бога"

    fun start() {
        println("${AnsiColors.ANSI_GREEN}Добро пожаловать в Морской бой!${AnsiColors.ANSI_RESET}\n")

        println("${AnsiColors.ANSI_YELLOW}Хотите ли вы, чтобы ваши корабли были " +
                "автоматически размещены? (да/нет): ${AnsiColors.ANSI_RESET}")

        val humanAutoPlaceInput = readValidatedInput(checkForGodMode = true)
        if (humanAutoPlaceInput == "да" || humanAutoPlaceInput == "yes") {
            humanPlayer.autoPlaceShips() // Автоматически размещение кораблей
        } else {
            humanPlayer.placeShips() // ручное заполнение кораблей
        }

        // размещение кораблей для компьютера
        computerPlayer.placeShips()

        println("\n${AnsiColors.ANSI_CYAN}Игра начинается ${AnsiColors.ANSI_RESET}")
        displayBoards(humanPlayer.board, computerPlayer.board)
        isGameOver = false
        while (!isGameOver) {
            try {
                playTurn(humanPlayer, computerPlayer)

                if (isGameOver) break

                playTurn(computerPlayer, humanPlayer)
                displayBoards(humanPlayer.board, computerPlayer.board)
            } catch (e: Exception) {
                println("${AnsiColors.ANSI_RED}Во время игры произошла ошибка. " +
                        "Пожалуйста, попробуйте снова.${AnsiColors.ANSI_RESET}")
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
        val opponentDisplay = opponentBoard.getBoardDisplay(showShips = isGodMode)

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

    private fun readValidatedInput(prompt: String = "", checkForGodMode: Boolean = false): String {
        while (true) {
            try {
                if (prompt.isNotEmpty()) {
                    print(prompt)
                }
                val input = readlnOrNull()?.trim()?.lowercase() ?: throw IllegalArgumentException("Ввод не может быть пустым")

                if (checkForGodMode && input.endsWith("godmode")) {
                    isGodMode = true
                    println("${AnsiColors.ANSI_RED}Читы активированы!${AnsiColors.ANSI_RESET}")
                    return input.removeSuffix("godmode").trim()
                }

                if (input != "да" && input != "нет" && input != "yes" && input != "no") {
                    throw IllegalArgumentException("Пожалуйста, введите 'да' или 'нет' (или 'yes'/'no')")
                }
                return input
            } catch (e: IllegalArgumentException) {
                println("${AnsiColors.ANSI_RED}${e.message}${AnsiColors.ANSI_RESET}")
            }
        }
    }
}
