package com.sanderverbruggen.adventofcode.day10

class AsteroidMap(mapString: String) {
    val asteroids = parse(mapString)

    private fun parse(mapString: String): List<Asteroid> {
        return mapString.lines()
                .mapIndexed { y, line ->
                    line.split("").drop(1).dropLast(1)
                            .mapIndexed { x, cell -> if (cell == "#") Asteroid(x, y) else null }
                            .filterNotNull()
                }
                .flatten()
    }
}

class Asteroid(
        val x: Int,
        val y: Int
) {
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