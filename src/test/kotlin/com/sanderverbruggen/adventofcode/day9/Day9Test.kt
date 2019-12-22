package com.sanderverbruggen.adventofcode.day9

import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import com.sanderverbruggen.adventofcode.readFile
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@Tag("IntCode")
class Day9Test {
    @ParameterizedTest
    @CsvSource(
            "1102,34915192,34915192,7,4,7,99,0 | 1219070632396864",
            "104,1125899906842624,99 | 1125899906842624",
            delimiter = '|'
    )
    internal fun `should find answers to part 1 examples`(intCode: String, expectedAnswer: Long) {
        val program = IntcodeProgram(intCode)
        val answer = runBlocking { program.suspendedRun() }
        assertThat(answer).isEqualTo(expectedAnswer)
    }

    @Test
    internal fun `should return copy of itself`() {
        val intCode = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
        val program = IntcodeProgram(intCode, outputChannel = Channel(20))
        runBlocking {
            program.suspendedRun()
        }
        val result = runBlocking {
            program.outputChannel.consumeAsFlow()
                    .toList()
                    .joinToString(",")
        }
        assertThat(result).isEqualTo(intCode)
    }

    @Test
    internal fun `part 1 solution should be 2775723069`() {
        val result = runWithInput(1)
        assertThat(result).isEqualTo(2775723069)
    }

    @Test
    internal fun `part 2 solution should be 49115`() {
        val result = runWithInput(2)
        assertThat(result).isEqualTo(49115L)
    }

    private fun runWithInput(programInput: Long): Long {
        val intCode = readFile("day9/input.txt")
        return runBlocking {
            IntcodeProgram(intCode)
                    .apply { inputChannel.send(programInput) }
                    .suspendedRun()
        }
    }
}