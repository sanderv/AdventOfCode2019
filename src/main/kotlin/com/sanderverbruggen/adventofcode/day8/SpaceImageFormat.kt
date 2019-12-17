package com.sanderverbruggen.adventofcode.day8

class SpaceImageFormat(input: String, private val imageWidth: Int, imageHeight: Int) {
    companion object {
        val BLACK = '0'
        val WHITE = '1'
        val TRANSPARENT = '2'
    }

    val layers = input.chunked(imageWidth * imageHeight)

    fun nrLayers() = layers.size

    fun getImage(): String {
        var image = ""
        for (i in 0 until layers[0].length) {
            image += layers
                    .map { it[i] }
                    .filter { it != TRANSPARENT }
                    .first()
        }

        return image.chunked(imageWidth).joinToString("\n")
    }
}