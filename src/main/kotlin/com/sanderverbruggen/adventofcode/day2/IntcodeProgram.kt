package com.sanderverbruggen.adventofcode.day2

import com.sanderverbruggen.adventofcode.day2.Opcode.*
import kotlin.math.pow

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

    internal fun getOpcode() = Opcode.byCode(getInstruction() % 100)

    internal fun getParam(paramNr: Int): Int {
        return when (getParamMode(paramNr)) {
            ParamMode.POSITION -> program[program[instructionPointer + paramNr]]
            ParamMode.IMMEDIATE -> program[instructionPointer + paramNr]
        }
    }

    internal fun getParamMode(paramNr: Int) = ParamMode.byCode((getInstruction() / 10.0.pow(paramNr + 1) % 10).toInt())

    internal fun getInstruction() = program[instructionPointer]

    private fun add() = calculate { x, y -> x + y }

    private fun multiply() = calculate { x, y -> x * y }

    private fun calculate(calc: (x: Int, y: Int) -> Int) {
        val targetAddress = program[instructionPointer + 3]
        program[targetAddress] = calc(getParam(1), getParam(2))
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

enum class ParamMode(val code: Int) {
    POSITION(0), IMMEDIATE(1);

    companion object {
        fun byCode(code: Int) = values().first { it.code == code }
    }
}