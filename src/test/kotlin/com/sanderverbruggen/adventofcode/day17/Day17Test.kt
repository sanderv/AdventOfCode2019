package com.sanderverbruggen.adventofcode.day17

import com.sanderverbruggen.adventofcode.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day17Test {
    @Test
    internal fun `part 1 solution should be 7280`() {
        assertThat(VacuumRobot(readFile("day17/input.txt")).solvePart1()).isEqualTo(7280)
    }
}