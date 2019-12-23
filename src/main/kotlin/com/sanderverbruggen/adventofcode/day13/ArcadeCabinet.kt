package com.sanderverbruggen.adventofcode.day13

import com.sanderverbruggen.adventofcode.day2.IntcodeProgram
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ArcadeCabinet(intCode: String) {
    internal val program = IntcodeProgram(intCode)
    internal val grid: MutableList<Tile> = mutableListOf()

    fun run() {
        runBlocking {
            val job = launch { program.suspendedRun() }
            val out = program.outputChannel
            var currentY = 0L
            while (job.isActive) {
                val x = out.receive()
                val y = out.receive()
                val value = out.receive()

                val tile = Tile(x, y, BlockType.byCode(value))
                if (y != currentY) {
                    println()
                    currentY = y
                }
                print(tile.id.display)

                grid.add(tile)
            }
        }
    }

    fun insertQuarters(nrQuarters: Int) {
        program.program[0] = nrQuarters.toLong()
    }
}


data class Tile(val x: Long, val y: Long, val id: BlockType)

enum class BlockType(private val code: Long, val display: Char) {
    EMPTY(0, ' '),
    WALL(1, '.'),
    BLOCK(2, '#'),
    HORIZONTAL_PADDLE(3, '_'),
    BALL(4, 'o');

    companion object {
        fun byCode(code: Long): BlockType = values().first { it.code == code }

    }
}