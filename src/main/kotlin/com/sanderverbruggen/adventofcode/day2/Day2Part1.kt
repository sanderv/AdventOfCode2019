package com.sanderverbruggen.adventofcode.day2

class Day2Part1 {
    fun runIntcode(intcode: String): String {
        val program = Program(intcode)

        while (program.getOpcode() != 99) {
            when (program.getOpcode()) {
                1 -> program.add()
                2 -> program.multiply()
            }
            program.next()
        }

        return program.toString()
    }
}

internal class Program(val program: IntArray) {
    constructor(program: String) : this(program.split(",").map { it.toInt() }.toIntArray())

    private var pointer = 0

    fun getOpcode() = program[pointer]

    fun add() {
        val param1 = program[program[pointer + 1]]
        val param2 = program[program[pointer + 2]]
        val targetPos = program[pointer + 3]
        program[targetPos] = param1 + param2
        println(this)
    }

    fun multiply() {
        val param1 = program[program[pointer + 1]]
        val param2 = program[program[pointer + 2]]
        val targetPos = program[pointer + 3]
        program[targetPos] = param1 * param2
        println(this)
    }

    fun next() {
        pointer += 4
    }

    override fun toString() = program.joinToString(",")
}