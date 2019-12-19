package com.sanderverbruggen.adventofcode.day8

import com.sanderverbruggen.adventofcode.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day8Test {
    private val input = readFile("day8/input.txt")
    @Test
    internal fun `should create layers from input`() {
        val spaceImageFormat = SpaceImageFormat("123456789012", 3, 2)
        assertThat(spaceImageFormat.nrLayers()).isEqualTo(2)
        assertThat(spaceImageFormat.layers[0]).isEqualTo("123456")
        assertThat(spaceImageFormat.layers[1]).isEqualTo("789012")
    }

    @Test
    internal fun `day 8 part 1 solution should be 1224`() {
        val spaceImageFormat = SpaceImageFormat(input, 25, 6)
        val answer = spaceImageFormat.layers
                .map { layer ->
                    val count0s = layer.count { it == '0' }
                    val count1s = layer.count { it == '1' }
                    val count2s = layer.count { it == '2' }
                    count0s to count1s * count2s
                }
                .minBy { it.first }
                ?.second ?: -1
        assertThat(answer).isEqualTo(1224)
    }

    @Test
    internal fun `should render sample image`() {
        val spaceImageFormat = SpaceImageFormat("0222112222120000", 2, 2)
        val image = spaceImageFormat.getImage()

        assertThat(image).isEqualTo("01\n10")
    }

    @Test
    internal fun `day 8 part 2 solution should be`() {
        val answer = SpaceImageFormat(input, 25, 6).getImage()
        println(answer.replace('0', ' '))
    }
}