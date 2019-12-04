package com.sanderverbruggen.adventofcode.day3

import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.Math.abs

class Map(
        private val path1: List<Segment>,
        private val path2: List<Segment>
) {
    companion object {
        private fun draw(instructions: String): List<Segment> {
            var currentPoint = Point(0, 0)
            return instructions.split(",")
                    .map { instruction ->
                        val (segment, newPoint) = Segment.draw(currentPoint, instruction)
                        currentPoint = newPoint
                        segment
                    }.toList()
        }
    }

    constructor(instructionsPath1: String, instructionsPath2: String) : this(draw(instructionsPath1), draw(instructionsPath2))

    fun findNearestCrossingDistance(): Int {
        return path1
                .mapNotNull { path1Segment ->
                    path2
                            .map { path2Segment -> path1Segment.crossingDistanceFromStart(path2Segment) }
                            .filter { it > 0 }
                            .min()
                }
                .filter { it > 0 }
                .min() ?: 0
    }
}

abstract class Segment {

    companion object {
        fun draw(from: Point, instruction: String): Pair<Segment, Point> {
            val direction = Direction.valueOf(instruction[0].toString())
            val distance = instruction.drop(1).toInt()
            return when (direction) {
                Direction.L -> Pair(HorizontalSegment(from.x, from.x - distance, from.y), Point(from.x - distance, from.y))
                Direction.R -> Pair(HorizontalSegment(from.x, from.x + distance, from.y), Point(from.x + distance, from.y))
                Direction.U -> Pair(VerticalSegment(from.x, from.y, from.y + distance), Point(from.x, from.y + distance))
                Direction.D -> Pair(VerticalSegment(from.x, from.y, from.y - distance), Point(from.x, from.y - distance))
            }
        }
    }

    abstract fun crossingDistanceFromStart(other: Segment): Int

}

class HorizontalSegment(
        startX: Int,
        endX: Int,
        val y: Int
) : Segment() {

    val startX: Int = min(startX, endX)
    val endX: Int = max(startX, endX)
    override fun crossingDistanceFromStart(other: Segment): Int {
        return when (other) {
            is HorizontalSegment -> 0
            is VerticalSegment -> {
                val crosses = other.x in startX..endX && y in other.startY..other.endY
                if (crosses) abs(other.x) + abs(y) else 0
            }
            else -> 0
        }
    }

}

class VerticalSegment(
        val x: Int,
        startY: Int,
        endY: Int
) : Segment() {

    val startY = min(startY, endY)
    val endY = max(startY, endY)
    override fun crossingDistanceFromStart(other: Segment): Int {
        return when (other) {
            is HorizontalSegment -> {
                val crosses = other.y in startY..endY && x in other.startX..other.endX
                if (crosses) abs(other.y) + abs(x) else 0
            }
            is VerticalSegment -> 0
            else -> 0
        }
    }

}

class Point(val x: Int, val y: Int)

enum class Direction { L, R, U, D }
