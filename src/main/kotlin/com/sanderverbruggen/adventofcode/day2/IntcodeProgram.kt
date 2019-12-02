package com.sanderverbruggen.adventofcode.day2

internal class IntcodeProgram(val program: IntArray) {
    constructor(program: String) : this(program.split(",").map { it.toInt() }.toIntArray())

    private var instructionPointer = 0

    fun run() {
        while (getOpcode() != 99) {
            when (getOpcode()) {
                1 -> add()
                2 -> multiply()
            }
            next()
        }
    }

    private fun getOpcode() = program[instructionPointer]

    private fun add() = execute { x, y -> x + y }

    private fun multiply() = execute { x, y -> x * y }

    private fun execute(calc: (x: Int, y: Int) -> Int) {
        val param1 = program[program[instructionPointer + 1]]
        val param2 = program[program[instructionPointer + 2]]
        val targetAddress = program[instructionPointer + 3]
        program[targetAddress] = calc(param1, param2)
    }

    private fun next() {
        instructionPointer += 4
    }

    override fun toString() = program.joinToString(",")
}