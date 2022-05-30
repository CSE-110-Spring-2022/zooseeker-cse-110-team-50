package edu.ucsd.cse110.zooseeker.Util.Router;

import java.util.ArrayList;
import java.util.List;

public class MetaNode extends AbstractNode {

    List<String> nodeIds;
    String kind;

    public MetaNode(String id, String name, String kind, Double lat, Double lng) {
        super(id, name, lat, lng);
        this.kind = kind;
        this.nodeIds = new ArrayList<>();
    }

    public boolean addNode(String nodeId) {
        if (nodeIds.contains(nodeId))
            return false;
        this.nodeIds.add(nodeId);
        return true;
    }


}
