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
    internal fun `should check facts`(password: String, expectedMatch: Boolean) {
        assertThat(PasswordCracker().matchesSimpleFacts(password)).isEqualTo(expectedMatch)
    }

    @ParameterizedTest
    @CsvSource(
            " 112233 | true  ",
            " 123444 | false ",
            " 111122 | true ",
            delimiter = '|'
    )
    internal fun `should check stricter facts`(password: String, expectedMatch: Boolean) {
        assertThat(PasswordCracker().matchesStricterFacts(password)).isEqualTo(expectedMatch)
    }

    @Test
    internal fun `part 1 answer is 475`() {
        assertThat(PasswordCracker().solve((372304..847060))).isEqualTo(475)
    }

    @Test
    internal fun `part 2 answer`() {
        assertThat(PasswordCracker().solve((372304..847060), true)).isEqualTo(297)
    }
}