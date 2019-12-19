package com.sanderverbruggen.adventofcode.day2

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlin.math.pow

open class IntcodeProgram(
        programString: String,
        val inputChannel: Channel<Long> = Channel(4),
        val outputChannel: Channel<Long> = Channel(4)
) {
    internal val program = programString.split(",").map { it.toLong() }.toLongArray()
    internal var instructionPointer = 0
    internal var relativeBase = 0
    var exitCode = 0L

    internal var instructionSet = mutableMapOf(
            1L to AddInstruction(this),
            2L to MultiplyInstruction(this),
            5L to JumpIfTrueInstruction(this),
            6L to JumpIfFalseInstruction(this),
            7L to LessThanInstruction(this),
            8L to EqualsInstruction(this),
            9L to AdjustRelativeBaseInstruction(this)
    )

    suspend fun suspendedRun(): Long {
        while (getOpcode() != 99L) {
            val skipInts = when (getOpcode()) {
                3L -> readInput()
                4L -> sendOutput()
                else -> {
                    val instruction = instructionSet[getOpcode()]
                            ?: throw RuntimeException("Unknown opcode ${getOpcode()}")
                    instruction.exec()
                    instruction.skipInts
                }
            }
            advance(skipInts)
        }
        inputChannel.close()
        outputChannel.close()
        return exitCode
    }

    fun run(): Int = runBlocking {
        suspendedRun().toInt()
    }

    internal suspend fun readInput(): Int {
        write(inputChannel.receive(), 1)
        return 2
    }

    internal suspend fun sendOutput(): Int {
        val value = getParam(1)
        outputChannel.send(value)
        exitCode = value
        return 2
    }

    internal fun getOpcode() = getRawInstruction() % 100

    internal fun getParamMode(paramNr: Int) = ParamMode.byCode((getRawInstruction() / 10.0.pow(paramNr + 1) % 10).toInt())

    internal fun getRawInstruction() = program[instructionPointer]

    internal fun getParam(paramNr: Int): Long {
        return when (getParamMode(paramNr)) {
            ParamMode.POSITION -> program[program[instructionPointer + paramNr].toInt()]
            ParamMode.IMMEDIATE -> program[instructionPointer + paramNr]
            ParamMode.RELATIVE -> program[relativeBase + program[instructionPointer + paramNr].toInt()]
        }
    }

    internal fun write(value: Long, targetParam: Int) {
        val targetAddress = program[instructionPointer + targetParam].toInt()
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
    JUMP_IF_TRUE(5),
    JUMP_IF_FALSE(6),
    END(99);

    companion object {
        fun byCode(code: Int) = values().first { it.code == code }
    }
}

enum class ParamMode(val code: Int) {
    POSITION(0), IMMEDIATE(1), RELATIVE(2);

    companion object {
        fun byCode(code: Int) = values().first { it.code == code }
    }
}

abstract class Instruction(var skipInts: Int, protected val program: IntcodeProgram) {
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

class JumpIfTrueInstruction(program: IntcodeProgram) : Instruction(3, program) {
    override fun exec() {
        with(program) {
            if (getParam(1) != 0L) {
                instructionPointer = getParam(2).toInt()
                skipInts = 0 // already jumped
            } else {
                skipInts = 3 // no jump, advance to next instruction
            }
        }
    }
}

class JumpIfFalseInstruction(program: IntcodeProgram) : Instruction(3, program) {
    override fun exec() {
        with(program) {
            if (getParam(1) == 0L) {
                instructionPointer = getParam(2).toInt()
                skipInts = 0 // already jumped
            } else {
                skipInts = 3 // no jump, advance to next instruction
            }
        }
    }
}

class LessThanInstruction(program: IntcodeProgram) : Instruction(4, program) {
    override fun exec() {
        with(program) {
            val value = if (getParam(1) < getParam(2)) 1L else 0L
            write(value, 3)
        }
    }
}

class EqualsInstruction(program: IntcodeProgram) : Instruction(4, program) {
    override fun exec() {
        with(program) {
            val value = if (getParam(1) == getParam(2)) 1L else 0L
            write(value, 3)
        }
    }
}

class AdjustRelativeBaseInstruction(program: IntcodeProgram) : Instruction(2, program) {
    override fun exec() {
        with(program) {
            relativeBase += getParam(1).toInt()
        }
    }
}