package com.sanderverbruggen.adventofcode.day12

import org.apache.commons.math3.util.ArithmeticUtils.lcm
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day12Test {
    @Test
    internal fun `should calculate velocity between moons`() {
        val ganymede = Moon(Coordinate3D(3, 5, 1), Coordinate3D(0, 0, 0))
        val callisto = Moon(Coordinate3D(5, 3, 1), Coordinate3D(0, 0, 0))

        ganymede.applyGravity(callisto)

        assertThat(ganymede.velocity).isEqualToComparingFieldByField(Coordinate3D(1, -1, 0))
    }

    @Test
    internal fun `should work on system of moons`() {
        val system = System(listOf(
                Moon(-1, 0, 2),
                Moon(2, -10, -7),
                Moon(4, -8, 8),
                Moon(3, 5, -1)
        ))

        repeat(10) {
            system.nextStep()
            println("After $it steps:")
            println(system)
            println()
        }
        assertThat(system.totalEnergy()).isEqualTo(179)
    }

    @Test
    internal fun `another example`() {
        val system = System(listOf(
                Moon(x = -8, y = -10, z = 0),
                Moon(x = 5, y = 5, z = 10),
                Moon(x = 2, y = -7, z = 3),
                Moon(x = 9, y = -8, z = -3)
        ))

        repeat(100) {
            system.nextStep()
            if (it % 10 == 0) {
                println("After $it steps:")
                println(system)
                println()
            }
        }
        assertThat(system.totalEnergy()).isEqualTo(1940)
    }

    @Test
    internal fun `part 1 solution should be 6678`() {
        val system = System(listOf(
                Moon(x = -10, y = -10, z = -13),
                Moon(x = 5, y = 5, z = -9),
                Moon(x = 3, y = 8, z = -16),
                Moon(x = 1, y = 3, z = -3)
        ))

        repeat(1000) {
            system.nextStep()
        }
        assertThat(system.totalEnergy()).isEqualTo(6678)
    }

    @Test
    internal fun `find repeating step example`() {
        val system = System(listOf(
                Moon(-1, 0, 2),
                Moon(2, -10, -7),
                Moon(4, -8, 8),
                Moon(3, 5, -1)
        ))

        val previousForms: HashSet<String> = hashSetOf()

        var found = false
        var step = 0
        while (!found) {
            system.nextStep()
            val systemString = system.toString()
            found = previousForms.contains(systemString)
            previousForms.add(systemString)
            step++
        }

        assertThat(step - 1).isEqualTo(2772)
    }

    @Test
    @Disabled
    internal fun `part 2 solution should be ???`() {
        val system = System(listOf(
                Moon(x = -10, y = -10, z = -13),
                Moon(x = 5, y = 5, z = -9),
                Moon(x = 3, y = 8, z = -16),
                Moon(x = 1, y = 3, z = -3)
        ))

        val previousForms: HashSet<String> = hashSetOf()

        var found = false
        var step = 0L
        while (!found) {
            system.nextStep()
            val systemString = system.toString()
            found = previousForms.contains(systemString)
            previousForms.add(systemString)
            step++
            if (step % 1_000_000 == 0L) {
                println("After $step steps:")
            }
        }

        assertThat(step - 1).isEqualTo(0)
    }

    @Test
    internal fun `part 2 pirated solution`() {
        val system = System(listOf(
                Moon(x = -10, y = -10, z = -13),
                Moon(x = 5, y = 5, z = -9),
                Moon(x = 3, y = 8, z = -16),
                Moon(x = 1, y = 3, z = -3)
        ))
        val moons = system.moons
        val startingX: List<Pair<Int, Int>> = moons.map { it.position.x to it.velocity.x }
        val startingY: List<Pair<Int, Int>> = moons.map { it.position.y to it.velocity.y }
        val startingZ: List<Pair<Int, Int>> = moons.map { it.position.z to it.velocity.z }
        var foundX: Long? = null
        var foundY: Long? = null
        var foundZ: Long? = null
        var stepCount = 0L
        do {
            stepCount += 1
            system.nextStep()
            foundX = if (foundX == null && startingX == moons.map { it.position.x to it.velocity.x }) stepCount else foundX
            foundY = if (foundY == null && startingY == moons.map { it.position.y to it.velocity.y }) stepCount else foundY
            foundZ = if (foundZ == null && startingZ == moons.map { it.position.z to it.velocity.z }) stepCount else foundZ

        } while (foundX == null || foundY == null || foundZ == null)
        println("$foundX - $foundY - $foundZ --> 496734501382552")
        val result = lcm(foundX, lcm(foundY, foundZ))
        assertThat(result).isEqualTo(496734501382552)
    }
}