package com.sanderverbruggen.adventofcode.day16

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
}