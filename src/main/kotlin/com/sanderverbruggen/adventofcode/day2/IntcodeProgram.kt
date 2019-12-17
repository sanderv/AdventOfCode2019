package com.sanderverbruggen.adventofcode.day2

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.pow

open class IntcodeProgram(
        programString: String,
        private val inputChannel: Channel<Int> = Channel(),
        private val outputChannel: Channel<Int> = Channel()
) {
    constructor(programString: String, inputValue: Int) : this(programString, Channel()) {
        runBlocking {
            launch {
                inputChannel.send(inputValue)
            }
        }
    }

    internal val program = programString.split(",").map { it.toInt() }.toIntArray()
    var exitCode = 0

    internal var instructionSet = mutableMapOf(
            1 to AddInstruction(this),
            2 to MultiplyInstruction(this),
            5 to JumpIfTrueInstruction(this),
            6 to JumpIfFalseInstruction(this),
            7 to LessThanInstruction(this),
            8 to EqualsInstruction(this)
    )

    internal var instructionPointer = 0

    suspend fun suspendedRun(): Int {
        while (getOpcode() != 99) {
            val skipInts = when (getOpcode()) {
                3 -> readInput()
                4 -> sendOutput()
                else -> {
                    val instruction = instructionSet[getOpcode()]
                            ?: throw RuntimeException("Unknown opcode ${getOpcode()}")
                    instruction.exec()
                    instruction.skipInts
                }
            }
            advance(skipInts)
        }
        println("Exited")
        return exitCode
    }

    fun run(): Int = runBlocking {
        suspendedRun()
    }

    internal suspend fun readInput(): Int {
        write(inputChannel.receive(), 1)
        return 2
    }

    internal suspend fun sendOutput(): Int {
        val value = getParam(1)
        println("Sending $value")
        outputChannel.send(value)
        exitCode = value
        println("Sent $value")
        return 2
    }

    internal fun getOpcode() = getRawInstruction() % 100

    internal fun getParamMode(paramNr: Int) = ParamMode.byCode((getRawInstruction() / 10.0.pow(paramNr + 1) % 10).toInt())

    internal fun getRawInstruction() = program[instructionPointer]

    internal fun getParam(paramNr: Int): Int {
        return when (getParamMode(paramNr)) {
            ParamMode.POSITION -> program[program[instructionPointer + paramNr]]
            ParamMode.IMMEDIATE -> program[instructionPointer + paramNr]
        }
    }

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
    JUMP_IF_TRUE(5),
    JUMP_IF_FALSE(6),
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
            if (getParam(1) != 0) {
                instructionPointer = getParam(2)
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
            if (getParam(1) == 0) {
                instructionPointer = getParam(2)
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
            val value = if (getParam(1) < getParam(2)) 1 else 0
            write(value, 3)
        }
    }
}

class EqualsInstruction(program: IntcodeProgram) : Instruction(4, program) {
    override fun exec() {
        with(program) {
            val value = if (getParam(1) == getParam(2)) 1 else 0
            write(value, 3)
        }
    }
}