package com.sanderverbruggen.adventofcode.day10

import java.lang.Math.toDegrees
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class AsteroidMap(mapString: String) {
    val asteroids = parse(mapString)

    private fun parse(mapString: String): List<Asteroid> {
        val asteroids = mapString.lines()
                .mapIndexed { y, line ->
                    line
                            .mapIndexed { x, cell -> if (cell == '#') Asteroid(x, y) else null }
                            .filterNotNull()
                }
                .flatten()

        asteroids.forEach { asteroid ->
            asteroids.forEach { otherAsteroid ->
                asteroid.addNeighbor(otherAsteroid)
            }

        }

        return asteroids
    }

    fun findAsteroidWithMostDetected() = asteroids.maxBy { it.nrOfDetectableAsteroids() }!!
}

class Asteroid(
        val x: Int,
        val y: Int,
        val neighbors: MutableMap<Double, MutableList<NeighborAsteroid>> = mutableMapOf()
) {
    fun addNeighbor(otherAsteroid: Asteroid) {
        if (otherAsteroid == this)
            return

        val angle = getAngle(otherAsteroid)
        val distance = getDistance(otherAsteroid)
        val neighbor = NeighborAsteroid(otherAsteroid, angle, distance)
        neighbors.getOrPut(angle, { mutableListOf() })
                .add(neighbor)
    }

    fun nrOfDetectableAsteroids() = neighbors.size

    fun vaporize(): List<Asteroid> {
        neighbors.forEach {
            it.value
                    .sortedBy { it.distance }
                    .forEachIndexed { index, neighborAsteroid -> neighborAsteroid.blockedBy = index }
        }
        val order = neighbors
                .flatMap {
                    it.value
                }.toMutableList()

        order.sortWith(compareBy(
                { it.blockedBy },
                { it.angle }))

        return order.map { it.asteroid }
    }


    fun getAngle(other: Asteroid): Double {
        val angle = toDegrees(atan2(other.y.toDouble() - y, other.x.toDouble() - x)) + 90
        return if (angle < 0) {
            angle + 360
        } else {
            angle
        }
    }

    private fun getDistance(other: Asteroid): Double {
        return sqrt(abs(other.x.toDouble() - x).pow(2) + abs(other.y.toDouble() - y).pow(2))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Asteroid

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString() = "($x, $y)"
}

class NeighborAsteroid(
        val asteroid: Asteroid,
        val angle: Double,
        val distance: Double,
        var blockedBy: Int = 0
)