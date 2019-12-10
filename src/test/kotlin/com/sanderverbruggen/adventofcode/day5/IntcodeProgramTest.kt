package com.sanderverbruggen.adventofcode.day5

import com.sanderverbruggen.adventofcode.day2.Instruction
import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import com.sanderverbruggen.adventofcode.day2.ParamMode
import com.sanderverbruggen.adventofcode.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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

    // Part 2

    @ParameterizedTest
    @CsvSource(
            " 3,9,8,9,10,9,4,9,99,-1,8 | 8 | 7 ",
            " 3,9,7,9,10,9,4,9,99,-1,8 | 7 | 9 ",
            " 3,3,1108,-1,8,3,4,3,99   | 8 | 7 ",
            " 3,3,1107,-1,8,3,4,3,99   | 7 | 9 ",
            " 3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9 | 1 | 0 ",
            " 3,3,1105,-1,9,1101,0,0,12,4,12,99,1 | 1 | 0 "
            , delimiter = '|'
    )
    internal fun `day 5 part 2 examples`(instructions: String, inputResult1: Int, inputResult0: Int) {
        with(TestableIntcodeProgram(instructions, inputResult1)) {
            run()
            assertThat(output.last()).isEqualTo(1)
        }

        with(TestableIntcodeProgram(instructions, inputResult0)) {
            run()
            assertThat(output.last()).isEqualTo(0)
        }
    }

    @Test
    internal fun `day 5 part 2 solution`() {
        val instructions = readFile("day5/input.txt")
        val program = TestableIntcodeProgram(instructions, 5)
        program.run()
        println(program.output)
    }
}

internal class TestableIntcodeProgram(instructions: String, val input: Int) : IntcodeProgram(instructions) {
    val output: MutableList<Int> = mutableListOf()

    init {
        instructionSet[3] = object : Instruction(2, this) {
            override fun exec() {
                program.write(input, 1)
            }
        }
        instructionSet[4] = object : Instruction(2, this) {
            override fun exec() {
                output.add(getParam(1))
            }
        }
    }
}