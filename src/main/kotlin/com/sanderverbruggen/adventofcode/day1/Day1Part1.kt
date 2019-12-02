package com.sanderverbruggen.adventofcode.day1

class Day1Part1 {
    fun calculateFuelNeeded(moduleWeigts: List<Int>): Int {
        return moduleWeigts
                .map { it / 3 - 2 }
                .sum()
    }
}
