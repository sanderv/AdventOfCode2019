package com.sanderverbruggen.adventofcode.day16

class PatternGenerator(private val multiply: Int) {
    companion object {
        val basePattern = arrayListOf(0, 1, 0, -1)
    }

    var position = if (multiply == 1) 0 else 1

    fun next(): Int {
        val result = basePattern[position / multiply]
        position = ++position % (basePattern.size * multiply)
        return result;
    }
}