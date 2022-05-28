package edu.ucsd.cse110.zooseeker.Navigator;

public class RouteStep {
    public String edgeId;
    public double distance;
    public String to;
    public RouteStep(String edgeId, double distance, String to) {
        this.edgeId = edgeId;
        this.distance = distance;
        //this.to = placeInfo.get(to);
        this.to = "";
    }
}
