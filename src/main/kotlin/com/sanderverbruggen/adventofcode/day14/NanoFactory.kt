package com.sanderverbruggen.adventofcode.day14

import kotlin.math.sign

class NanoFactory(input: String, requestedFuel: Int) {
    val formulas = parseFormulas(input)
    var neededChemicals = listOf(Chemical(requestedFuel.toLong(), Chemical.FUEL))

    internal fun parseFormulas(input: String): MutableMap<Chemical, Formula> {
        return input.lines()
                .map { Formula.parse(it) }
                .map { it.chemical to it }
                .toMap()
                .toMutableMap()
    }

    fun solve(): Long {
        while (!solved()) {
            val substituted = mutableListOf<Chemical>()
            neededChemicals = neededChemicals
                    .flatMap { neededChemical ->
                        if (canBeSubstituted(neededChemical)) {
                            substituted.add(neededChemical)
                            val formula = formulas[neededChemical]!!
                            formula.materials
                                    .map { sourceChemical ->
                                        Chemical(sourceChemical.quantity * nrBatches(neededChemical.quantity, formula.chemical.quantity), sourceChemical.name)
                                    }
                        } else {
                            listOf(neededChemical)
                        }
                    }
                    .groupBy { it.name }
                    .map { (name, chemicals) -> Chemical(chemicals.map { it.quantity }.sum(), name) }
            substituted.forEach { formulas.remove(it) }
        }

        return neededChemicals.first().quantity
    }

    private fun solved() = neededChemicals.size == 1 && neededChemicals.first().name == Chemical.ORE
    private fun canBeSubstituted(chemical: Chemical): Boolean = !formulas.values.flatMap { it.materials }.any { it == chemical } && chemical.name != Chemical.ORE
    private fun nrBatches(required: Long, batchSize: Long) = ((required / batchSize) + (required % batchSize).sign)
}

class Chemical(
        val quantity: Long,
        val name: String
) {
    companion object {
        const val ORE = "ORE"
        const val FUEL = "FUEL"
    }

    override fun toString() = "$quantity $name"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chemical

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class Formula(
        val materials: List<Chemical>,
        val chemical: Chemical
) {
    companion object {
        fun parse(input: String): Formula {
            val elements = input.trim().split("=>")
            return Formula(parseMaterials(elements[0]), parseChemical(elements[1]))
        }

        private fun parseChemical(input: String): Chemical {
            val elements = input.trim().split(" ")
            return Chemical(elements[0].toLong(), elements[1].trim())
        }

        private fun parseMaterials(input: String): List<Chemical> {
            return input.trim().split(",")
                    .map { parseChemical(it) }

        }
    }

    override fun toString(): String {
        return "$materials => $chemical)"
    }


}