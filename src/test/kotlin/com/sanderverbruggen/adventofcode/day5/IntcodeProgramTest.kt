package com.sanderverbruggen.adventofcode.day5

import com.sanderverbruggen.adventofcode.day2.Instruction
import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import com.sanderverbruggen.adventofcode.day2.ParamMode
import com.sanderverbruggen.adventofcode.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IntcodeProgramTest {
    @Test
    internal fun `should get correct param mode`() {
        val program = IntcodeProgram("1002")
        assertThat(program.getParamMode(1)).isEqualTo(ParamMode.POSITION)
        assertThat(program.getParamMode(2)).isEqualTo(ParamMode.IMMEDIATE)
        assertThat(program.getParamMode(3)).isEqualTo(ParamMode.POSITION)
    }

    @Test
    internal fun `should get correct params`() {
        val program = IntcodeProgram("1002,4,3,99,33")
        assertThat(program.getParam(1)).isEqualTo(33)
        assertThat(program.getParam(2)).isEqualTo(3)
    }

    @Test
    internal fun `should run program with new params`() {
        val program = IntcodeProgram("1002,4,3,4,33")
        program.run()
        assertThat(program.program).containsExactly(1002, 4, 3, 4, 99)
    }

    @Test
    internal fun `day 5 part 1 solution should be 7286649`() {
        val instructions = readFile("day5/input.txt")
        val program = TestableIntcodeProgram(instructions, 1)
        program.run()
        assertThat(program.output.last()).isEqualTo(7286649)
    }
}

internal class TestableIntcodeProgram(instructions: String, val input: Int) : IntcodeProgram(instructions) {
    val output: MutableList<Int> = mutableListOf()

    init {
        instructionSet[3] = object : Instruction(2, this) {
            override fun exec() {
                program.write(1, 1)
            }
        }
        instructionSet[4] = object : Instruction(2, this) {
            override fun exec() {
                output.add(getParam(1))
            }
        }
    }
}