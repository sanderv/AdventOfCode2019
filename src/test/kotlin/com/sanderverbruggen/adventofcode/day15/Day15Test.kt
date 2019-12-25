package com.sanderverbruggen.adventofcode.day15

import com.sanderverbruggen.adventofcode.readFile
import org.junit.jupiter.api.Test

class Day15Test {
    val intCode = readFile("day15/input.txt")

    @Test
    internal fun `part 1 solution should be`() {
        val repairDroid = RepairDroid(intCode)
        repairDroid.solve()
    }
}