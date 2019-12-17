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

    internal fun runAmpChain(phases: List<Int>): Int {
        return runBlocking {
            val channels = phases.map { phase -> Channel<Int>(4).apply { send(phase) } }.toMutableList()
            channels.add(Channel<Int>(4)) // the output channel
            channels.first().send(0) // initial input
            for (index in 0 until phases.size) {
                launch {
                    val program = IntcodeProgram(intCode, channels[index], channels[index + 1])
                    program.suspendedRun()
                }
            }

            channels.last().receive()
        }
    }
}