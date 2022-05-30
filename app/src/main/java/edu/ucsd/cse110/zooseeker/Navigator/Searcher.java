package edu.ucsd.cse110.zooseeker.Navigator;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.ucsd.cse110.zooseeker.NewNavigator.EdgeWithId;
import edu.ucsd.cse110.zooseeker.NewNavigator.MetaVertex;
import edu.ucsd.cse110.zooseeker.Util.Router.ZooGraphMapper;

public class Searcher {
    Graph<String, EdgeWithId> graph;
    Map<String, String> edgeInfo;
    Map<String, String> placeInfo;

    public Searcher(ZooGraphMapper zooGraphMapper){
        //Graph<String, EdgeWithId> loadedGraph =
        this.graph = GraphLoader.loadFromRawGraph(zooGraphMapper);
    }

    public static Searcher builder(ZooGraphMapper zooGraphMapper){
        return new Searcher(zooGraphMapper);
    }

    public Searcher build(){ return this; }

    public Searcher loadEdgeInfo(Map<String, String> edgeInfo){
        this.edgeInfo = edgeInfo;
        return this;
    }

    public Searcher loadPlaceInfo(Map<String, String> placeInfo){
        this.placeInfo = placeInfo;
        return this;
    }

    public Searcher loadFromRawGraph(ZooGraphMapper zooGraphMapper){
        graph = new DefaultUndirectedWeightedGraph<>(EdgeWithId.class);
        zooGraphMapper.nodes.stream().forEach((node) -> {
            graph.addVertex(node.id);
        });

        zooGraphMapper.edges.stream().forEach((edge) -> {
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

    public void setGraph(ZooGraphMapper zooGraphMapper){
        graph = GraphLoader.loadFromRawGraph(zooGraphMapper);
    }

    /**
     *
     * @param node: current node
     * @param nodes: list of nodes
     * @returns the closest node to node from nodes
     * This basically stems from the getPathToClosestNode algorithm above with a few tweaks
     */
    public MetaVertex closestNode(MetaVertex node, List<MetaVertex> nodes){
        MetaVertex closest = null;
        String source = node.id;
        List<String> targets = null;
        for(int i = 0; i < nodes.size(); ++i){
            targets.add(nodes.get(i).id);
        }

        PriorityQueue<NodeWithDist> pq = new PriorityQueue<>((x, y) -> (int)(x.distance - y.distance));
        GraphPath<String, EdgeWithId> shortestPath = shortestPath(source, targets.get(0));
        GraphPath<String, EdgeWithId> newPath;
        for (MetaVertex ind : nodes) {
            String target = ind.id;
            newPath = shortestPath(source, target);
            if (newPath.getWeight() < shortestPath.getWeight()) {
                shortestPath = newPath;
                closest = ind;
            }
        }
        return closest;
    }
}
