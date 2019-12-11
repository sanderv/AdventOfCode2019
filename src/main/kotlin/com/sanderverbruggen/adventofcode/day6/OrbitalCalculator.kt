package com.sanderverbruggen.adventofcode.day6

class OrbitalCalculator {
    internal val objectsMap: MutableMap<String, SpaceObject> = mutableMapOf()

    constructor(input: String) {
        input.lines()
                .map { inputLine ->
                    val centerName = inputLine.split(')')[0]
                    val orbitName = inputLine.split(')')[1]
                    Pair(centerName, orbitName)
                }
                .forEach { spaceObject ->
                    addToMap(spaceObject)
                }

        getCOM().distanceFromCOM = 0
    }

    fun getNrDirectAndIndirectOrbits(): Int {
        return getCOM().getNrDirectAndIndirectOrbits()
    }

    internal fun getCOM() = objectsMap["COM"]!!

    internal fun addToMap(namePair: Pair<String, String>) {
        val centerObject = objectsMap.getOrPut(namePair.first, { SpaceObject(namePair.first) })
        val orbitObject = objectsMap.getOrPut(namePair.second, { SpaceObject(namePair.second) })
        centerObject.addOrbiter(orbitObject)
        orbitObject.orbiting = centerObject
    }

    fun getNrOrbitTransfersToSAN(): Int {
        val you = objectsMap["YOU"]!!
        val san = objectsMap["SAN"]!!
        you.onPath = true
        san.onPath = true

        val commonObject = objectsMap.values
                .find { it.collision }!!

        return you.orbiting!!.distanceFromCOM - commonObject.distanceFromCOM + san.orbiting!!.distanceFromCOM - commonObject.distanceFromCOM
    }

}

class SpaceObject(val name: String) {
    val objectsInOrbit: MutableList<SpaceObject> = mutableListOf()
    var orbiting: SpaceObject? = null
    var onPath: Boolean = false
        set(value) {
            field = value
            if (objectsInOrbit.filter { it.onPath }.count() == 2)
                collision = true
            orbiting?.let { it.onPath = value }
        }
    var collision = false
    var distanceFromCOM = 0
        set(value) {
            field = value
            objectsInOrbit.forEach { it.distanceFromCOM = value + 1 }
        }

    fun addOrbiter(orbitingObject: SpaceObject) {
        objectsInOrbit.add(orbitingObject)
    }

    fun getNrDirectAndIndirectOrbits(): Int =
            objectsInOrbit
                    .map { it.getNrDirectAndIndirectOrbits() }
                    .sum() + distanceFromCOM

    override fun toString(): String {
        return "{ $name: { $objectsInOrbit } }"
    }

}