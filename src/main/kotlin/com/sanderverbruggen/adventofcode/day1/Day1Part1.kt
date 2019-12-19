package com.sanderverbruggen.adventofcode.day1

class Day1Part1 {
    fun calculateFuelNeeded(moduleWeights: List<Int>): Int {
        return moduleWeights
                .map { it / 3 - 2 }
                .sum()
    }
}
