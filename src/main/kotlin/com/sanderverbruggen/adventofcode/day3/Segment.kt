package com.sanderverbruggen.adventofcode.day3

import java.lang.Integer.max
import java.lang.Integer.min

enum class Direction { L, R, U, D }

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
            is VerticalSegment -> if (other.x in startX..endX && y in other.startY..other.endY) other.x + y else 0
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
            is HorizontalSegment -> if (other.y in startY..endY && x in other.startX..other.endX) other.y + x else 0
            is VerticalSegment -> 0
            else -> 0
        }
    }
}

class Point(val x: Int, val y: Int)