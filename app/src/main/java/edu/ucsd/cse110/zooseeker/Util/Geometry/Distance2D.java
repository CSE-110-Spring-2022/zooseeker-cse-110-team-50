package edu.ucsd.cse110.zooseeker.Util.Geometry;

import android.util.Pair;


public class Distance2D {
    public static double distance(Point2D p1, Point2D p2) {
        return Math.sqrt(
                Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)
        );
    }

    /**
     * Compute the distance between point 0 and the closest point on the line segment.
     * @param p0 the point
     * @param l the line segment created from two points
     * @return the distance between point 0 and the closest point on the line segment.
     */
    public static double distance(Point2D p0, Pair<Point2D, Point2D> l)
            throws IllegalArgumentException {
        double x0 = p0.x;
        double y0 = p0.y;
        Point2D p1 = l.first;
        Point2D p2 = l.second;

        if (p1.equals(p2))
            throw new IllegalArgumentException("The two points are the same");

        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;

        if (closestPointOnSegment(p0, l)) {
            return Math.abs(
                    (x2-x1)*(y1-y0) - (x1-x0)*(y2-y1)
            ) / distance(p1, p2);
        } else {
            // The closest point is one of the endpoints
            return Math.min(distance(p0, p1), distance(p0, p2));
        }
    }

    /**
     * Returns true if the closest point on the segment is in the interior of the segment.
     * @param p0 the point
     * @param l the line segment
     * @return true if the closest point on the segment is in the interior of the segment
     */
    public static boolean closestPointOnSegment(Point2D p0, Pair<Point2D, Point2D> l) {
        double x0 = p0.x;
        double y0 = p0.y;
        Point2D p1 = l.first;
        Point2D p2 = l.second;
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;

        double t = - ((x0-x1) * (x1-x2) + (y0-y1) * (y1-y2)) / Math.pow(distance(p1, p2), 2);

        return (t >= 0) && (t <= 1);
    }
}
