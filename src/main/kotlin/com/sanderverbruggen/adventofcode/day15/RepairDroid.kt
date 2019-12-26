package com.sanderverbruggen.adventofcode.day15

import com.sanderverbruggen.adventofcode.day15.AndroidDirection.*
import com.sanderverbruggen.adventofcode.day17.Robot
import com.sanderverbruggen.adventofcode.day3.Point
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.max
import kotlin.math.min

class RepairDroid(input: String) : Robot<RepairDroidCell>(input) {
    private var moved = false
    private var nrStepsTaken = 0
    private var nrMinutesWaited = -1

    private var minX = 0
    private var maxX = 0
    private var minY = 0
    private var maxY = 0

    fun solve() {
        runBlocking {
            val job = launch { brain.suspendedRun() }
            area.putIfAbsent(currentLocation, RepairDroidCell(RepairDroidCellType.FREE))

            do {
                var move = nextMove()
                if (move == BACKTRACK) {
                    move = currentCell().enteredFrom!!
                    nrStepsTaken -= 2 // Remove 2, because taking the step back will add 1
                }
                brain.inputChannel.send(move.command)
                val cellType = getCellType()
                markCell(cellType, move)
                if (cellType != RepairDroidCellType.WALL) {
                    executeMove(move)
                }
            } while (movePossible())
            println("Done")

            nrMinutesWaited = 0
            while (emptySpaceLeft()) {
                waitAMinute()
                nrMinutesWaited++
            }
            println("Ship flooded with oxygen in $nrMinutesWaited minutes")
            job.cancel()
        }
    }

    private fun emptySpaceLeft() = area.values.any { it.type == RepairDroidCellType.FREE }
    private fun waitAMinute() {
        area
                .filter { it.value.type == RepairDroidCellType.OXYGEN }
                .forEach { floodSurroundings(it.key) }
    }

    private fun floodSurroundings(location: Point) {
        currentLocation = location
        listOf(NORTH, EAST, SOUTH, WEST)
                .forEach { direction ->
                    val type = area[getTargetLocation(direction)]!!.type
                    if (type != RepairDroidCellType.WALL) {
                        markCell(RepairDroidCellType.OXYGEN, direction)
                    }
                }
    }

    private suspend fun getCellType(): RepairDroidCellType {
        return when (brain.outputChannel.receive().toInt()) {
            0 -> RepairDroidCellType.WALL
            1 -> RepairDroidCellType.FREE
            2 -> RepairDroidCellType.OXYGEN
            else -> throw RuntimeException("Unknown command")
        }
    }

    private fun movePossible() = !moved || currentLocation != Point(0, 0)
    private fun nextMove(): AndroidDirection {
        var nextDirection: AndroidDirection = currentCell().lastDirectionTried ?: AndroidDirection.values()[0]
        while (nextDirection != BACKTRACK && area.containsKey(getTargetLocation(nextDirection))) {
            nextDirection = values()[nextDirection.ordinal + 1]
        }
        currentCell().lastDirectionTried = nextDirection
        return nextDirection
    }

    private fun currentCell() = area[currentLocation]!!
    private fun markCell(cellType: RepairDroidCellType, move: AndroidDirection) {
        val targetLocation = getTargetLocation(move)
        if (cellType == RepairDroidCellType.OXYGEN && nrMinutesWaited == -1) {
            println("Found oxygen at $targetLocation in ${nrStepsTaken + 1} steps")
        }
        if (cellType != RepairDroidCellType.OXYGEN) {
            area.putIfAbsent(targetLocation, RepairDroidCell(
                    type = cellType,
                    enteredFrom = if (cellType == RepairDroidCellType.WALL) null else move.opposite())
            )
        } else {
            area.put(targetLocation, RepairDroidCell(
                    type = cellType,
                    enteredFrom = if (cellType == RepairDroidCellType.WALL) null else move.opposite())
            )
        }
        minX = min(minX, targetLocation.x)
        maxX = max(maxX, targetLocation.x)
        minY = min(minY, targetLocation.y)
        maxY = max(maxY, targetLocation.y)
    }

    private fun executeMove(move: AndroidDirection) {
        currentLocation = getTargetLocation(move)
        nrStepsTaken++
        moved = true
    }

}

class RepairDroidCell(
        val type: RepairDroidCellType,
        val enteredFrom: AndroidDirection? = null
) {
    var lastDirectionTried: AndroidDirection? = null
    override fun toString(): String {
        return type.display.toString()
    }
}

enum class RepairDroidCellType(
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