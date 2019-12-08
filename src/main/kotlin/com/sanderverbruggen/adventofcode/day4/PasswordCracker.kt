package com.sanderverbruggen.adventofcode.day4

class PasswordCracker {

    fun solve(passwordRange: IntRange, stricterFacts: Boolean = false): Int {
        return passwordRange
                .map { it.toString() }
                .filter { if (stricterFacts) matchesStricterFacts(it) else matchesSimpleFacts(it) }
                .count()
    }

    internal fun matchesSimpleFacts(password: String): Boolean {
        return hasDoubles(password) && neverDecreases(password)
    }

    internal fun matchesStricterFacts(password: String): Boolean {
        return hasExactDouble(password) && neverDecreases(password)
    }

    private fun hasDoubles(password: String): Boolean {
        return splitPasswordIntoChars(password) // split() adds an empty string at the start and end
                .groupBy { it }
                .any { it.value.size >= 2 }
    }

    private fun hasExactDouble(password: String): Boolean {
        return splitPasswordIntoChars(password) // split() adds an empty string at the start and end
                .groupBy { it }
                .any { it.value.size == 2 }
    }

    private fun neverDecreases(password: String): Boolean {
        return splitPasswordIntoChars(password)
                .sorted()
                .joinToString("") == password
    }

    private fun splitPasswordIntoChars(password: String) = password.split("")
            .drop(1).dropLast(1)
}
