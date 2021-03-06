package edu.ucsd.cse110.zooseeker.Util.Router;

import android.content.Context;
import android.util.Pair;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Util.Geometry.Distance2D;
import edu.ucsd.cse110.zooseeker.Util.Geometry.NearestObjectUtil;
import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader.JSONLoader;

public class Router implements Serializable {
    Graph<MetaNode, EdgeWithId> graph;
    Map<String, MetaNode> metaNodeMap;

    public Router(Context context) {
        List<Place> nodeInfo = JSONLoader.loadNodeInfo(context);
        Map<String, String> edgeInfo = JSONLoader.loadEdgeInfo(context);
        ZooGraphMapper zooGraphMapper = JSONLoader.loadRawGraph(context);
        Pair<Graph<MetaNode, EdgeWithId>, Map<String, MetaNode>> graphWithInfo = new GraphBuilder()
                .loadNodes(nodeInfo)
                .loadEdgeInfo(edgeInfo)
                .loadGraphInfo(zooGraphMapper)
                .build();
        this.graph = graphWithInfo.first;
        this.metaNodeMap = graphWithInfo.second;
    }

    public Router(List<Place> nodeInfo, Map<String, String> edgeInfo, ZooGraphMapper zooGraphMapper) {
        Pair<Graph<MetaNode, EdgeWithId>, Map<String, MetaNode>> graphWithInfo = new GraphBuilder()
                .loadNodes(nodeInfo)
                .loadEdgeInfo(edgeInfo)
                .loadGraphInfo(zooGraphMapper)
                .build();
        this.graph = graphWithInfo.first;
        this.metaNodeMap = graphWithInfo.second;
    }

    public GraphPath<MetaNode, EdgeWithId> shortestGraphPath(String source, String target) {
        DijkstraShortestPath<MetaNode, EdgeWithId> dijkstraShortestPath =
                new DijkstraShortestPath<MetaNode, EdgeWithId>(graph);
        GraphPath<MetaNode, EdgeWithId> graphPath = dijkstraShortestPath.getPath(
                metaNodeMap.get(source), metaNodeMap.get(target)
        );
        return graphPath;
    }

    public String shortestGraphPathInStringDetailed(String source, String target) {
        return PathStringRepresentation.toStringDetailed(
                shortestGraphPath(source, target)
        );
    }

    public String shortestGraphPathInStringBrief(String source, String target) {
        return PathStringRepresentation.toStringBrief(
                shortestGraphPath(source, target)
        );
    }

    public String shouldReroute(List<String> nodeSet, String current, String next, double lat, double lon) {
        Point2D currentLocation = new Point2D(lat, lon);

        List<String> nodeIdSet = new ArrayList<>(nodeSet);
        List<Point2D> nodeSetCoords = new ArrayList<>();

        for (String nodeId : nodeIdSet) {
            nodeSetCoords.add(metaNodeMap.get(nodeId).getPoint2DCoord());
        }


        // Get all nodes that aren't planned
//        List<Point2D> notPlannedNodes = new ArrayList<>();
//        List<String> notPlannedNodeIds = new ArrayList<>();
//        for (String nodeId : metaNodeMap.keySet()) {
//            if (!planList.contains(nodeId)) {
//                notPlannedNodes.add(metaNodeMap.get(nodeId).getPoint2DCoord());
//                notPlannedNodeIds.add(nodeId);
//            }
//        }

        // Get EdgeSegments of current path
        GraphPath<MetaNode, EdgeWithId> path = shortestGraphPath(current, next);
        List<Pair<Point2D, Point2D>> edgeSegments = path
                .getEdgeList()
                .stream()
                .map((EdgeWithId::getEdgeSegment))
                .collect(Collectors.toList());

        Pair<Double, Integer> nearestUnvisitedNode
                = NearestObjectUtil.nearestPoint(currentLocation, nodeSetCoords);

        Pair<Double, Integer> nearestEdge
                = NearestObjectUtil.nearestLineSegment(currentLocation, edgeSegments);

        // reroute
        if (nearestUnvisitedNode.first < nearestEdge.first)
            return nodeIdSet.get(nearestUnvisitedNode.second);
        // don't reroute
        else
            return null;
    }

