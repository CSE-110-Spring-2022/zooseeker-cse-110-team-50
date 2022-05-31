package edu.ucsd.cse110.zooseeker.Util.Router;

import android.util.Pair;

import org.jgrapht.graph.DefaultWeightedEdge;

import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;

import java.io.Serializable;

public class EdgeWithId extends DefaultWeightedEdge implements Serializable {
    public String id, name;
    public Double distance;
    public MetaNode source, target;

    public EdgeWithId(String id, MetaNode source, MetaNode target, Double distance) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    public EdgeWithId(String id, String name, MetaNode source, MetaNode target, Double distance) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    public Pair<Point2D, Point2D> getEdgeSegment() {
        return new Pair<>(source.getPoint2DCoord(), target.getPoint2DCoord());
    }

    public void setName(String name) {
        this.name = name;
    }
}
