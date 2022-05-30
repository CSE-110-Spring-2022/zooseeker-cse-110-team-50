package edu.ucsd.cse110.zooseeker.Util.Router;

import android.content.Context;
import android.util.Pair;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Util.Geometry.NearestObjectUtil;
import edu.ucsd.cse110.zooseeker.Util.Geometry.Point2D;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader.JSONLoader;

public class Router {
    Graph graph;

    public Router(Context context) {
        List<Place> nodeInfo = JSONLoader.loadNodeInfo(context);
        Map<String, String> edgeInfo = JSONLoader.loadEdgeInfo(context);
        ZooGraphMapper zooGraphMapper = JSONLoader.loadRawGraph(context);
        this.graph = new GraphBuilder()
                .loadNodes(nodeInfo)
                .loadEdgeInfo(edgeInfo)
                .loadGraphInfo(zooGraphMapper)
                .build();
    }

    // TODO: shortestPath(String n1_id, String n2_id) -> List<EdgeWithId> shortestPath;

    /**
     * Given a node, find the nearest node (in terms of the Euclidean distance)
     * from a given list of node
     * @param node the node
     * @param nodeList the list of node
     * @return (distance, nearestNode)
     *
     * If nodeList is empty, the distance will be -1
     */
    public static Pair<Double, String> nearstPoint(AbstractNode node, List<AbstractNode> nodeList) {

        if (nodeList.size() == 0) {
            return new Pair<>(-1.0, node.id);
        }

        Point2D nodeCoord = node.getPoint2DCoord();
        List<Point2D> nodeListCoord = nodeList.stream()
                .map(n -> n.getPoint2DCoord())
                .collect(Collectors.toList());

        Pair<Double, Integer> nearestPoint = NearestObjectUtil.nearestPoint(nodeCoord, nodeListCoord);

        return new Pair<>(
                nearestPoint.first,
                nodeList.get(nearestPoint.second).id
        );
    }

}
