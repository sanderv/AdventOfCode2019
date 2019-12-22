package com.sanderverbruggen.adventofcode.day12

import kotlin.math.abs

class System(moons: List<Moon> = listOf(Moon(0, 0, 0))) {
    var moons = moons

    fun nextStep() {
        moons.forEach {
            it.applyGravity(moons)
        }
        moons.forEach {
            it.applyVelocity()
        }
    }

    fun totalEnergy(): Int = moons
            .map { it.totalEnergy() }
            .sum()

    override fun toString() = moons.joinToString("\n")
}

data class Moon(
        val position: Coordinate3D,
        val velocity: Coordinate3D = Coordinate3D(0, 0, 0)
) {
    constructor(x: Int, y: Int, z: Int) : this(Coordinate3D(x, y, z))

    fun applyGravity(otherMoons: List<Moon>) {
        otherMoons
                .filter { it != this }
                .forEach { moon ->
                    velocity.x += compareValues(moon.position.x, position.x)
                    velocity.y += compareValues(moon.position.y, position.y)
                    velocity.z += compareValues(moon.position.z, position.z)
                }
    }

    fun applyVelocity() {
        position.x += velocity.x
        position.y += velocity.y
        position.z += velocity.z
    }

    fun potentialEnergy() = position.absSum()
    fun kineticEnergy() = velocity.absSum()
    fun totalEnergy() = potentialEnergy() * kineticEnergy()

    override fun toString() = "pos=$position, vel=$velocity"
}

class Coordinate3D(var x: Int, var y: Int, var z: Int) {
    fun absSum() = abs(x) + abs(y) + abs(z)
    override fun toString() = "<x=%3d, y=%3d, z=%3d>".format(x, y, z)
}

