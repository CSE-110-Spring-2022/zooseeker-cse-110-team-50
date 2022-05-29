package edu.ucsd.cse110.zooseeker.Navigator;

import java.util.Map;

public class RouteStep {
    public String edgeId;
    public double distance;
    public String to;
    public RouteStep(String edgeId, double distance, String to, Map<String, String> placeInfo) {
        this.edgeId = edgeId;
        this.distance = distance;
        this.to = placeInfo.get(to);
        //this.to = "";
    }
}
