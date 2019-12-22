package com.sanderverbruggen.adventofcode.day10

import com.sanderverbruggen.adventofcode.readFile
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

    @Test
    internal fun `part 1 solution should be 284`() {
        val map = AsteroidMap(readFile("day10/input.txt"))
        assertThat(map.findAsteroidWithMostDetected().nrOfDetectableAsteroids()).isEqualTo(284)
    }

    @Test
    internal fun `should vaporize in clockwise order`() {
        val map = AsteroidMap("""
            .#....#####...#..
            ##...##.#####..##
            ##...#...#.#####.
            ..#.....#...###..
            ..#.#.....#....##
        """.trimIndent())

        val asteroid = map.findAsteroidWithMostDetected()
        val order = asteroid.vaporize()
        assertThat(order.take(5)).containsExactly(
                Asteroid(8, 1),
                Asteroid(9, 0),
                Asteroid(9, 1),
                Asteroid(10, 0),
                Asteroid(9, 2)
        )
    }

    @Test
    internal fun `part 2 solution should be 404`() {
        val map = AsteroidMap(readFile("day10/input.txt"))
        val asteroid = map.findAsteroidWithMostDetected()
        val target200 = asteroid.vaporize()[199]
        assertThat(target200).isEqualTo(Asteroid(4, 4))
    }
}