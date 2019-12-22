package com.sanderverbruggen.adventofcode.day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day12Test {
    @Test
    internal fun `should calculate velocity between moons`() {
        val ganymede = Moon(Coordinate3D(3, 5, 1), Coordinate3D(0, 0, 0))
        val callisto = Moon(Coordinate3D(5, 3, 1), Coordinate3D(0, 0, 0))

        ganymede.applyGravity(listOf(callisto))

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
}