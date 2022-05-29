package edu.ucsd.cse110.zooseeker.Navigator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoutePackage {
    private String start = "";
    private String end = "";
    private List<RouteStep> steps = new ArrayList<>();
    Map<String, String> edgeInfo;
    Map<String, String> placeInfo;
    public RoutePackage(String startPlaceId, String endPlaceId, Map<String, String> edge, Map<String, String> place) {
        //this.edgeInfo = edge;
        //this.placeInfo = place;
        this.start = placeInfo.get(startPlaceId);
        this.end = placeInfo.get(endPlaceId);
    }

    public String getStart() {
        return  start;
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

            if(start == step.to){
                continue;
            }
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
            if(start == step.to){
                continue;
            }
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
