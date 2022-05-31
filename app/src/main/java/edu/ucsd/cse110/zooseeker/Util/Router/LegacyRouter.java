package edu.ucsd.cse110.zooseeker.Util.Router;


import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * NOTE: LEGACY CODE, NOT TO BE USED IN FINAL APP
 */

public class LegacyRouter {

    Graph<String, EdgeWithId> graph;
    Map<String, String> edgeInfo;
    Map<String, String> placeInfo;

    public class EdgeWithId extends DefaultWeightedEdge {
        public String edgeId;
        public EdgeWithId(String id) {
            edgeId = id;
        }
    }

    private class NodeWithDist {
        public String id;
        public double distance;
        public NodeWithDist(String id, double distance) {this.id = id; this.distance = distance; }
    }

    public class RouteStep {
        public String edgeId;
        public double distance;
        public String to;
        public RouteStep(String edgeId, double distance, String to) {
            this.edgeId = edgeId;
            this.distance = distance;
            this.to = placeInfo.get(to);
        }
    }

    public class RoutePackage {
        private String start;
        private String end;
        private List<RouteStep> steps = new ArrayList<>();
        public RoutePackage(String startPlaceId, String endPlaceId) {
            this.start = placeInfo.get(startPlaceId);
            this.end = placeInfo.get(endPlaceId);
        }

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

        public String toStringDetailed() {
            int cnt = 1;
            String ret = "From " + start + "\n\n\n";
            for (RouteStep step : steps) {

                //if(start == step.to){
                //    continue;
                //}
                ret += (cnt++ + ". Proceed on " +
                        edgeInfo.get(step.edgeId) +
                        " " + step.distance +
                        " ft toward " + step.to + "\n\n");
            }

            ret += "\nDestination: " + end + "\n";
            return ret;
        }

        public String toStringBrief() {
            int cnt = 1;
            String ret = "From " + start + "\n\n\n";
            List<String> compressedEdges = new ArrayList();
            List<Double> compressedDistances = new ArrayList();
            List<String> endOfPaths = new ArrayList();
            int index = -1;

            for(RouteStep step : steps){
                //if(start == step.to){
                //    continue;
                //}
                if(index == -1){
                    // first time. We will add the place and the distances
                    compressedEdges.add(edgeInfo.get(step.edgeId));
                    index = 0;
                    compressedDistances.add(step.distance);
                    endOfPaths.add(step.to);
                    continue;
                }
                if(!compressedEdges.get(index).equals(edgeInfo.get(step.edgeId))){
                    // We are in a new place. Add to the compressedEdges
                    index++;
                    compressedEdges.add(edgeInfo.get(step.edgeId));
                    compressedDistances.add(step.distance);
                    endOfPaths.add(step.to);
                }
                else{
                    compressedDistances.set(index, compressedDistances.get(index) + step.distance);
                    endOfPaths.set(index, step.to);
                }
            }
            index = 0;
            for (String edge : compressedEdges){
                ret += (cnt++ + ". Proceed on " +
                        edge +
                        " " + compressedDistances.get(index) +
                        " ft toward " + endOfPaths.get(index) + "\n\n");
                index++;
            }
            ret += "\nDestination: " + end + "\n";
            return ret;
        }
    }

    public static LegacyRouter builder() {
        return new LegacyRouter();
    }

    public LegacyRouter build() {
        return this;
    }

    public LegacyRouter loadEdgeInfo(Map<String, String> edgeInfo) {
        this.edgeInfo = edgeInfo;
        return this;
    }

    public LegacyRouter loadPlaceInfo(Map<String, String> placeInfo) {
        this.placeInfo = placeInfo;
        return this;
    }

    public LegacyRouter loadFromRawGraph(ZooGraphMapper zooGraphMapper) {
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
                pkg.addStep(new RouteStep(edge.edgeId, graph.getEdgeWeight(edge), graph.getEdgeTarget(edge)));
            }
            ret.add(pkg);
        }
        return ret;
    }
}
