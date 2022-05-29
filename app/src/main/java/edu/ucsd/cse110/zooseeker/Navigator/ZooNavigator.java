package edu.ucsd.cse110.zooseeker.Navigator;


import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Things it should do:
 *  -make route
 *  -reroute
 *  -go through route (next and previous)
 *  -skip current node and reroute
 *      -observer for database, deleting planlist from database through observer
 *
 */

public class ZooNavigator {

    private int routeIndex = 0;
//    Graph<String, EdgeWithId> graph;
//    Map<String, String> edgeInfo;
//    Map<String, String> placeInfo;
    List<RoutePackage> route;

    public static ZooNavigator builder(){ return new ZooNavigator(); }

    public ZooNavigator setRoute(List<RoutePackage> route) {
        this.route = route;
        return this;
    }

    public ZooNavigator build(){ return this; }

    public RoutePackage nextExhibit(){
        routeIndex++;
        return currentExhibit();
    }

    public RoutePackage previousExhibit(){
        routeIndex--;
        return currentExhibit();
    }

    public RoutePackage currentExhibit() {
        return route.get(routeIndex);
    }

    public void route(List<String> nodes){

    }

    public void reroute(){

    }
}
