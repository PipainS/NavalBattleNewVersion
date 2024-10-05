package Base.Abstract

import Base.Board.Board
import Base.Coordinate
import Base.Enum.Orientation
import Base.Ship

class HumanPlayer(board: Board) : Player(board) {
    private val letterToIndex = ('A'..'Z').mapIndexed { index, c -> c.toString() to index }.toMap() // fix

    override fun makeMove(opponentBoard: Board): Coordinate {
        println("Your board:")
        displayBoards(board, opponentBoard)

        println("Enter coordinates (e.g., A 0): ")
        val input = readLine()
        val (letter, x) = input!!.split(" ")
        val y = letterToIndex[letter.uppercase()] ?: throw IllegalArgumentException("Invalid coordinate")
        return Coordinate(x.toInt(), y)
    }

    override fun placeShips() {
        println("Your board before placing ships:")
        displayBoard(board, true)

        val shipSizes = listOf(5, 4, 3, 3, 2) // Example ship sizes
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

    private fun displayBoards(playerBoard: Board, opponentBoard: Board) {
        val playerDisplay = playerBoard.getBoardDisplay(showShips = true)
        val opponentDisplay = opponentBoard.getBoardDisplay(showShips = false)

        println("Your board:".padEnd(25) + "Opponent's board:")
        for (i in playerDisplay.indices) {
            println(playerDisplay[i].padEnd(25) + opponentDisplay[i])
        }
    }

    private fun displayBoard(board: Board, showShips: Boolean) {
        val display = board.getBoardDisplay(showShips)
        for (line in display) {
            println(line)
        }
    }
}
