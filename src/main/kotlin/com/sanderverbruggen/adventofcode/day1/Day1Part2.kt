package com.sanderverbruggen.adventofcode.day1

class Day1Part2 {
    fun calculateFuelNeeded(moduleWeigts: List<Int>): Int {
        return moduleWeigts
                .map { calcFuelWeight(it) }
                .sum()
    }

    private fun calcFuelWeight(weight: Int): Int {
        val fuelNeeded = weight / 3 - 2
        return if (fuelNeeded <= 0)
            0
        else
            calcFuelWeight(fuelNeeded) + fuelNeeded
    }
}