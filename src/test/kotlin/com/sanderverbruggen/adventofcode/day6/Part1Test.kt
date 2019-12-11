package com.sanderverbruggen.adventofcode.day6

import com.sanderverbruggen.adventofcode.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Part1Test {
    @Test
    internal fun `should calculate orbits for example`() {
        val calculator = OrbitalCalculator(readFile("day6/example.txt"))

        assertThat(calculator.getNrDirectAndIndirectOrbits()).isEqualTo(42)
    }

    @Test
    internal fun `part 1 solution`() {
        val calculator = OrbitalCalculator(readFile("day6/input.txt"))

        assertThat(calculator.getNrDirectAndIndirectOrbits()).isEqualTo(224901)
    }

    @Test
    internal fun `should calculate orbits for part 2 example`() {
        val calculator = OrbitalCalculator(readFile("day6/example2.txt"))

        assertThat(calculator.getNrOrbitTransfersToSAN()).isEqualTo(4)
    }

    @Test
    internal fun `part 2 solution`() {
        val calculator = OrbitalCalculator(readFile("day6/input.txt"))

        assertThat(calculator.getNrOrbitTransfersToSAN()).isEqualTo(334)
    }
}