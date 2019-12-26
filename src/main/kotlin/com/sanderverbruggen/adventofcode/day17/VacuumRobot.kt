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
                    val location = Point(x, y)
                    markCell(location, type)
                    if (type.isDroid()) {
                        currentLocation = location
                    }
                }
                if (ascii == 10L) {
                    x = 0
                    y++
                } else {
                    x++
                }
            }
        }
        printArea()
        return findCrossings()
                .map { it.x * it.y }
                .sum()
    }

    fun solvePart2(): Int {
        var result = -1
        runBlocking {
            val job = launch { brain.suspendedRun() }
            launch {
                // Drain output
                while (!brain.outputChannel.isClosedForReceive) {
                    val ascii = brain.outputChannel.receive()
                    if (ascii < 255) {
                        if (ascii == 10L)
                            println()
                        else
                            print(ascii.toChar())
                    } else {
                        result = ascii.toInt()
                    }
                }
            }
            val instructions = listOf(
                    "A,B,A,C,B,C,A,B,A,C",
                    "R,10,L,8,R,10,R,4",
                    "L,6,L,6,R,10",
                    "L,6,R,12,R,12,R,10",
                    "n"
            )

            instructions.forEach { moveFunction ->
                moveFunction.forEach { instruction ->
                    val instr = instruction.toLong()
                    brain.inputChannel.send(instr)
                }
                brain.inputChannel.send(10)
            }
        }
        return result
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
    CAMERA('O'),
    DROID_LEFT('<'),
    DROID_RIGHT('>'),
    DROID_UP('^'),
    DROID_DOWN('v'),
    ;

    companion object {
        fun fromAscii(ascii: Long): VacuumRobotCellType? = values().firstOrNull { it.display.toInt() == ascii.toInt() }
    }

    fun isDroid() = name.startsWith("DROID")

    override fun toString() = display.toString()
}