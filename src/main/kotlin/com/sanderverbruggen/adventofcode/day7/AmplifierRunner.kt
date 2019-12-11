package com.sanderverbruggen.adventofcode.day7

class AmplifierRunner(private val intCode: String) {
    fun solve(): Int {
        return permute(listOf(0, 1, 2, 3, 4))
                .map { runAmpChain(it) }
                .max() ?: -1
    }

    internal fun runAmpChain(inputs: List<Int>): Int {
        var ampOutput = 0
        inputs.map {
            ampOutput = MultiInputRunner(intCode, listOf(it, ampOutput)).run()
        }
        println("$inputs -> $ampOutput")
        return ampOutput
    }
}