package com.sanderverbruggen.adventofcode.day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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

    @ParameterizedTest
    @CsvSource(
            " R8,U5,L5,D3 | U7,R6,D4,L4 | 6 ",
            " R75,D30,R83,U83,L12,D49,R71,U7,L72 | U62,R66,U55,R34,D71,R55,D58,R83 | 159 ",
            " R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51 | U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 | 135 "
            , delimiter = '|'
    )
    internal fun `should detect crossing in paths`(path1: String, path2: String, expectedDistance: Int) {
        assertThat(Map(path1, path2).findNearestCrossingDistance()).isEqualTo(expectedDistance)
    }
}