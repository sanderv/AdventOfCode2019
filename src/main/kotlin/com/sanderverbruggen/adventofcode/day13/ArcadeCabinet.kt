package com.sanderverbruggen.adventofcode.day13

import com.sanderverbruggen.adventofcode.day13.BlockType.*
import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.sign

class ArcadeCabinet(intCode: String) {
    internal val program = IntcodeProgram(intCode)
    internal val grid = Array<Array<BlockType>>(26) { Array(42) { EMPTY } }
    var score = 0
    var paddlePos: Int? = null
    var ballPos: Int? = null
    fun run(): Int {
        runBlocking {
            launch { program.suspendedRun() }
            val out = program.outputChannel
            var running = true
            while (running) {
                val x = out.receive().toInt()
                val y = out.receive().toInt()
                val value = out.receive().toInt()

                if (x == -1) {
                    score = value
                    running = !cleared()
                } else {
                    val blockType = BlockType.byCode(value)
                    grid[y][x] = blockType
                    when (blockType) {
                        BALL -> ballPos = x
                        HORIZONTAL_PADDLE -> paddlePos = x
                    }
                }

                if (paddlePos != null && ballPos != null) {
                    printScreen()
                    val joystickMove = (ballPos!! - paddlePos!!).sign.toLong()
                    program.inputChannel.send(joystickMove)
                    ballPos = null
                }
            }
        }

        return score
    }

    fun insertQuarters(nrQuarters: Int) {
        program.program[0] = nrQuarters.toLong()
    }

    fun printScreen() {
        val screen = grid
                .map {
                    it
                            .map { it.display }
                            .joinToString("")
                }
                .joinToString("\n")
        println("\rScore: $score")
        print(screen)
    }

    fun cleared() = grid.flatMap { it.map { it.display } }.none { it == BLOCK.display }
}


enum class BlockType(private val code: Int, val display: Char) {
    EMPTY(0, ' '),
    WALL(1, '.'),
    BLOCK(2, '#'),
    HORIZONTAL_PADDLE(3, '_'),
    BALL(4, 'o');

    companion object {
        fun byCode(code: Int): BlockType = values().first { it.code == code }

    }
}