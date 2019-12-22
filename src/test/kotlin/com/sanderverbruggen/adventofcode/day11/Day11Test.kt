package com.sanderverbruggen.adventofcode.day11

import com.sanderverbruggen.adventofcode.day3.Direction
import com.sanderverbruggen.adventofcode.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day11Test {
    @ParameterizedTest
    @CsvSource(
            "UP    | 0 | LEFT ",
            "UP    | 1 | RIGHT",
            "LEFT  | 0 | DOWN ",
            "LEFT  | 1 | UP   ",
            delimiter = '|'
    )
    internal fun `should turn correctly`(startingDirection: Direction, turn: Int, expectedDirection: Direction) {
        assertThat(startingDirection.turn(turn)).isEqualTo(expectedDirection)
    }

    @Test
    internal fun `part 1 solution should be 2268`() {
        val intCode = readFile("day11/input.txt")
        val paintRobot = PaintRobot(intCode)
        paintRobot.paint()
        assertThat(paintRobot.hull.size).isEqualTo(2268)
    }

    @Test
    internal fun `part 2 solution`() {
        val intCode = readFile("day11/input.txt")
        val paintRobot = PaintRobot(intCode)
        paintRobot.paint(Color.WHITE)
        paintRobot.takeHullPicture()
    }
}