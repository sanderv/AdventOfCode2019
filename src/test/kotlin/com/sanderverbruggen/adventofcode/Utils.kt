package com.sanderverbruggen.adventofcode

fun readFile(filename: String) = Dummy::class.java.classLoader.getResource(filename).readText()

class Dummy