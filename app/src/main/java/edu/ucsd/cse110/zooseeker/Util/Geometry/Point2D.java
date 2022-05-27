package edu.ucsd.cse110.zooseeker.Util.Geometry;

public class Point2D {
    public double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Point2D other) {
        return (this.x == other.x) && (this.y == other.y);
    }

    @Override
    public String toString() {
        return "Point2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

