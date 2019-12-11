package com.sanderverbruggen.adventofcode.day7

import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import java.util.*

class MultiInputRunner(
        intcode: String,
        input: List<Int>
) {
    private val inputStack = Stack<Int>()

    init {
        input.reversed().forEach {
            inputStack.push(it)
        }
    }

    private val program = IntcodeProgram(intcode) { inputStack.pop() }

    fun run(): Int = program.run()
}