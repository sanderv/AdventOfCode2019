package com.sanderverbruggen.adventofcode.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day10Test {
    private val mapString = """
            .#..#
            .....
            #####
            ....#
            ...##
        """.trimIndent()

    @Test
    internal fun `should find asteroids`() {
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

    @Test
    internal fun `should find best asteroid in simple example`() {
        val map = AsteroidMap(mapString)

        assertThat(map.findAsteroidWithMostDetected()).isEqualTo(Asteroid(3, 4))
        assertThat(map.findAsteroidWithMostDetected().nrOfDetectableAsteroids()).isEqualTo(8)
    }

    @Test
    internal fun `should find best asteroid in big example`() {
        val map = AsteroidMap("""
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
        """.trimIndent())

        assertThat(map.findAsteroidWithMostDetected()).isEqualTo(Asteroid(11, 13))
        assertThat(map.findAsteroidWithMostDetected().nrOfDetectableAsteroids()).isEqualTo(210)
    }
}