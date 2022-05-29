package edu.ucsd.cse110.zooseeker.Navigator;


import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Util.Router.RawGraph;

/**
 * Things it should do:
 *  -go through route (next and previous)
 *  -skip current node and reroute
 *      -observer for database, deleting planlist from database through observer
 *  -define methods in class before implementing
 *  -signal as public interfaces
 */

public class ZooNavigator {

    //past implementation stuff
    private int routeIndex = 0;
//    Graph<String, EdgeWithId> graph;
//    Map<String, String> edgeInfo;
//    Map<String, String> placeInfo;
    List<RoutePackage> route;
    RouteMaker routeMaker = RouteMaker.builder();

    //Current implementation stuff
    List<RawGraph.Node> futureNodes;
    List<RawGraph.Node> pastNodes = new ArrayList<RawGraph.Node>();
    RawGraph.Node current;
    RawGraph.Node next;

    public ZooNavigator(){

    }

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

    public void reroute(List<String> nodes){
        List<RoutePackage> oldRoute = route;
        List<String> newRouteNodes = new ArrayList<String>();
        for(int i = routeIndex; i < nodes.size(); ++i){
            newRouteNodes.add(nodes.get(i));
            oldRoute.remove(i);
        }
        List<RoutePackage> newRoute = route(newRouteNodes);
        oldRoute.addAll(newRoute);
        setRoute(oldRoute); //note, not returning THE old route, but an edited route
    }

    public void skip(){
        route.remove(routeIndex);

    }

//    public void setRoute(List<RoutePackage> route){
//        this.route = route;
//    }
}
