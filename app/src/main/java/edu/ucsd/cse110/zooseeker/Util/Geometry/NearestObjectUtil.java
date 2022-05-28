package edu.ucsd.cse110.zooseeker.Util.Geometry;

import android.util.Pair;
import java.util.List;

public class NearestObjectUtil {

    /**
     * Finds the closest point from a list of points to a given point.
     * @param p given point p.
     * @param points list of points.
     * @return (distance, index)
     */
    public static Pair<Double, Integer> nearestPoint(Point2D p, List<Point2D> points) {
        int minIdx = -1;
        double minDist = Double.POSITIVE_INFINITY;

        for (int i = 0; i < points.size(); i++) {
            double d = Distance2D.distance(p, points.get(i));

            // If the distance is 0, then the point is the closest point.
            if (d == 0)
                return new Pair<>(0.0, i);

            if (d < minDist) {
                minDist = d;
                minIdx = i;
            }
        }
        return new Pair<>(minDist, minIdx);
    }

    /**
     * Finds the closest point from a list of line segments to a given point.
     * @param p given point p.
     * @param lineSegments list of line segments.
     * @return (distance, index)
     */
    public static Pair<Double, Integer> nearestLineSegment(
            Point2D p,
            List<Pair<Point2D, Point2D>> lineSegments) {
        int minIdx = -1;
        double minDist = Double.POSITIVE_INFINITY;

        for (int i = 0; i < lineSegments.size(); i++) {
            double d = Distance2D.distance(p, lineSegments.get(i));

            if (d == 0)
                return new Pair<>(0.0, i);

            if (d < minDist) {
                minDist = d;
                minIdx = i;
            }
        }
        return new Pair<>(minDist, minIdx);
    }
}
