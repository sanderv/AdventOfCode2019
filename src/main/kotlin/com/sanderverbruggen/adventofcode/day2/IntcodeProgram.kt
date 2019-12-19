package com.sanderverbruggen.adventofcode.day2

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlin.math.pow

open class IntcodeProgram(
        programString: String,
        val inputChannel: Channel<Long> = Channel(4),
        val outputChannel: Channel<Long> = Channel(4)
) {
    internal val program = Memory(programString)
    internal var instructionPointer = 0
    internal var relativeBase = 0
    private var exitCode = 0L

    private var instructionSet = mutableMapOf(
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
        return exitCode
    }

    fun run(): Int = runBlocking {
        suspendedRun().toInt()
    }

    private suspend fun readInput(): Int {
        write(inputChannel.receive(), 1)
        return 2
    }

    private suspend fun sendOutput(): Int {
        val value = getParam(1)
        outputChannel.send(value)
        exitCode = value
        return 2
    }

    private fun getOpcode() = getRawInstruction() % 100

    internal fun getParamMode(paramNr: Int) = ParamMode.byCode((getRawInstruction() / 10.0.pow(paramNr + 1) % 10).toInt())

    private fun getRawInstruction() = program[instructionPointer]

    internal fun getParam(paramNr: Int): Long = program[getParamPointer(paramNr)]
    private fun getParamPointer(paramNr: Int): Int {
        return when (getParamMode(paramNr)) {
            ParamMode.POSITION -> program[instructionPointer + paramNr].toInt()
            ParamMode.IMMEDIATE -> instructionPointer + paramNr
            ParamMode.RELATIVE -> relativeBase + program[instructionPointer + paramNr].toInt()
        }
    }

    internal fun write(value: Long, targetParam: Int) {
        program[getParamPointer(targetParam)] = value
    }

    private fun advance(steps: Int) {
        instructionPointer += steps
    }

    fun memoryDump() = program.memoryDump()
    override fun toString() = program.toString()
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

class Memory(programString: String) {
    private val memory = programString
            .split(",")
            .mapIndexed { index, value -> index to value.toLong() }
            .toMap()
            .toMutableMap()

    operator fun get(index: Int) = memory.getOrDefault(index, 0)
    operator fun set(index: Int, value: Long) = memory.set(index, value)

    fun memoryDump(): List<Long> {
        return memory
                .entries
                .sortedBy { it.key }
                .map { it.value }
    }

    override fun toString() = memoryDump()
            .joinToString(",")
}