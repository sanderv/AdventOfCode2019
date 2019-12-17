package com.sanderverbruggen.adventofcode.day7

import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AmplifierRunner(private val intCode: String) {

    fun solve(): Int {
        return permute(listOf(0, 1, 2, 3, 4))
                .map { runAmpChain(it) }
                .max() ?: -1
    }

    fun solveLoopback(): Int {
        return permute(listOf(5, 6, 7, 8, 9))
                .map { runAmpChain(it) }
                .max() ?: -1
    }


    internal fun runAmpChain(phases: List<Int>): Int {
        return runBlocking {
            val channels = phases.map { phase -> Channel<Int>(4).apply { send(phase) } }.toMutableList()
            channels.first().send(0) // initial input
            runBlocking {
                for (index in 0 until phases.size) {
                    launch {
                        val outputIndex = if (index == phases.size - 1) 0 else index + 1
                        val program = IntcodeProgram(intCode, channels[index], channels[outputIndex])
                        program.suspendedRun()
                    }
                }
            }

            channels.first().receive()
        }
    }
}