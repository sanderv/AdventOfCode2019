package com.sanderverbruggen.adventofcode.day16

import kotlin.math.absoluteValue

class FFT(private var signal: String) {
    fun nextPhase(): String {
        signal = (0 until signal.length).map { index ->
            val patternGenerator = PatternGenerator(index + 1)
            "" + signal
                    .map { secondDigit -> secondDigit.toString().toInt() * patternGenerator.next() }
                    .sum()
                    .absoluteValue % 10
        }.joinToString(separator = "")
        return signal
    }
}