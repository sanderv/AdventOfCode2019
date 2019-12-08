package com.sanderverbruggen.adventofcode.day4

class PasswordCracker {

    fun solve(passwordRange: IntRange): Int {
        return passwordRange
                .filter { matchesFacts(it) }
                .count()
    }

    internal fun matchesFacts(password: Int): Boolean {
        return hasDoubles(password) && !decreases(password)
    }

    private fun hasDoubles(password: Int): Boolean {
        var lastSeen = 0
        var foundDouble = false
        password.toString().chars().forEach {
            if (it == lastSeen)
                foundDouble = true
            else
                lastSeen = it
        }

        return foundDouble
    }

    private fun decreases(password: Int): Boolean {
        var lastSeen = 0
        var decreasing = false
        password.toString().chars().forEach {
            if (it >= lastSeen)
                lastSeen = it
            else
                decreasing = true
        }

        return decreasing
    }
}
