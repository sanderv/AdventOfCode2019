package com.sanderverbruggen.adventofcode.day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Day 2 Part 1's")
@Tag("IntCode")
class Day2Part1Test {
    @ParameterizedTest
    @CsvSource(
            "1,0,0,0,99          | 2,0,0,0,99",
            "2,3,0,3,99          | 2,3,0,6,99",
            "2,4,4,5,99,0        | 2,4,4,5,99,9801",
            "1,1,1,4,99,5,6,0,99 | 30,1,1,4,2,5,6,0,99",
            delimiter = '|'
    )
    internal fun `examples should work`(intcode: String, expectedResult: String) {
        val program = IntcodeProgram(intcode)
        program.run()
        assertThat(program.toString()).isEqualTo(expectedResult)
    }

    @Test
    internal fun `final answer should be`() {
        val program = IntcodeProgram("1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,1,19,5,23,2,23,6,27,1,27,5,31,2,6,31,35,1,5,35,39,2,39,9,43,1,43,5,47,1,10,47,51,1,51,6,55,1,55,10,59,1,59,6,63,2,13,63,67,1,9,67,71,2,6,71,75,1,5,75,79,1,9,79,83,2,6,83,87,1,5,87,91,2,6,91,95,2,95,9,99,1,99,6,103,1,103,13,107,2,13,107,111,2,111,10,115,1,115,6,119,1,6,119,123,2,6,123,127,1,127,5,131,2,131,6,135,1,135,2,139,1,139,9,0,99,2,14,0,0")
        program.run()
        assertThat(program.program[0]).isEqualTo(5866663)
    }
}