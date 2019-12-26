package com.sanderverbruggen.adventofcode.day16

class PatternGenerator(private val multiply: Int) {
    companion object {
        val basePattern = arrayListOf(0, 1, 0, -1)
    }

    val pattern: List<Int> = basePattern.flatMap { List(multiply) { _ -> it } }
    var position = if (multiply == 1) 0 else 1

    fun next(): Int {
        val result = pattern[position]
        position = ++position % pattern.size
        return result;
    }
}