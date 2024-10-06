package Base.Abstract

import Base.Board.Board
import Base.Coordinate
import Base.Enum.CellStatus
import Base.Enum.Orientation
import Base.Ship

class HumanPlayer(board: Board) : Player(board) {
    private val letterToIndex = ('A'..'Z').mapIndexed { index, c -> c.toString() to index }.toMap()
    // ToDo: перенести в отдельный класс с константами

    override fun makeMove(opponentBoard: Board): Coordinate {
        while (true) {
            println("Enter coordinates (e.g., A 0): ")

            val input = readLine()
            val (letter, xStr) = input!!.split(" ")
            val y = letterToIndex[letter.uppercase()] ?: throw IllegalArgumentException("Invalid coordinate")
            val x = xStr.toInt()

            // Проверка, стреляли ли уже в эту клетку
            // Проверка, стреляли ли уже в эту клетку
            if (opponentBoard.grid[x][y].status == CellStatus.MISS || opponentBoard.grid[x][y].status == CellStatus.HIT) {
                println("You have already shot here! Try again.")
                continue
            }


            return Coordinate(x, y)
        }
    }


    override fun placeShips() {
        println("Your board before placing ships:")
        displayBoard(board, true)

        val shipSizes = listOf(5, 4, 3, 3, 2) // Example ship sizes ToDo: Перенести в отдельный класс с константами

        for (size in shipSizes) {
            var placed = false
            while (!placed) {
                println("Enter coordinates and orientation (H/V) for ship of size $size (e.g., A 0 H): ")

                val input = readLine()
                val (letter, x, orientationInput) = input!!.split(" ")
                val y = letterToIndex[letter.uppercase()] ?: throw IllegalArgumentException("Invalid coordinate")

                val orientation = if (orientationInput.uppercase() == "H") Orientation.HORIZONTAL else Orientation.VERTICAL
                val coordinates = generateCoordinates(size, Coordinate(x.toInt(), y), orientation)

                val ship = Ship(size, coordinates, orientation)
                placed = board.placeShip(ship)

                if (!placed) {
                    println("Cannot place ship here. Try again.")
                }

                println("Your board:")
                displayBoard(board, true)
            }
        }
    }

    private fun generateCoordinates(size: Int, start: Coordinate, orientation: Orientation): List<Coordinate> {
        return (0 until size).map {
            if (orientation == Orientation.HORIZONTAL) {
                Coordinate(start.x, start.y + it)
            } else {
                Coordinate(start.x + it, start.y)
            }
        }
    }

    // ToDo: Вынести в класс с доской, так же из класса game вынести в класс board для удаления дубликата кода
    private fun displayBoards(playerBoard: Board, opponentBoard: Board) {

        val playerDisplay = playerBoard.getBoardDisplay(showShips = true)
        val opponentDisplay = opponentBoard.getBoardDisplay(showShips = false)

        println("Your board:".padEnd(25) + "Opponent's board:")
        for (i in playerDisplay.indices) {
            println(playerDisplay[i].padEnd(25) + opponentDisplay[i])
        }
    }

    // ToDo: Вынести в класс board
    private fun displayBoard(board: Board, showShips: Boolean) {
        val display = board.getBoardDisplay(showShips)

        for (line in display) {
            println(line)
        }
    }
}
