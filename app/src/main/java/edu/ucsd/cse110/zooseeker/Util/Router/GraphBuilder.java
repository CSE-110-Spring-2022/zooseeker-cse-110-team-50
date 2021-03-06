package edu.ucsd.cse110.zooseeker.Util.Router;

import android.util.Pair;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker.Persistence.Place;

public class GraphBuilder implements Serializable {
    List<Place> places;
    Map<String, String> edgeInfo;
    Map<String, MetaNode> metaNodeMap;  // id -> metaNode
    ZooGraphMapper zooGraphMapper;
    List<MetaNode> metaNodeList;
    List<EdgeWithId> edgeList;

    public GraphBuilder loadNodes(List<Place> places){
        this.places = places;
        return this;
    }

    public GraphBuilder loadEdgeInfo(Map<String, String> edgeInfo){
        this.edgeInfo = edgeInfo;
        return this;
    }

    public GraphBuilder loadGraphInfo(ZooGraphMapper zooGraphMapper){
        this.zooGraphMapper = zooGraphMapper;
        return this;
    }

    public Pair<Graph<MetaNode, EdgeWithId>, Map<String, MetaNode>> build() {
        this.constructMetaNodeList();
        this.constructEdgeList();

        Graph<MetaNode, EdgeWithId> graph =
                new DefaultDirectedWeightedGraph<>(EdgeWithId.class);

        this.metaNodeList.stream().forEach((node) -> {
            graph.addVertex(node);
        });

        this.edgeList.stream().forEach((edge) -> {
            graph.addEdge(edge.source, edge.target, edge);
            graph.setEdgeWeight(edge, edge.distance);
        });

        return new Pair<>(graph, metaNodeMap);
    }

    private void constructMetaNodeList() {
        List<Place> waitList = new ArrayList<>();
        metaNodeList = new ArrayList<>();
        metaNodeMap = new HashMap<>();

        for (Place place : this.places) {
            if (place.parentId == null) {
                // exhibit group or an independent node
                MetaNode metaNode = new MetaNode(place.placeId, place.name, place.kind, place.lat, place.lng);
                metaNodeMap.put(place.placeId, metaNode);
                metaNodeList.add(metaNode);
            } else {
                // a child node of a meta node
                MetaNode metaNode = this.searchForMetaNode(place.parentId);
                if (metaNode == null) {
                    waitList.add(place);
                } else {
                    metaNode.addNode(place.placeId);
                    metaNodeMap.put(place.placeId, metaNode);
                }
            }
        }

        // In case child comes before parent in the list
        for (Place place : waitList) {
            MetaNode metaNode = this.searchForMetaNode(place.parentId);
            if (metaNode == null) {
                throw new IllegalArgumentException("Does not contain meta node information.");
            } else {
                metaNode.addNode(place.placeId);
                metaNodeMap.put(place.placeId, metaNode);
            }
        }
    }

    private MetaNode searchForMetaNode(String id) {
        return this.metaNodeMap.get(id);
    }

    private void constructEdgeList() {
        edgeList = new ArrayList<>();

        for (ZooGraphMapper.Edge e : zooGraphMapper.edges) {
            // simulate unweighted graph
            edgeList.add(new EdgeWithId(
                    e.id, edgeInfo.get(e.id), metaNodeMap.get(e.source), metaNodeMap.get(e.target), e.weight
            ));
            edgeList.add(new EdgeWithId(
                    e.id, edgeInfo.get(e.id), metaNodeMap.get(e.target), metaNodeMap.get(e.source), e.weight
            ));
        }

    }

}
