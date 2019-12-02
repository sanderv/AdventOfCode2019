package com.sanderverbruggen.adventofcode.day1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Day 1 Part 1's")
internal class Day1Part1Test {
    private val input = this::class.java.classLoader.getResource("day1/input.txt")
            .readText()
            .lines()
            .map { it.toInt() }

    @ParameterizedTest
    @CsvSource(
            "    12,     2",
            "    14,     2",
            "  1969,   654",
            "100756, 33583"
    )
    internal fun `examples should work`(moduleWeight: Int, expectedFuelWeight: Int) {
        assertThat(Day1Part1().calculateFuelNeeded(listOf(moduleWeight))).isEqualTo(expectedFuelWeight)
    }

    @Test
    internal fun `final answer should be 3384232`() {
        assertThat(Day1Part1().calculateFuelNeeded(input)).isEqualTo(3384232)
    }
}
