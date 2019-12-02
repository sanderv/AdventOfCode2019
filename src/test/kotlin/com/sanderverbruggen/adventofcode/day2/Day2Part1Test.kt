package com.sanderverbruggen.adventofcode.day2

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Day 2 Part 1's")
class Day2Part1Test {
    @ParameterizedTest
    @CsvSource(
            "1,0,0,0,99          | 2,0,0,0,99",
            "2,3,0,3,99          | 2,3,0,6,99",
            "2,4,4,5,99,0        | 2,4,4,5,99,9801",
            "1,1,1,4,99,5,6,0,99 | 30,1,1,4,2,5,6,0,99"
    )
    internal fun `examples should work`() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}