package com.sanderverbruggen.adventofcode.day13

import com.sanderverbruggen.adventofcode.day13.BlockType.BLOCK
import com.sanderverbruggen.adventofcode.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day13Test {
    @Test
    @Disabled
    internal fun `part 1 solution`() {
        val arcadeCabinet = ArcadeCabinet(readFile("day13/input.txt"))
        arcadeCabinet.run()
        val nrBlocks = arcadeCabinet.grid
                .count { it.id == BLOCK }

        // The IntcodeProgram doesn't exit (as intended)
        assertThat(nrBlocks).isEqualTo(0)
    }

    @Test
    internal fun `part 2 solution`() {
        val arcadeCabinet = ArcadeCabinet(readFile("day13/input.txt"))
        arcadeCabinet.insertQuarters(2)
        arcadeCabinet.run()
    }
}