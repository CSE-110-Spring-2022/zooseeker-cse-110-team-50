package edu.ucsd.cse110.zooseeker.Navigator;


import org.jgrapht.graph.DefaultWeightedEdge;

public class EdgeWithId extends DefaultWeightedEdge {
    public String edgeId;
    public EdgeWithId(String id) {
        edgeId = id;
    }
}
