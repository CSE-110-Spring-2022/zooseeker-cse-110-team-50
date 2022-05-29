package edu.ucsd.cse110.zooseeker.Util;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Util.Geometry.NearestObjectUtil;
import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;

@RunWith(AndroidJUnit4.class)
public class NearestPointTest {
    /**
     * point is not in the list
     */
    @Test
    public void testNearestPointNotInList() {
        Point2D point = new Point2D(3, 1);
        List<Point2D> listOfPoints = Arrays.asList(
                new Point2D(1, 2),
                new Point2D(2.5, 3),  // answer
                new Point2D(-1, 10),
                new Point2D(2, 8),
                new Point2D(3, -6),
                new Point2D(-3, -2)
        );
        Pair<Double, Integer> ans = NearestObjectUtil.nearestPoint(point, listOfPoints);
        Pair<Double, Integer> expected = new Pair<>(Math.sqrt(17) / 2, 1);
        assertEquals(expected.first, ans.first, 0.001);
        assertEquals(expected.second, ans.second);
    }

    /**
     * point is in the list
     */
    @Test
    public void testNearestPointInList() {
        Point2D point = new Point2D(3, 1);
        List<Point2D> listOfPoints = Arrays.asList(
                new Point2D(1, 2),
                new Point2D(2.5, 3),
                new Point2D(-1, 10),
                new Point2D(2, 8),
                new Point2D(3, -6),
                new Point2D(3, 1),  // answer
                new Point2D(-3, -2)
        );
        Pair<Double, Integer> ans = NearestObjectUtil.nearestPoint(point, listOfPoints);
        Pair<Double, Integer> expected = new Pair<>(0.0, 5);
        assertEquals(expected.first, ans.first, 0.001);
        assertEquals(expected.second, ans.second);
    }

    /**
     * list is empty
     */
    @Test
    public void testNearestPointEmptyList() {
        Point2D point = new Point2D(3, 1);
        List<Point2D> listOfPoints = new ArrayList<>();
        Pair<Double, Integer> ans = NearestObjectUtil.nearestPoint(point, listOfPoints);
        Pair<Double, Integer> expected = new Pair<>(Double.POSITIVE_INFINITY, -1);
        assertEquals(expected.first, ans.first, 0.001);
        assertEquals(expected.second, ans.second);
    }

}
