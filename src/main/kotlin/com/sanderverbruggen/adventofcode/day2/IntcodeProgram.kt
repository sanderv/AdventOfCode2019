package com.sanderverbruggen.adventofcode.day2

import com.sanderverbruggen.adventofcode.day2.Opcode.*

internal class IntcodeProgram(val program: IntArray) {
    constructor(program: String) : this(program.split(",").map { it.toInt() }.toIntArray())

    private var instructionPointer = 0

    fun run() {
        while (getOpcode() != END) {
            when (getOpcode()) {
                ADD -> add()
                MULTIPLY -> multiply()
            }
            advance(when (getOpcode()) {
                ADD, MULTIPLY -> 4
                else -> TODO("Don't know what to do")
            })
        }
    }

    private fun getOpcode() = Opcode.byCode(program[instructionPointer] % 100)

    private fun add() = execute { x, y -> x + y }

    private fun multiply() = execute { x, y -> x * y }

    private fun execute(calc: (x: Int, y: Int) -> Int) {
        val param1 = program[program[instructionPointer + 1]]
        val param2 = program[program[instructionPointer + 2]]
        val targetAddress = program[instructionPointer + 3]
        program[targetAddress] = calc(param1, param2)
    }

    private fun advance(steps: Int) {
        instructionPointer += steps
    }

    override fun toString() = program.joinToString(",")
}

enum class Opcode(private val code: Int) {
    ADD(1),
    MULTIPLY(2),
    END(99);

    companion object {
        fun byCode(code: Int) = values().first { it.code == code }
    }
}