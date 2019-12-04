package com.sanderverbruggen.adventofcode.day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3Test {
    @Test
    internal fun `segment should return crossing with distance from start`() {
        assertThat(HorizontalSegment(0, 5, 0).crossingDistanceFromStart(HorizontalSegment(0, 5, 0))).isZero()
        assertThat(HorizontalSegment(0, 5, 0).crossingDistanceFromStart(VerticalSegment(10, 0, 0))).isZero()
        assertThat(HorizontalSegment(0, 5, 0).crossingDistanceFromStart(VerticalSegment(2, 2, -2))).isEqualTo(2)
        assertThat(VerticalSegment(2, 2, -2).crossingDistanceFromStart(HorizontalSegment(0, 5, 0))).isEqualTo(2)
        assertThat(HorizontalSegment(0, 7, 10).crossingDistanceFromStart(VerticalSegment(4, 11, 2))).isEqualTo(14)
    }

    @Test
    internal fun `should draw a segment from instruction`() {
        val (segment, endPoint) = Segment.draw(Point(0, 0), "L5")
        assertThat(segment).isEqualToComparingFieldByField(HorizontalSegment(-5, 0, 0))
        assertThat(endPoint).isEqualToComparingFieldByField(Point(-5, 0))

        val (segment2, endPoint2) = Segment.draw(endPoint, "D8")
        assertThat(segment2).isEqualToComparingFieldByField(VerticalSegment(-5, 0, -8))
        assertThat(endPoint2).isEqualToComparingFieldByField(Point(-5, -8))
    }
}