package edu.ucsd.cse110.zooseeker.Util.Router;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;

public class MetaNode {

    String id, name, kind;
    Double lat, lng;
    List<String> nodeIds;

    public MetaNode(String id, String name, String kind, Double lat, Double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.kind = kind;
        this.nodeIds = new ArrayList<>();
    }

    public boolean addNode(String nodeId) {
        if (nodeIds.contains(nodeId))
            return false;
        this.nodeIds.add(nodeId);
        return true;
    }

    public Point2D getPoint2DCoord() {
        return new Point2D(lat, lng);
    }

    public boolean equals(MetaNode other) {
        return this.id.equals(other.id);
    }


}
