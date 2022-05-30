package edu.ucsd.cse110.zooseeker.NewNavigator;


import org.jgrapht.graph.DefaultWeightedEdge;

public class EdgeWithId extends DefaultWeightedEdge {
    public String edgeId, name;

    public EdgeWithId(String id) {
        this.edgeId = id;
        this.name = null;
    }

    public EdgeWithId(String id, String name) {
        this.edgeId = id;
        this.name = name;
    }
}
