package com.sanderverbruggen.adventofcode.day12

import kotlin.math.absoluteValue

class System(val moons: List<Moon> = listOf(Moon(0, 0, 0))) {
    val moonPairs = moons.permutations()

    fun nextStep() {
        moonPairs.forEach {
            it.first.applyGravity(it.second)
            it.second.applyGravity(it.first)
        }
        moons.forEach {
            it.applyVelocity()
        }
    }

    fun totalEnergy(): Int = moons
            .map { it.totalEnergy() }
            .sum()

    override fun toString() = moonPairs.joinToString("\n")
}

data class Moon(
        val position: Coordinate3D,
        val velocity: Coordinate3D = Coordinate3D(0, 0, 0)
) {
    constructor(x: Int, y: Int, z: Int) : this(Coordinate3D(x, y, z))

    fun applyGravity(otherMoon: Moon) {
        velocity.x += compareValues(otherMoon.position.x, position.x)
        velocity.y += compareValues(otherMoon.position.y, position.y)
        velocity.z += compareValues(otherMoon.position.z, position.z)
    }

    private fun compareValues(a: Int, b: Int) =
            if (a == b) 0 else if (a < b) -1 else 1

    fun applyVelocity() {
        position.x += velocity.x
        position.y += velocity.y
        position.z += velocity.z
    }

    fun potentialEnergy() = position.absSum()
    fun kineticEnergy() = velocity.absSum()
    fun totalEnergy() = potentialEnergy() * kineticEnergy()

    override fun toString() = "$position|$velocity"
//    override fun toString() = "pos=" + position + "," + velocity + "$velocity"
}

class Coordinate3D(var x: Int, var y: Int, var z: Int) {
    fun absSum() = x.absoluteValue + y.absoluteValue + z.absoluteValue
    override fun toString() = "$x,$y,$z"
    //    override fun toString() = "<x=%3d, y=%3d, z=%3d>".format(x, y, z)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate3D

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

}

fun <T> List<T>.permutations(): List<Pair<T, T>> =
        this.mapIndexed { idx, left ->
            this.drop(idx + 1).map { right ->
                Pair(left, right)
            }
        }.flatten()