package edu.ucsd.cse110.zooseeker.Navigator;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import edu.ucsd.cse110.zooseeker.NewNavigator.EdgeWithId;
import edu.ucsd.cse110.zooseeker.Util.Router.RawGraph;

public class GraphLoader {

    public static Graph<String, EdgeWithId> loadFromRawGraph(RawGraph rawGraph){
        Graph<String, EdgeWithId> graph = new DefaultUndirectedWeightedGraph<>(EdgeWithId.class);
        rawGraph.nodes.stream().forEach((node) -> {
            graph.addVertex(node.id);
        });

        rawGraph.edges.stream().forEach((edge) -> {
            EdgeWithId newEdge = new EdgeWithId(edge.id);
            graph.addEdge(edge.source, edge.target, newEdge);
            graph.setEdgeWeight(newEdge, edge.weight);
        });
        return graph;
    }

}
