package com.sanderverbruggen.adventofcode.day3

import kotlin.math.abs

class Map(
        private val path1: Path,
        private val path2: Path
) {
    companion object {
        private fun draw(instructions: String): Path {
            val path = Path()
            instructions.split(",")
                    .forEach { path.addStep(it) }
            return path
        }
    }

    constructor(instructionsPath1: String, instructionsPath2: String) : this(draw(instructionsPath1), draw(instructionsPath2))

    fun findNearestManhattanCrossingDistance(): Int {
        return path1.findNearestManhattanCrossingDistance(path2)
    }

    fun findNearestPathStepsDistance(): Int {
        return path1.findNearestPathStepsDistance(path2)
    }
}

class Path {
    private var currentPoint = Point(0, 0)
    private val pointCache: MutableMap<Int, MutableMap<Int, Point>> = mutableMapOf()
    val steps: MutableList<Point> = mutableListOf()

    fun addStep(instruction: String) {
        val direction = Direction.byCode(instruction[0].toString())
        val distance = instruction.drop(1).toInt()
        repeat(distance) {
            val nextPoint = currentPoint.move(direction)
            // This should find a previous "encounter" of this point, according to the specs:
            // If a wire visits a position on the grid multiple times, use the steps value from the first time it visits that position when calculating the total value of a specific intersection.
            // But the site does't accept that answer
            currentPoint = /*pointCache.get(nextPoint.x)?.get(nextPoint.y)
                    ?:*/ Point(nextPoint.x, nextPoint.y, currentPoint.stepsFromStart + 1)
            steps.add(currentPoint)
            addToCache(currentPoint)
        }
    }

    private fun addToCache(point: Point) {
        pointCache[point.x] = pointCache[point.x] ?: mutableMapOf()
        pointCache[point.x]?.put(point.y, point)
    }

    fun findNearestManhattanCrossingDistance(other: Path): Int {
        return steps.intersect(other.steps)
                .map { abs(it.x) + abs(it.y) }
                .min() ?: throw RuntimeException("No crossing found")
    }

    fun findNearestPathStepsDistance(other: Path): Int {
        return steps.intersect(other.steps)
                .map { crossing ->
                    this.findPoint(crossing).stepsFromStart + other.findPoint(crossing).stepsFromStart
                }
                .min() ?: throw RuntimeException("No crossing found")
    }

    internal fun findPoint(point: Point): Point = steps.find { point == it }
            ?: throw RuntimeException("Point not found")
}

class Point(val x: Int, val y: Int, val stepsFromStart: Int = 0) {
    fun move(direction: Direction): Point {
        return when (direction) {
            Direction.LEFT -> Point(x - 1, y)
            Direction.RIGHT -> Point(x + 1, y)
            Direction.UP -> Point(x, y + 1)
            Direction.DOWN -> Point(x, y - 1)
        }
    }

    override fun toString() = "($x, $y)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

enum class Direction(private val code: String) {
    UP("U"), RIGHT("R"), DOWN("D"), LEFT("L");

    companion object {
        fun byCode(code: String) = values().first { it.code == code }
    }

    /**
     * @param turn 0 = left, 1 = right
     */
    fun turn(turn: Int): Direction {
        var newOrdinal = this.ordinal + if (turn == 0) -1 else 1
        if (newOrdinal >= values().size) newOrdinal = 0
        if (newOrdinal < 0) newOrdinal = values().size - 1

        return values()[newOrdinal]
    }
}
