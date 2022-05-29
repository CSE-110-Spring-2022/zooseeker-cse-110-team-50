package edu.ucsd.cse110.zooseeker.Navigator;


import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker.Util.JSONLoader.RouteMaker;

/**
 * Things it should do:
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
    RouteMaker routeMaker = RouteMaker.builder();

    public static ZooNavigator builder(){ return new ZooNavigator(); }

    public ZooNavigator setRoute(List<RoutePackage> route) {
        this.route = route;
        return this;
    }

    public ZooNavigator build(){ return this; }

    public RoutePackage nextExhibit(){
        if(routeIndex + 1 < route.size()){ routeIndex++; }
        return currentExhibit();
    }

    public RoutePackage previousExhibit(){
        if(routeIndex - 1 >= 0){ routeIndex--; }
        return currentExhibit();
    }

    public RoutePackage currentExhibit() {
        return route.get(routeIndex);
    }

    public List<RoutePackage> route(List<String> nodes){
            return routeMaker.route(nodes);
    }

    public List<RoutePackage> reroute(List<String> nodes){
        ArrayList<RoutePackage> newRoute = new ArrayList<RoutePackage>();
        for(int i = routeIndex; i < nodes.size(); ++i){

        }

        return null;
    }
}
