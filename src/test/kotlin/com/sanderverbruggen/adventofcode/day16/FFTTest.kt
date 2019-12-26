package com.sanderverbruggen.adventofcode.day16

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class FFTTest {
    @Test
    internal fun `should calculate phases for example`() {
        val input = "12345678"
        val expectedOutputs = listOf("48226158", "34040438", "03415518", "01029498")

        val fft = FFT(input)
        expectedOutputs.forEach { expectedOutput ->
            assertThat(fft.nextPhase()).isEqualTo(expectedOutput)
        }
    }

    @ParameterizedTest
    @CsvSource(
            "80871224585914546619083218645595 | 24176176",
            "19617804207202209144916044189917 | 73745418",
            "69317163492948606335995924319873 | 52432133",
            delimiter = '|'
    )
    internal fun `should calculate 100 phases examples`(input: String, expectedResult: String) {
        val fft = FFT(input)
        repeat(99) { fft.nextPhase() }
        val result = fft.nextPhase()
        assertThat(result).startsWith(expectedResult)
    }
}