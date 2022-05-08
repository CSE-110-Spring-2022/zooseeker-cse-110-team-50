package edu.ucsd.cse110.zooseeker.Util.Router;


import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Router {

    Graph<String, EdgeWithId> graph;
    Map<String, String> edgeInfo;

    private class EdgeWithId extends DefaultWeightedEdge {
        public String edgeId;
        public EdgeWithId(String id) {
            edgeId = id;
        }
    }

    private class NodeWithDist {
        public String id;
        public double distance;
        public NodeWithDist(String id, double distance) {this.id = id; this.distance = distance; }

//        public boolean equals(Object obj) {
//            if (!(obj instanceof NodeWithDist)) return false;
//            return ((NodeWithDist) obj).id.equals(this.id);
//        }
    }

    public class RouteStep {
        public String edgeId;
        public double distance;
        public RouteStep(String edgeId, double distance) { this.edgeId = edgeId; this.distance = distance; }
    }

    public class RoutePackage {
        private String start;
        private String end;
        private List<RouteStep> steps = new ArrayList<>();
        public RoutePackage(String start, String end) { this.start = start; this.end = end; }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        public List<RouteStep> getSteps() {
            return steps;
        }

        public void addStep(RouteStep step) {
            steps.add(step);
        }

        public String toString() {
            String ret = "\n";
            for (RouteStep step : steps)
                ret += ("    -> " + edgeInfo.get(step.edgeId) + "\n");
            ret += "\n\n" + end + "\n";
            return ret;
        }
    }

    public static Router builder() {
        return new Router();
    }

    public Router loadEdgeInfo(Map<String, String> edgeInfo) {
        this.edgeInfo = edgeInfo;
        return this;
    }

    public Router loadFromRawGraph(RawGraph rawGraph) {
        graph = new DefaultUndirectedWeightedGraph<>(EdgeWithId.class);
        rawGraph.nodes.stream().forEach((node) -> {
            graph.addVertex(node.id);
        });

        rawGraph.edges.stream().forEach((edge) -> {
            graph.addEdge(edge.source, edge.target, new EdgeWithId(edge.id));
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
            RoutePackage pkg = new RoutePackage(route.getStartVertex(), route.getEndVertex());
            for (EdgeWithId edge : route.getEdgeList()) {
                pkg.addStep(new RouteStep(edge.edgeId, 0));
            }
            ret.add(pkg);
        }
        return ret;
    }
}
