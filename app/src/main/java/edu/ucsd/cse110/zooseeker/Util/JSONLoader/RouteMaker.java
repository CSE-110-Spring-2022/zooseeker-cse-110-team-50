package edu.ucsd.cse110.zooseeker.Util.JSONLoader;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.ucsd.cse110.zooseeker.Navigator.EdgeWithId;
import edu.ucsd.cse110.zooseeker.Navigator.NodeWithDist;
import edu.ucsd.cse110.zooseeker.Navigator.RoutePackage;
import edu.ucsd.cse110.zooseeker.Navigator.RouteStep;
import edu.ucsd.cse110.zooseeker.Util.Router.RawGraph;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;
//import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class RouteMaker {
    Graph<String, EdgeWithId> graph;
    Map<String, String> edgeInfo;
    Map<String, String> placeInfo;
    private String start;
    private String end;

    private List<RouteStep> steps = new ArrayList<>();

    public String getStart(){ return start; }

    public String getEnd(){ return end; }

    public static RouteMaker builder(){ return new RouteMaker();}

    public RouteMaker build(){ return this;}

    public RouteMaker loadEdgeInfo(Map<String, String> edgeInfo){
        this.edgeInfo = edgeInfo;
        return this;
    }

    public void addStep(RouteStep step){ steps.add(step); }

    public RouteMaker loadPlaceInfo(Map<String, String> placeInfo){
        this.placeInfo = placeInfo;
        return this;
    }

    public RouteMaker loadFromRawGraph(RawGraph rawGraph){
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

    public List<RoutePackage> route(List<String> nodes) {
        List<GraphPath<String, EdgeWithId>> routes = new ArrayList<>();
        String START = "entrance_exit_gate";
        String current = START;
        nodes.remove(current);
        while(!nodes.isEmpty()) {
            routes.add(getPathToClosestNode(current, nodes));
            current = routes.get(routes.size()-1).getEndVertex();
            nodes.remove(current);
        }
        routes.add(shortestPath(current, START));

        List<RoutePackage> ret = new ArrayList<>();
        for (GraphPath<String, EdgeWithId> route : routes) {
            RoutePackage pkg = new RoutePackage(route.getStartVertex(), route.getEndVertex(), edgeInfo, placeInfo);
            for (EdgeWithId edge : route.getEdgeList()) {
                pkg.addStep(new RouteStep(edge.edgeId, graph.getEdgeWeight(edge), graph.getEdgeTarget(edge), placeInfo));
            }
            ret.add(pkg);
        }
        return ret;
    }
}
