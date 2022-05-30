package edu.ucsd.cse110.zooseeker.Util.Router;

import android.content.Context;
import android.util.Pair;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Util.Geometry.NearestObjectUtil;
import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader.JSONLoader;

public class Router {
    List<Place> nodeInfo;
    Map<String, String> edgeInfo;
    ZooGraphMapper zooGraphMapper;
    Graph graph;
    Map<String, MetaNode> metaNodeMap;

    public Router(Context context) {
        List<Place> nodeInfo = JSONLoader.loadNodeInfo(context);
        Map<String, String> edgeInfo = JSONLoader.loadEdgeInfo(context);
        ZooGraphMapper zooGraphMapper = JSONLoader.loadRawGraph(context);
        Pair<Graph, Map<String, MetaNode>> graphWithInfo = new GraphBuilder()
                .loadNodes(nodeInfo)
                .loadEdgeInfo(edgeInfo)
                .loadGraphInfo(zooGraphMapper)
                .build();
        this.graph = graphWithInfo.first;
        this.metaNodeMap = graphWithInfo.second;
    }

    public Router(List<Place> nodeInfo, Map<String, String> edgeInfo, ZooGraphMapper zooGraphMapper) {
        Pair<Graph, Map<String, MetaNode>> graphWithInfo = new GraphBuilder()
                .loadNodes(nodeInfo)
                .loadEdgeInfo(edgeInfo)
                .loadGraphInfo(zooGraphMapper)
                .build();
        this.graph = graphWithInfo.first;
        this.metaNodeMap = graphWithInfo.second;
    }

    public GraphPath<MetaNode, EdgeWithId> shortestGraphPath(String node1Id, String node2Id) {
        DijkstraShortestPath<MetaNode, EdgeWithId> dijkstraShortestPath =
                new DijkstraShortestPath<MetaNode, EdgeWithId>(graph);
        GraphPath<MetaNode, EdgeWithId> graphPath = dijkstraShortestPath.getPath(
                metaNodeMap.get(node1Id), metaNodeMap.get(node2Id)
        );
        return graphPath;
    }

    public Pair<List<EdgeWithId>, Double> shortestPathWithDistance(String node1Id, String node2Id) {
        GraphPath<MetaNode, EdgeWithId> graphPath = this.shortestGraphPath(node1Id, node2Id);
        return new Pair<>(graphPath.getEdgeList(), graphPath.getWeight());
    }

    public List<EdgeWithId> shortestPath(String node1Id, String node2Id) {
        GraphPath<MetaNode, EdgeWithId> graphPath = this.shortestGraphPath(node1Id, node2Id);
        return graphPath.getEdgeList();
    }

    public List<GraphPath<MetaNode, EdgeWithId>> route(String source, String target, List<String> toVisit) {
        List<GraphPath<MetaNode, EdgeWithId>> graphPathList = new ArrayList<>();

        if (toVisit.isEmpty()) {
            return graphPathList;
        }

        MetaNode src = this.metaNodeMap.get(source);
        MetaNode tar = this.metaNodeMap.get(target);
        List<MetaNode> nodeToVisit = new ArrayList<>();
        for (String s : toVisit) {
            nodeToVisit.add(this.metaNodeMap.get(s));
        }

        // remove source in the list
        for (int i = 0; i < nodeToVisit.size(); i++) {
            if (nodeToVisit.get(i).equals(src)) {
                nodeToVisit.remove(i);
                break;
            }
        }

        // remove target in the list
        for (int i = 0; i < nodeToVisit.size(); i++) {
            if (nodeToVisit.get(i).equals(tar)) {
                nodeToVisit.remove(i);
                break;
            }
        }

        if (nodeToVisit.isEmpty()) {
            // node to visit only contains source and target
            graphPathList.add(shortestGraphPath(source, target));
            return graphPathList;
        }

        // Greedy to find local minima
        int currTargetIdx = nearestNodeIdx(src, nodeToVisit);
        MetaNode currTarget = nodeToVisit.get(currTargetIdx);
        graphPathList.add(shortestGraphPath(src.id, currTarget.id));

        while (!nodeToVisit.isEmpty()) {
            MetaNode currSrc = currTarget;
            currTargetIdx = nearestNodeIdx(currTarget, nodeToVisit);
            currTarget = nodeToVisit.get(currTargetIdx);
            graphPathList.add(shortestGraphPath(currSrc.id, currTarget.id));
            nodeToVisit.remove(currTargetIdx);
        }

        graphPathList.add(shortestGraphPath(currTarget.id, tar.id));

        return graphPathList;
    }

    /**
     * Given a node, find the nearest node (in terms of the Euclidean distance)
     * from a given list of node
     * @param node the node
     * @param nodeList the list of node
     * @return (distance, nearestNode)
     *
     * If nodeList is empty, the distance will be -1
     */
    public static Pair<Double, AbstractNode> nearestNode(AbstractNode node, List<AbstractNode> nodeList) {

        if (nodeList.size() == 0) {
            return new Pair<>(-1.0, node);
        }

        Point2D nodeCoord = node.getPoint2DCoord();
        List<Point2D> nodeListCoord = nodeList.stream()
                .map(AbstractNode::getPoint2DCoord)
                .collect(Collectors.toList());

        Pair<Double, Integer> nearestPoint = NearestObjectUtil.nearestPoint(nodeCoord, nodeListCoord);

        return new Pair<>(
                nearestPoint.first,
                nodeList.get(nearestPoint.second)
        );
    }

    private static int nearestNodeIdx(AbstractNode node, List<MetaNode> nodeList) {

        if (nodeList.size() == 0) {
            return -1;
        }

        Point2D nodeCoord = node.getPoint2DCoord();
        List<Point2D> nodeListCoord = nodeList.stream()
                .map(AbstractNode::getPoint2DCoord)
                .collect(Collectors.toList());

        Pair<Double, Integer> nearestPoint = NearestObjectUtil.nearestPoint(nodeCoord, nodeListCoord);

        return nearestPoint.second;
    }

}
