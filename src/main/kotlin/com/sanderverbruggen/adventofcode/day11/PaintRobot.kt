package com.sanderverbruggen.adventofcode.day11

import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import com.sanderverbruggen.adventofcode.day3.Direction
import com.sanderverbruggen.adventofcode.day3.Point
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PaintRobot(intCode: String) {
    private val brain = IntcodeProgram(intCode)
    internal val hull: MutableMap<Point, Color> = mutableMapOf()

    fun paint(startingColor: Color = Color.BLACK) {
        var position = Point(0, 0)
        paint(position, startingColor.ordinal)
        var direction = Direction.UP

        runBlocking {
            val paintJob = launch { brain.suspendedRun() }

            while (paintJob.isActive) {
                brain.inputChannel.send(hull.getOrDefault(position, Color.BLACK).value.toLong())

                val requestedColor = brain.outputChannel.receive().toInt()
                val requestedTurn = brain.outputChannel.receive().toInt()

                paint(position, requestedColor)

                direction = direction.turn(requestedTurn)
                position = position.move(direction)
            }
        }
    }

    private fun paint(position: Point, requestedColor: Int) {
        hull[position] = Color.values()[requestedColor]
    }

    fun takeHullPicture() {
        val shiftHorizontal = hull.minBy { it.key.x }!!.key.x * -1
        val width = hull.maxBy { it.key.x }!!.key.x + shiftHorizontal + 1
        val shiftVertical = hull.minBy { it.key.y }!!.key.y * -1
        val height = hull.maxBy { it.key.y }!!.key.y + shiftVertical + 1
        val image = MutableList(height) { " ".repeat(width) }

        hull.forEach { point, color ->
            if (color == Color.WHITE) {
                val x = point.x + shiftHorizontal
                val y = point.y + shiftVertical
                image[y] = image[y].replaceRange(x, x + 1, "#")
            }

        }

        image
                .reversed()
                .forEach { println(it) }
    }
}

enum class Color(val value: Int) { BLACK(0), WHITE(1) }
