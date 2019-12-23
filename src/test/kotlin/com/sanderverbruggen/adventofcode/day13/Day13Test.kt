package com.sanderverbruggen.adventofcode.day13

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
        // The IntcodeProgram doesn't exit (as intended)
    }

    @Test
    internal fun `part 2 solution`() {
        val arcadeCabinet = ArcadeCabinet(readFile("day13/input.txt"))
        arcadeCabinet.insertQuarters(2)
        val score = arcadeCabinet.run()
        assertThat(score).isEqualTo(18647)
    }
}