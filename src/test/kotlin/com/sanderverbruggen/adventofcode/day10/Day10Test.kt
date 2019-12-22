package com.sanderverbruggen.adventofcode.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    internal fun `should find asteroids`() {
        val mapString = """
            .#..#
            .....
            #####
            ....#
            ...##
        """.trimIndent()
        val map = AsteroidMap(mapString)

        assertThat(map.asteroids).containsExactlyInAnyOrder(
                Asteroid(1, 0),
                Asteroid(4, 0),
                Asteroid(0, 2),
                Asteroid(1, 2),
                Asteroid(2, 2),
                Asteroid(3, 2),
                Asteroid(4, 2),
                Asteroid(4, 3),
                Asteroid(3, 4),
                Asteroid(4, 4)
        )
    }
}