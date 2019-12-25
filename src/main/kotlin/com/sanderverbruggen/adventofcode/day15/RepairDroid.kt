package com.sanderverbruggen.adventofcode.day15

import com.sanderverbruggen.adventofcode.day15.AndroidDirection.*
import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import com.sanderverbruggen.adventofcode.day3.Point
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.max
import kotlin.math.min

class RepairDroid(input: String) {
    private val brain = IntcodeProgram(input)
    private val area: MutableMap<Point, Cell> = mutableMapOf()
    private var currentLocation = Point(0, 0)
    private var moved = false

    private var minX = 0
    private var maxX = 0
    private var minY = 0
    private var maxY = 0

    fun solve() {
        runBlocking {
            launch { brain.suspendedRun() }
            area.putIfAbsent(currentLocation, Cell(CellType.START))

            do {
                var move = nextMove()
                if (move == BACKTRACK) {
                    move = currentCell().enteredFrom!!
                }
                brain.inputChannel.send(move.command)
                val cellType = getCellType()
                markCell(cellType, move)
                if (cellType != CellType.WALL) {
                    executeMove(move)
                }
                printArea()
            } while (movePossible())
        }
        printArea()
    }

    private suspend fun getCellType(): CellType {
        return when (brain.outputChannel.receive().toInt()) {
            0 -> CellType.WALL
            1 -> CellType.FREE
            2 -> CellType.OXYGEN
            else -> throw RuntimeException("Unknown command")
        }
    }

    private fun movePossible() = !moved || area[currentLocation]!!.type != CellType.START
    private fun nextMove(): AndroidDirection {
        var nextDirection: AndroidDirection = currentCell().lastDirectionTried ?: AndroidDirection.values()[0]
        while (nextDirection != BACKTRACK && area.containsKey(getTargetLocation(nextDirection))) {
            nextDirection = values()[nextDirection.ordinal + 1]
        }
        currentCell().lastDirectionTried = nextDirection
        return nextDirection
    }

    private fun currentCell() = area[currentLocation]!!
    private fun markCell(cellType: CellType, move: AndroidDirection) {
        val targetLocation = getTargetLocation(move)
        minX = min(minX, targetLocation.x)
        maxX = max(maxX, targetLocation.x)
        minY = min(minY, targetLocation.y)
        maxY = max(maxY, targetLocation.y)
        area.putIfAbsent(targetLocation, Cell(
                type = cellType,
                enteredFrom = if (cellType == CellType.WALL) null else move.opposite())
        )
    }

    private fun getTargetLocation(move: AndroidDirection): Point {
        val (dx, dy) = when (move) {
            NORTH -> Pair(0, -1)
            EAST -> Pair(1, 0)
            SOUTH -> Pair(0, 1)
            WEST -> Pair(-1, 0)
            else -> throw RuntimeException("Shouldn't place on backtracking")
        }
        return Point(currentLocation.x + dx, currentLocation.y + dy)
    }

    private fun executeMove(move: AndroidDirection) {
        currentLocation = getTargetLocation(move)
        moved = true
    }

    private fun printArea() {
        println()
        println()
        println()
        val unknownCell = Cell(CellType.UNKNOWN)
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val drawPoint = Point(x, y)
                print(if (drawPoint == currentLocation) 'D' else area.getOrDefault(drawPoint, unknownCell))
            }
            println()
        }
    }
}

class Cell(
        val type: CellType,
        val enteredFrom: AndroidDirection? = null
) {
    var lastDirectionTried: AndroidDirection? = null
    override fun toString(): String {
        return type.display.toString()
    }
}

enum class CellType(
        val code: Int,
        val display: Char
) {
    WALL(0, '#'),
    FREE(1, '.'),
    OXYGEN(2, 'O'),
    START(-1, 'S'),
    UNKNOWN(-999, ' ')
}

enum class AndroidDirection(
        val command: Long
) {
    NORTH(1), EAST(4), SOUTH(2), WEST(3), BACKTRACK(-1);

    fun opposite(): AndroidDirection = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
        else -> throw RuntimeException("No opposite for backtracking")
    }
}