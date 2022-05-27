package edu.ucsd.cse110.zooseeker.Util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.util.Pair;

import org.junit.Test;

import edu.ucsd.cse110.zooseeker.Util.Geometry.Distance2D;
import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;

public class DistancePointLineTest {
    /**
     * Make sure distance throw an exception when p1 and p2
     * of the line segment are the same point.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDistancePointLineSegmentSamePoint() {
        double distance = Distance2D.distance(
                new Point2D(1, 1),
                new Pair<>(
                        new Point2D(0, 0),
                        new Point2D(0, 0)
                )
        );
    }

    /**
     * Test for closestPointOnSegment, which determines
     * whether a point (x, y) is on the line segment.
     */
    @Test
    public void testClosestPointOnSegment() {
        boolean onSegment = Distance2D.closestPointOnSegment(
                new Point2D(1, 1.5),
                new Pair<>(
                        new Point2D(0, 0),
                        new Point2D(2, 3)
                )
        );
        assertTrue(onSegment);

        // swap endpoints
        onSegment = Distance2D.closestPointOnSegment(
                new Point2D(1, 1.5),
                new Pair<>(
                        new Point2D(2, 3),
                        new Point2D(0, 0)
                )
        );
        assertTrue(onSegment);

        onSegment = Distance2D.closestPointOnSegment(
                new Point2D(-1, -1),
                new Pair<>(
                        new Point2D(-3, -2.5),
                        new Point2D(-1, -0.5)
                )
        );
        assertTrue(onSegment);

        // point is on an endpoint
        onSegment = Distance2D.closestPointOnSegment(
                new Point2D(-3, -2.5),
                new Pair<>(
                        new Point2D(-3, -2.5),
                        new Point2D(-1, -0.5)
                )
        );
        assertTrue(onSegment);
    }

    /**
     * Whether the point (x, y) is on the line segment.
     */
    @Test
    public void testClosestPointNotOnSegment() {
        boolean onSegment = Distance2D.closestPointOnSegment(
                new Point2D(1, 1),
                new Pair<>(
                        new Point2D(-3, -2.5),
                        new Point2D(-1, -0.5)
                )
        );
        assertFalse(onSegment);

        onSegment = Distance2D.closestPointOnSegment(
                new Point2D(-6, -4),
                new Pair<>(
                        new Point2D(-3, -5),
                        new Point2D(1, -1)
                )
        );
        assertFalse(onSegment);

        onSegment = Distance2D.closestPointOnSegment(
                new Point2D(-6, -4),
                new Pair<>(
                        new Point2D(-3, -5),
                        new Point2D(1, -1)
                )
        );
        assertFalse(onSegment);

        onSegment = Distance2D.closestPointOnSegment(
                new Point2D(2, 2),
                new Pair<>(
                        new Point2D(-5, 5),
                        new Point2D(-4, 4)
                )
        );
        assertFalse(onSegment);
    }

    /**
     * Distance between a line segment and a point on the line segment
     * (0 distance expected)
     */
    @Test
    public void testDistancePointOnLineSegment() {
        double distance = Distance2D.distance(
                new Point2D(1, 1.5),
                new Pair<>(
                        new Point2D(0, 0),
                        new Point2D(2, 3)
                )
        );
        assertEquals(0, distance, 0.001);

        // same, but switch p1 and p2
        distance= Distance2D.distance(
                new Point2D(1, 1.5),
                new Pair<>(
                        new Point2D(2, 3),
                        new Point2D(0, 0)

                )
        );
        assertEquals(0, distance, 0.001);

        // point as an endpoint
        distance= Distance2D.distance(
                new Point2D(2, 3),
                new Pair<>(
                        new Point2D(2, 3),
                        new Point2D(0, 0)

                )
        );
        assertEquals(0, distance, 0.001);

        distance= Distance2D.distance(
                new Point2D(0, 0),
                new Pair<>(
                        new Point2D(2, 3),
                        new Point2D(0, 0)

                )
        );
        assertEquals(0, distance, 0.001);
    }

    /**
     * Distance between a line segment and a point
     * to which the closest point is on the line segment
     */
    @Test
    public void testDistanceClosetPointOnLineSegment() {
        double distance = Distance2D.distance(
                new Point2D(-1, -1),
                new Pair<>(
                        new Point2D(-3, -2.5),
                        new Point2D(-1, -0.5)
                )
        );
        assertEquals(1 / (2 * Math.sqrt(2)), distance, 0.001);

        distance = Distance2D.distance(
                new Point2D(2, 2),
                new Pair<>(
                        new Point2D(1, 1),
                        new Point2D(3, 2)
                )
        );
        assertEquals(1 / (Math.sqrt(5)), distance, 0.001);

        distance = Distance2D.distance(
                new Point2D(2, 2),
                new Pair<>(
                        new Point2D(-1, -1),
                        new Point2D(3, 2)
                )
        );
        assertEquals(0.6, distance, 0.001);

        distance = Distance2D.distance(
                new Point2D(2, 2),
                new Pair<>(
                        new Point2D(-1, -1),
                        new Point2D(3, -2)
                )
        );
        assertEquals((double)15 / Math.sqrt((double) 17), distance, 0.001);
    }


    /**
     * Distance between a line segment and a point
     * to which the closest point is not on the line segment.
     * In this case, the closest point is one of the end points
     */
    @Test
    public void testDistancePointNotOnLineSegment() {
        double distance = Distance2D.distance(
                new Point2D(2, 2),
                new Pair<>(
                        new Point2D(-3, -2.5),
                        new Point2D(-1, 0.5)
                )
        );
        assertEquals(3*Math.sqrt(5) / 2, distance, 0.001);

        distance = Distance2D.distance(
                new Point2D(-6, -4),
                new Pair<>(
                        new Point2D(-3, -5),
                        new Point2D(1, -1)
                )
        );
        assertEquals(Math.sqrt(10), distance, 0.001);
    }
}
