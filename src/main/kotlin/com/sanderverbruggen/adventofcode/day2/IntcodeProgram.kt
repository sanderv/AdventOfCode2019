package com.sanderverbruggen.adventofcode.day2

import com.sanderverbruggen.adventofcode.day2.Opcode.*
import kotlin.math.pow

open class IntcodeProgram(val program: IntArray) {
    constructor(program: String) : this(program.split(",").map { it.toInt() }.toIntArray())

    private var instructionPointer = 0

    fun run() {
        while (getOpcode() != END) {
            val instruction = when (getOpcode()) {
                ADD -> AddInstruction(this)
                MULTIPLY -> MultiplyInstruction(this)
                INPUT -> input()
                OUTPUT -> output()
                else -> throw RuntimeException("Unknown opcode")
            }

            instruction.exec()
            advance(instruction.nrOfInts)
        }
    }

    internal fun getOpcode(): Opcode = Opcode.byCode(getInstruction() % 100)

    internal fun getParam(paramNr: Int): Int {
        return when (getParamMode(paramNr)) {
            ParamMode.POSITION -> program[program[instructionPointer + paramNr]]
            ParamMode.IMMEDIATE -> program[instructionPointer + paramNr]
        }
    }

    internal fun getParamMode(paramNr: Int) = ParamMode.byCode((getInstruction() / 10.0.pow(paramNr + 1) % 10).toInt())

    internal fun getInstruction() = program[instructionPointer]

    protected open fun input(): Instruction = InputInstruction(this)

    protected open fun output(): Instruction = OutputInstruction(this)

    internal fun write(value: Int, targetParam: Int) {
        val targetAddress = program[instructionPointer + targetParam]
        program[targetAddress] = value
    }

    private fun advance(steps: Int) {
        instructionPointer += steps
    }

    override fun toString() = program.joinToString(",")
}

enum class Opcode(private val code: Int) {
    ADD(1),
    MULTIPLY(2),
    INPUT(3),
    OUTPUT(4),
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

abstract class Instruction(val nrOfInts: Int, protected val program: IntcodeProgram) {
    abstract fun exec()
}

class AddInstruction(program: IntcodeProgram) : Instruction(4, program) {
    override fun exec() {
        with(program) {
            write(getParam(1) + getParam(2), 3)
        }
    }
}

class MultiplyInstruction(program: IntcodeProgram) : Instruction(4, program) {
    override fun exec() {
        with(program) {
            write(getParam(1) * getParam(2), 3)
        }
    }
}

class InputInstruction(program: IntcodeProgram) : Instruction(2, program) {
    override fun exec() {
        with(program) {
            print("> ")
            write(readLine()!!.toInt(), 1)
        }
    }
}

class OutputInstruction(program: IntcodeProgram) : Instruction(2, program) {
    override fun exec() {
        with(program) {
            println(getParam(1))
        }
    }
}