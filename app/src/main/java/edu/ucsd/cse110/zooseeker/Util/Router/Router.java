package edu.ucsd.cse110.zooseeker.Util.Router;

import android.content.Context;
import android.util.Pair;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader.JSONLoader;

public class Router {
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

            if (graphPath.getWeight() < totalWeight)
                idx = i;
        }

        return idx;
    }

    public List<GraphPath<MetaNode, EdgeWithId>> route(String source, String target, List<String> toVisit) {
        List<GraphPath<MetaNode, EdgeWithId>> graphPathList = new ArrayList<>();

        if (toVisit.isEmpty()) {
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

    /**
     * NOT TO BE USED IN THE APP
     */
//    public List<GraphPath<MetaNode, EdgeWithId>> route(String source, String target, List<String> toVisit) {
//        List<GraphPath<MetaNode, EdgeWithId>> graphPathList = new ArrayList<>();
//
//        if (toVisit.isEmpty()) {
//            return graphPathList;
//        }
//
//        MetaNode src = this.metaNodeMap.get(source);
//        MetaNode tar = this.metaNodeMap.get(target);
//        List<MetaNode> nodeToVisit = new ArrayList<>();
//        for (String s : toVisit) {
//            nodeToVisit.add(this.metaNodeMap.get(s));
//        }
//
//        // remove source in the list
//        for (int i = 0; i < nodeToVisit.size(); i++) {
//            if (nodeToVisit.get(i).equals(src)) {
//                nodeToVisit.remove(i);
//                break;
//            }
//        }
//
//        // remove target in the list
//        for (int i = 0; i < nodeToVisit.size(); i++) {
//            if (nodeToVisit.get(i).equals(tar)) {
//                nodeToVisit.remove(i);
//                break;
//            }
//        }
//
//        if (nodeToVisit.isEmpty()) {
//            // node to visit only contains source and target
//            graphPathList.add(shortestGraphPath(source, target));
//            return graphPathList;
//        }
//
//        // Greedy to find local minima
//        int currTargetIdx = nearestNodeIdx(src, nodeToVisit);
//        MetaNode currTarget = nodeToVisit.get(currTargetIdx);
//        graphPathList.add(shortestGraphPath(src.id, currTarget.id));
//
//        while (!nodeToVisit.isEmpty()) {
//            MetaNode currSrc = currTarget;
//            currTargetIdx = nearestNodeIdx(currTarget, nodeToVisit);
//            currTarget = nodeToVisit.get(currTargetIdx);
//            graphPathList.add(shortestGraphPath(currSrc.id, currTarget.id));
//            nodeToVisit.remove(currTargetIdx);
//        }
//
//        graphPathList.add(shortestGraphPath(currTarget.id, tar.id));
//
//        return graphPathList;
//    }

    public Pair<List<EdgeWithId>, Double> shortestPathWithDistance(String node1Id, String node2Id) {
        GraphPath<MetaNode, EdgeWithId> graphPath = this.shortestGraphPath(node1Id, node2Id);
        return new Pair<>(graphPath.getEdgeList(), graphPath.getWeight());
    }

    public List<EdgeWithId> shortestPath(String node1Id, String node2Id) {
        GraphPath<MetaNode, EdgeWithId> graphPath = this.shortestGraphPath(node1Id, node2Id);
        return graphPath.getEdgeList();
    }

}
