package edu.ucsd.cse110.zooseeker.Util.Router;

import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;

public abstract class AbstractNode {
    String id, name;
    Double lat, lng;

    public AbstractNode(String id, String name, Double lat, Double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public Point2D getPoint2DCoord() {
        return new Point2D(lat, lng);
    }
}
