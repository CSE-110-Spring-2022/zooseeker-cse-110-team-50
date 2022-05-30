package edu.ucsd.cse110.zooseeker.NewNavigator;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader.JSONLoader;
import edu.ucsd.cse110.zooseeker.Util.Router.RawGraph;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

/**
 * Things it should do:
 *  -go through route (next and previous)
 *  -skip current node and reroute
 *      -observer for database, deleting planlist from database through observer
 *  -define methods in class before implementing
 *  -signal as public interfaces
 */

public class ZooNavigator {

    //Current implementation stuff
    List<String> futureNodes;
    Stack<String> pastNodes;
    List<String> routeExhibits;
    String currentVertex;
    String nextVertex;

    // Sometime in the future for checks

    //comment

    public ZooNavigator(List<String> ids){
        pastNodes = new Stack<>();
        routeExhibits = new ArrayList<>(ids);
        futureNodes = new ArrayList<>(ids);
        routeExhibits.add("entrance_exit_gate");
        currentVertex = "entrance_exit_gate";
        nextVertex = getClosestNode();
        if(nextVertex == null){
            nextVertex = "entrance_exit_gate";
        }

    }

    public void next(){
        if(nextVertex.equals("entrance_exit_gate") && futureNodes.size() == 0){
            return;
        }

        if(routeExhibits.contains(currentVertex) && !pastNodes.contains(currentVertex)){
            pastNodes.push(currentVertex);
        }
        nextVertex = getClosestNode();
        if(nextVertex == null){
            nextVertex = "entrance_exit_gate";
        }
    }

    public void back(){
        if(pastNodes.size() == 0){
            return;
        }
        if(!futureNodes.contains(nextVertex) && !nextVertex.equals("entrance_exit_gate")){
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
        if(nextVertex.equals("entrance_exit_gate") && futureNodes.size() == 0){
            return;
        }
        routeExhibits.remove(nextVertex);
        nextVertex = getClosestNode();
        if(nextVertex == null){
            nextVertex = "entrance_exit_gate";
        }
    }

    private String getClosestNode(){
        if(futureNodes.size() == 0){
            return null;
        }
        String closestNode = "dummyString";
        futureNodes.remove(closestNode);
        return closestNode;
    }
    public String getCurrNode(){
        return null;
    }

    public String getNextNode(){
        return null;
    }

    public String getRouteBrief(){
        return null;
    }

    public String getRouteDetailed(){
        return null;
    }

    public String getRoutePreview(){
        return null;
    }










   /*

    public ZooNavigator build(){ return this; }

    public void nextExhibit(){
        if(firstPrev) {
            MetaVertex alt = current;
            current = next;
            next = current;
            firstPrev = false;
        }
        else {
            pastNodes.add(current);
            current = next;
            next = searcher.closestNode(current, futureNodes);
        }
    }

    public void previousExhibit(){
        if(!firstPrev){
            firstPrev = true;
            futureNodes.add(next);
            next = searcher.closestNode(current, pastNodes);
        }
        else{
            futureNodes.add(current);
            current = next;
            next = searcher.closestNode(current, pastNodes);
        }
    }

    public MetaVertex currentExhibit() {
        return current;
    }

    public void skip(){
        //route.remove(routeIndex);
        next = searcher.closestNode(current, futureNodes);
    }


    /**
     * Everything below is legacy based on RouteMaker and RoutePackage,
     * currently using Searcher and MetaVertex
     */
/*
    public List<RoutePackage> route(List<String> nodes){
            return routeMaker.route(nodes);
    }

    public ZooNavigator setRoute(List<RoutePackage> route) {
        this.route = route;
        return this;
    }


 */
//    public void reroute(List<String> nodes){
//        List<RoutePackage> oldRoute = route;
//        List<String> newRouteNodes = new ArrayList<String>();
//        for(int i = routeIndex; i < nodes.size(); ++i){
//            newRouteNodes.add(nodes.get(i));
//            oldRoute.remove(i);
//        }
//        List<RoutePackage> newRoute = route(newRouteNodes);
//        oldRoute.addAll(newRoute);
//        setRoute(oldRoute); //note, not returning THE old route, but an edited route
//    }

//    public void setRoute(List<RoutePackage> route){
//        this.route = route;
//    }

}
