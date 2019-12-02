package com.sanderverbruggen.adventofcode.day1

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Day 1 Part 2's")
internal class Day1Part2Test {
    private val input = this::class.java.classLoader.getResource("day1/input.txt")
            .readText()
            .lines()
            .map { it.toInt() }

    @ParameterizedTest
    @CsvSource(
            "    12,     2",
            "    14,     2",
            "  1969,   966",
            "100756, 50346"
    )
    internal fun `examples should work`(moduleWeight: Int, expectedFuelWeight: Int) {
        Assertions.assertThat(Day1Part2().calculateFuelNeeded(listOf(moduleWeight))).isEqualTo(expectedFuelWeight)
    }

    @Test
    internal fun `final answer should be 5073456`() {
        Assertions.assertThat(Day1Part2().calculateFuelNeeded(input)).isEqualTo(5073456)
    }

}