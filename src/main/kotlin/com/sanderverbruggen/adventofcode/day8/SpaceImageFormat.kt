package com.sanderverbruggen.adventofcode.day8

class SpaceImageFormat(input: String, imageWidth: Int, imageHeight: Int) {
    val layers = input.chunked(imageWidth * imageHeight)

    fun nrLayers() = layers.size
}