package edu.ucsd.cse110.zooseeker.Util.Router;

import org.jgrapht.graph.DefaultWeightedEdge;

public class EdgeWithId extends DefaultWeightedEdge {
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

    public void setName(String name) {
        this.name = name;
    }
}
