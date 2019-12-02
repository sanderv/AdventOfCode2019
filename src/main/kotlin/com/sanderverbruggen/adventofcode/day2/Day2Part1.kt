package com.sanderverbruggen.adventofcode.day2

internal class Program(val program: IntArray) {
    constructor(program: String) : this(program.split(",").map { it.toInt() }.toIntArray())

    private var pointer = 0

    fun run(): String {
        while (getOpcode() != 99) {
            when (getOpcode()) {
                1 -> add()
                2 -> multiply()
            }
            next()
        }
        return this.toString()
    }

    private fun getOpcode() = program[pointer]

    private fun add() = execute { x, y -> x + y }

    private fun multiply() = execute { x, y -> x * y }

    private fun execute(calc: (x: Int, y: Int) -> Int) {
        val param1 = program[program[pointer + 1]]
        val param2 = program[program[pointer + 2]]
        val targetPos = program[pointer + 3]
        program[targetPos] = calc(param1, param2)
    }

    private fun next() {
        pointer += 4
    }

    override fun toString() = program.joinToString(",")
}