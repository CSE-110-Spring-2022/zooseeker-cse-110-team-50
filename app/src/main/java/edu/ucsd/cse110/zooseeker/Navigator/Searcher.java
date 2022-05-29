package edu.ucsd.cse110.zooseeker.Navigator;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.ucsd.cse110.zooseeker.Util.Router.RawGraph;

public class Searcher {
    Graph<String, EdgeWithId> graph;
    Map<String, String> edgeInfo;
    Map<String, String> placeInfo;

    public Searcher builder(){ return new Searcher(); }

    public Searcher build(){ return this; }

    public Searcher loadEdgeInfo(Map<String, String> edgeInfo){
        this.edgeInfo = edgeInfo;
        return this;
    }

    public Searcher loadPlaceInfo(Map<String, String> placeInfo){
        this.placeInfo = placeInfo;
        return this;
    }

    public Searcher loadFromRawGraph(RawGraph rawGraph){
        graph = new DefaultUndirectedWeightedGraph<>(EdgeWithId.class);
        rawGraph.nodes.stream().forEach((node) -> {
            graph.addVertex(node.id);
        });

        rawGraph.edges.stream().forEach((edge) -> {
            EdgeWithId newEdge = new EdgeWithId(edge.id);
            graph.addEdge(edge.source, edge.target, newEdge);
            graph.setEdgeWeight(newEdge, edge.weight);
        });
        return this;
    }

    public GraphPath<String, EdgeWithId> shortestPath(String source, String target) {
        DijkstraShortestPath<String, EdgeWithId> dijkstraAlg =
                new DijkstraShortestPath<>(graph);
        ShortestPathAlgorithm.SingleSourcePaths<String, EdgeWithId> iPaths = dijkstraAlg.getPaths(source);
        return iPaths.getPath(target);
    }

    public GraphPath<String, EdgeWithId> getPathToClosestNode(String source, List<String> targets) {
        PriorityQueue<NodeWithDist> pq = new PriorityQueue<>((x, y) -> (int)(x.distance - y.distance));
        GraphPath<String, EdgeWithId> shortestPath = shortestPath(source, targets.get(0));
        GraphPath<String, EdgeWithId> newPath;
        for (String target : targets) {
            newPath = shortestPath(source, target);
            if (newPath.getWeight() < shortestPath.getWeight()) {
                shortestPath = newPath;
            }
        }
        return shortestPath;
    }

    /**
     *
     * @param node: current node
     * @param nodes: list of nodes
     * @returns the closest node to node from nodes
     */
    public static RawGraph.Node closestNode(RawGraph.Node node, List<RawGraph.Node> nodes){

        return null;
    }
}
