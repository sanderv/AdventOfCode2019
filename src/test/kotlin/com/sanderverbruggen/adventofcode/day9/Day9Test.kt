package com.sanderverbruggen.adventofcode.day9

import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import com.sanderverbruggen.adventofcode.readFile
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day9Test {
    @ParameterizedTest
    @CsvSource(
            "1102,34915192,34915192,7,4,7,99,0 | 1219070632396864",
            "104,1125899906842624,99 | 1125899906842624",
            delimiter = '|'
    )
    @Disabled
    internal fun `should find answers to part 1 examples`(intCode: String, expectedAnswer: Long) {
        val program = IntcodeProgram(intCode)
        val answer = runBlocking { program.suspendedRun() }
        Assertions.assertThat(answer).isEqualTo(expectedAnswer)
    }

    @Test
    @Disabled
    internal fun `should return copy of itself`() {
        val intCode = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
        val program = IntcodeProgram(intCode, outputChannel = Channel(20))
        val result = runBlocking {
            program.suspendedRun()
        }
        Assertions.assertThat(program.toString()).isEqualTo(intCode)
    }

    @Test
    internal fun `part 1 solution should be`() {
        val intCode = readFile("day9/input.txt")
        val result = runBlocking {
            IntcodeProgram(intCode)
                    .apply { inputChannel.send(1) }
                    .suspendedRun()
        }
        println(result)
    }
}