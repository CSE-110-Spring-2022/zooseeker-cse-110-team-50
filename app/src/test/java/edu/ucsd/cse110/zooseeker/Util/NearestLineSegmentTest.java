package edu.ucsd.cse110.zooseeker.Util;

import static org.junit.Assert.assertEquals;

import android.util.Pair;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Util.Geometry.NearestObjectUtil;
import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;

@RunWith(AndroidJUnit4.class)
public class NearestLineSegmentTest {
    /**
     * When the point is not on any line segment.
     */
    @Test
    public void testPointNotOnSegments() {
        Point2D point = new Point2D(3, 1);
        List<Pair<Point2D, Point2D>> segments = Arrays.asList(
                new Pair<>(new Point2D(0, 0), new Point2D(4, 0)),
                new Pair<>(new Point2D(-2, -3), new Point2D(3, -1)),
                new Pair<>(new Point2D(1, -1), new Point2D(4, 10))
        );
        Pair<Double, Integer> answer = NearestObjectUtil.nearestLineSegment(point, segments);
        Pair<Double, Integer> expected = new Pair<>(1.0, 0);
        assertEquals(expected.first, answer.first, 0.001);
        assertEquals(expected.second, answer.second);
    }

    /**
     * When the point is not on a line segment.
     */
    @Test
    public void testPointOnSegments() {
        Point2D point = new Point2D(3, 1);
        List<Pair<Point2D, Point2D>> segments = Arrays.asList(
                new Pair<>(new Point2D(3, 1), new Point2D(4, 0)),  // answer
                new Pair<>(new Point2D(-2, -3), new Point2D(3, -1)),
                new Pair<>(new Point2D(1, -1), new Point2D(4, 10))
        );
        Pair<Double, Integer> answer = NearestObjectUtil.nearestLineSegment(point, segments);
        Pair<Double, Integer> expected = new Pair<>(0.0, 0);
        assertEquals(expected.first, answer.first, 0.001);
        assertEquals(expected.second, answer.second);
    }

    /**
     * When the list of segments is empty.
     */
    @Test
    public void testPointEmptyList() {
        Point2D point = new Point2D(3, 1);
        List<Pair<Point2D, Point2D>> segments = new ArrayList<>();
        Pair<Double, Integer> answer = NearestObjectUtil.nearestLineSegment(point, segments);
        Pair<Double, Integer> expected = new Pair<>(Double.POSITIVE_INFINITY, -1);
        assertEquals(expected.first, answer.first, 0.001);
        assertEquals(expected.second, answer.second);
    }
}
