package com.sanderverbruggen.adventofcode.day17

import com.sanderverbruggen.adventofcode.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day17Test {
    @Test
    internal fun `part 1 solution should be 7280`() {
        assertThat(VacuumRobot(readFile("day17/input.txt")).solvePart1()).isEqualTo(7280)
    }

    @Test
    internal fun `part 2 solution should be 1045393`() {
        assertThat(VacuumRobot(readFile("day17/input.txt").replaceRange(0, 1, "2")).solvePart2()).isEqualTo(1045393)
    }
}