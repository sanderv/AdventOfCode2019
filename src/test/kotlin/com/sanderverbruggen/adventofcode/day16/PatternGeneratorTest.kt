package com.sanderverbruggen.adventofcode.day16

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PatternGeneratorTest {
    @ParameterizedTest
    @CsvSource(
            "0 | 0, 1, 0, -1, 0, 1, 0, -1, 0, 1",
            "1 | 0, 1, 1, 0, 0, -1, -1, 0, 0, 1",
            "2 | 0, 0, 1, 1, 1, 0, 0, 0, -1, -1",
            delimiter = '|'
    )
    internal fun `should generate pattern based on position`(position: Int, expectedPattern: String) {
        val patternGenerator = PatternGenerator(position + 1)
        expectedPattern.split(", ")
                .map { it.toInt() }
                .forEach {
                    assertThat(patternGenerator.next()).isEqualTo(it)
                }
    }
}