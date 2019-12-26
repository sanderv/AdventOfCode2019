package com.sanderverbruggen.adventofcode.day17

import com.sanderverbruggen.adventofcode.day15.AndroidDirection
import com.sanderverbruggen.adventofcode.day15.RepairDroidCell
import com.sanderverbruggen.adventofcode.day15.RepairDroidCellType
import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import com.sanderverbruggen.adventofcode.day3.Point

open class Robot<T>(input: String) {
    protected val brain = IntcodeProgram(input)
    protected val area: MutableMap<Point, T> = mutableMapOf()
    protected var currentLocation = Point(0, 0)

    protected fun getTargetLocation(move: AndroidDirection, fromLocation: Point = currentLocation): Point {
        val (dx, dy) = when (move) {
            AndroidDirection.NORTH -> Pair(0, -1)
            AndroidDirection.EAST -> Pair(1, 0)
            AndroidDirection.SOUTH -> Pair(0, 1)
            AndroidDirection.WEST -> Pair(-1, 0)
            else -> throw RuntimeException("Shouldn't place on backtracking")
        }
        return Point(fromLocation.x + dx, fromLocation.y + dy)
    }

    protected fun printArea() {
        println()
        println()
        println()
        val unknownCell = RepairDroidCell(RepairDroidCellType.UNKNOWN)
        for (y in minY()..maxY()) {
            for (x in minX()..maxX()) {
                val drawPoint = Point(x, y)
                print(area.getOrDefault(drawPoint, unknownCell))
            }
            println()
        }
    }

    private fun minX() = area.keys.minBy { it.x }?.x ?: 0
    private fun maxX() = area.keys.maxBy { it.x }?.x ?: 0
    private fun minY() = area.keys.minBy { it.y }?.y ?: 0
    private fun maxY() = area.keys.maxBy { it.y }?.y ?: 0

}