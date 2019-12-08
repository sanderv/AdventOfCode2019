package com.sanderverbruggen.adventofcode.day4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PasswordCrackerTest {
    @ParameterizedTest
    @CsvSource(
            " 111111 | true  ",
            " 223450 | false ",
            " 123789 | false ",
            delimiter = '|'
    )
    internal fun `should check facts`(password: Int, expectedMatch: Boolean) {
        assertThat(PasswordCracker().matchesFacts(password)).isEqualTo(expectedMatch)
    }

    @Test
    internal fun `part 1 answer is 475`() {
        assertThat(PasswordCracker().solve((372304..847060))).isEqualTo(475)
    }
}