package edu.ucsd.cse110.zooseeker.NewNavigator;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
    Stack<String> pastNodes;
    List<String> planList; // list of all node ids in plan list
    String currentVertex;
    String nextVertex;
    Router router;

    // Sometime in the future for checks

    //comment

    public ZooNavigator(List<String> ids, Router router){
        this.router = router;
        pastNodes = new Stack<>();
        planList = new ArrayList<>(ids);
        futureNodes = new ArrayList<>(ids);
        planList.add("entrance_exit_gate");
        currentVertex = "entrance_exit_gate";
        nextVertex = getClosestNode();
        if(nextVertex == null){
            nextVertex = "entrance_exit_gate";
        }
        Log.d("ZooNavigator", "\n" + getRoutePreview());
    }

    public void next(){
        if(nextVertex.equals("entrance_exit_gate") && futureNodes.size() == 0){
            return;
        }

        if(planList.contains(currentVertex) && !pastNodes.contains(currentVertex)){
            pastNodes.push(currentVertex);
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
        currentVertex = pastNodes.pop();
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
            pastNodes.push(currentVertex);
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
        return router.routePreview("entrance_exit_gate", "entrance_exit_gate", futureNodes);
    }
}
