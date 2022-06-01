package edu.ucsd.cse110.zooseeker.NewNavigator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Util.Router.Router;

/**
 * Things it should do:
 *  -go through route (next and previous)
 *  -skip current node and reroute
 *      -observer for database, deleting planlist from database through observer
 *  -define methods in class before implementing
 *  -signal as public interfaces
 */

public class ZooNavigator implements Serializable {
    // Constants
    public static final String ENTRANCE_EXIT_GATE_NODE_ID = "entrance_exit_gate";

    //Current implementation stuff
    List<String> futureNodes;
    List<String> pastNodes;
    public List<String> planList; // list of all node ids in plan list
    String currentVertex;
    String nextVertex;
    Router router;
    private int initialPlanListHashCode;


    public ZooNavigator(List<String> ids, Router router, double lat, double log){
        // immutable hash code
        setInitialPlanListHashCode(ids);

        this.router = router;
        pastNodes = new ArrayList<>();
        planList = new ArrayList<>(ids);
        futureNodes = new ArrayList<>(ids);
        currentVertex = planList.get(0);
//        currentVertex = getStartNode(lat, log);
        futureNodes.remove(currentVertex);
        nextVertex = getClosestNode();
        if(nextVertex == null){
            nextVertex = "entrance_exit_gate";
        }
    }

    // Call only from ZooNavigatorJsonMapper
    public ZooNavigator(
            List<String> ids,
            Router router,
            List<String> pastNodes,
            List<String> futureNodes,
            String currentVertex,
            String nextVertex) {
        // immutable hash code
        setInitialPlanListHashCode(ids);

        this.router = router;
        planList = new ArrayList<>(ids);
        this.pastNodes = new ArrayList<>(pastNodes);
        this.futureNodes = new ArrayList<>(futureNodes);
        this.currentVertex = currentVertex;
        this.nextVertex = nextVertex;
        if(nextVertex == null){
            this.nextVertex = "entrance_exit_gate";
        }
    }

    private String getStartNode(double lat, double log) {
        return "";
    }

    public int getInitialPlanListHashCode() {
        return initialPlanListHashCode;
    }

    private void setInitialPlanListHashCode(List<String> initialPlanList) {
        initialPlanListHashCode = initialPlanList.hashCode();
    }

    public void dangerouslyUpdateInitialPlanListHashCode(int code) {
        initialPlanListHashCode = code;
    }

    public void next(){
        if(nextVertex.equals("entrance_exit_gate") && futureNodes.size() == 0){
            return;
        }

        if(planList.contains(currentVertex) && !pastNodes.contains(currentVertex)){
            pastNodes.add(currentVertex);
        }
        currentVertex = nextVertex;
        nextVertex = getClosestNode();
        if(nextVertex == null){
            nextVertex = "entrance_exit_gate";
        }
    }

    public void back(){
        if(pastNodes.size() == 0){
            return;
        }
        if(!futureNodes.contains(nextVertex) && !nextVertex.equals("entrance_exit_gate")
                && planList.contains(nextVertex)){
            futureNodes.add(nextVertex);
        }
        nextVertex = currentVertex;
        currentVertex = pastNodes.remove(pastNodes.size()-1);
    }

    public void reverse(){
        String temp = currentVertex;
        currentVertex = nextVertex;
        nextVertex = temp;
    }

    public void skip(){
        if(nextVertex.equals(ENTRANCE_EXIT_GATE_NODE_ID) && futureNodes.size() == 0){
            return;
        }
        planList.remove(nextVertex);
        nextVertex = getClosestNode();
        if(nextVertex == null){
            nextVertex = ENTRANCE_EXIT_GATE_NODE_ID;
        }
    }

    private String getClosestNode(){
        if(futureNodes.size() == 0){
            return null;
        }
        String closestNode = router.nearestNode(currentVertex, futureNodes);
        futureNodes.remove(closestNode);
        return closestNode;
    }

    public String shouldReroute(double latitude, double longitude) {
        return router.shouldReroute(planList, currentVertex, nextVertex, latitude, longitude);
    }

    public void reroute(String id){
        if(planList.contains(currentVertex) && !pastNodes.contains(currentVertex)){
            pastNodes.add(currentVertex);
        }
        if(!futureNodes.contains(nextVertex) && !nextVertex.equals(ENTRANCE_EXIT_GATE_NODE_ID)
                && planList.contains(nextVertex)){
            futureNodes.add(nextVertex);
        }
        currentVertex = id;
        futureNodes.remove(id);
        nextVertex = getClosestNode();
        if(nextVertex == null){
            nextVertex = ENTRANCE_EXIT_GATE_NODE_ID;
        }
    }

    public String getCurrNode(){
        return currentVertex;
    }

    public String getNextNode(){
        return nextVertex;
    }

    public String getRouteBrief(){
        return router.shortestGraphPathInStringBrief(currentVertex, nextVertex);
    }

    public String getRouteDetailed(){
        return router.shortestGraphPathInStringDetailed(currentVertex, nextVertex);
    }

    public String getRoutePreview(){

        List<String> routePreviewList = new ArrayList<>();
        for(String pNode : pastNodes){
            routePreviewList.add(pNode);
        }
        routePreviewList.add(currentVertex);
        routePreviewList.add(nextVertex);
        for(String fNode : futureNodes){
            routePreviewList.add(fNode);
        }
        String start = routePreviewList.remove(0);

        return router.routePreview(start, "entrance_exit_gate", routePreviewList);
    }
}
