package edu.ucsd.cse110.zooseeker.Util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.graphics.Point;
import android.util.Pair;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.zooseeker.Util.Geometry.Distance2D;
import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;

@RunWith(AndroidJUnit4.class)
public class DistancePointPointTest {
    /**
     * Distance between (0,0) and (1,1)
     */
    @Test
    public void testDistancePointPoint() {
        double distance = Distance2D.distance(
                new Point2D(0, 0),
                new Point2D(1, 1)
        );
        assertEquals(Math.sqrt(2), distance, 0.001);
    }

    /**
     * Distance between (0,0) and (-3,-4)
     */
    @Test
    public void testDistancePointPointXYNegative() {
        double distance = Distance2D.distance(
                new Point2D(0, 0),
                new Point2D(-3, -4)
        );
        assertEquals(5, distance, 0.001);
    }

    /**
     * Distance between (0,0) and (-3,4)
     */
    @Test
    public void testDistancePointPointXNegative() {
        double distance = Distance2D.distance(
                new Point2D(0, 0),
                new Point2D(-3, 4)
        );
        assertEquals(5, distance, 0.001);
    }

    /**
     * Distance between (0,0) and (3,-4)
     */
    @Test
    public void testDistancePointPointYNegative() {
        double distance = Distance2D.distance(
                new Point2D(0, 0),
                new Point2D(-3, 4)
        );
        assertEquals(5, distance, 0.001);
    }
}