    public String nearestNode(String node, List<String> nodeIdList) {

        String nearestNode = null;
        double totalWeight = Double.POSITIVE_INFINITY;
        for (String currNode : nodeIdList) {
            GraphPath<MetaNode, EdgeWithId> graphPath = this.shortestGraphPath(node, currNode);

            if (graphPath.getWeight() < totalWeight) {
                nearestNode = currNode;
                totalWeight = graphPath.getWeight();
            }

        }
        return nearestNode;
    }

    private int nearestNodeIdx(String node, List<String> nodeList) {

        int idx = -1;
        double totalWeight = Double.POSITIVE_INFINITY;
        for (int i = 0; i < nodeList.size(); i++) {
            String currNode = nodeList.get(i);
            GraphPath<MetaNode, EdgeWithId> graphPath = this.shortestGraphPath(node, currNode);

            if (graphPath.getWeight() < totalWeight) {
                idx = i;
                totalWeight = graphPath.getWeight();
            }
        }

        return idx;
    }

    public List<GraphPath<MetaNode, EdgeWithId>> route(String source, String target, List<String> toVisit) {
        List<GraphPath<MetaNode, EdgeWithId>> graphPathList = new ArrayList<>();

        if (toVisit.isEmpty()) {
            graphPathList.add(shortestGraphPath(source, target));
            return graphPathList;
        }

        // remove source in the list
        for (int i = 0; i < toVisit.size(); i++) {
            if (toVisit.get(i).equals(source)) {
                toVisit.remove(i);
                break;
            }
        }
        // remove target in the list
        for (int i = 0; i < toVisit.size(); i++) {
            if (toVisit.get(i).equals(target)) {
                toVisit.remove(i);
                break;
            }
        }

        if (toVisit.isEmpty()) {
            return graphPathList;
        }

        // only add an exhibit group once
        List<String> originalToVisit = toVisit;
        toVisit = new ArrayList<>();
        Map<String, Boolean> isAdded = new HashMap<>();
        for (String id : this.metaNodeMap.keySet()) {
            isAdded.put(id, false);
        }
        for (String n : originalToVisit) {
            String id = this.metaNodeMap.get(n).id;
            if (!isAdded.get(id)) {
                toVisit.add(id);
                isAdded.put(n, true);
            }
        }

        // Add path to source
        int nearestNodeIdx = this.nearestNodeIdx(source, toVisit);
        String currNode = toVisit.get(nearestNodeIdx);
        graphPathList.add(this.shortestGraphPath(source, currNode));
        toVisit.remove(nearestNodeIdx);

        while (!toVisit.isEmpty()) {
            nearestNodeIdx = this.nearestNodeIdx(currNode, toVisit);
            String targetNode = toVisit.get(nearestNodeIdx);
            graphPathList.add(this.shortestGraphPath(currNode, targetNode));
            currNode = targetNode;
            toVisit.remove(nearestNodeIdx);
        }

        // Add path to target
        graphPathList.add(this.shortestGraphPath(currNode, target));

        return graphPathList;

    }

    public String routePreview(String source, String target, List<String> toVisit) {
        List<GraphPath<MetaNode, EdgeWithId>> graphPathList = this.route(source, target, toVisit);
        return PathStringRepresentation.toStringPreview(graphPathList, toVisit);

    }

    public Pair<List<EdgeWithId>, Double> shortestPathWithDistance(String node1Id, String node2Id) {
        GraphPath<MetaNode, EdgeWithId> graphPath = this.shortestGraphPath(node1Id, node2Id);
        return new Pair<>(graphPath.getEdgeList(), graphPath.getWeight());
    }

    public List<EdgeWithId> shortestPath(String node1Id, String node2Id) {
        GraphPath<MetaNode, EdgeWithId> graphPath = this.shortestGraphPath(node1Id, node2Id);
        return graphPath.getEdgeList();
    }
 
}
