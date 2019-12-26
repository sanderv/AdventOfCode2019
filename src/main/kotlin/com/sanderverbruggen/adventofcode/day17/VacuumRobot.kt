package com.sanderverbruggen.adventofcode.day17

import com.sanderverbruggen.adventofcode.day15.AndroidDirection.*
import com.sanderverbruggen.adventofcode.day17.VacuumRobotCellType.SCAFFOLD
import com.sanderverbruggen.adventofcode.day3.Point
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class VacuumRobot(input: String) : Robot<VacuumRobotCellType>(input) {
    fun solvePart1(): Int {
        runBlocking {
            var x = 0
            var y = 0
            val job = launch { brain.suspendedRun() }
            while (!brain.outputChannel.isClosedForReceive) {
                val ascii = brain.outputChannel.receive()
                val type = VacuumRobotCellType.fromAscii(ascii)
                if (type != null) {
                    markCell(Point(x, y), type)
                }
                if (ascii == 10L) {
                    println()
                    x = 0
                    y++
                } else {
                    print(ascii.toChar())
                    x++
                }
            }
        }
        return findCrossings()
                .map { it.x * it.y }
                .sum()
    }

    private fun markCell(point: Point, type: VacuumRobotCellType) {
        area[point] = type
    }

    private fun findCrossings() =
            area
                    .filter { it.value == SCAFFOLD }
                    .filter { isCrossing(it.key) }
                    .map { it.key }

    private fun isCrossing(point: Point) =
            listOf(NORTH, EAST, SOUTH, WEST)
                    .all { area[getTargetLocation(it, point)] == SCAFFOLD }
}

class VacuumRobotCell(
        val cellType: VacuumRobotCellType
)

enum class VacuumRobotCellType(
        val display: Char
) {
    SCAFFOLD('#'),
    FREE('.'),
    CAMERA('O');

    companion object {
        fun fromAscii(ascii: Long): VacuumRobotCellType? = values().firstOrNull { it.display.toInt() == ascii.toInt() }
    }

    override fun toString() = display.toString()
}