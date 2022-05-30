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
    //private int routeIndex = 0;
//    Graph<String, EdgeWithId> graph;
//    Map<String, String> edgeInfo;
//    Map<String, String> placeInfo;
    List<RoutePackage> route;
    RouteMaker routeMaker = RouteMaker.builder();

    //Current implementation stuff
    List<MetaVertex> futureNodes;
    List<MetaVertex> pastNodes = new ArrayList<MetaVertex>();
    List<MetaVertex> allExhibits;

    MetaVertex current;
    MetaVertex next;
    Searcher searcher;
    Boolean firstPrev = false;

    //comment

    public ZooNavigator(RawGraph rawGraph){
        this.searcher = Searcher.builder(rawGraph);
    }

    public static ZooNavigator builder(RawGraph rawGraph){
        return new ZooNavigator(rawGraph);
    }

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

    public List<RoutePackage> route(List<String> nodes){
            return routeMaker.route(nodes);
    }

    public ZooNavigator setRoute(List<RoutePackage> route) {
        this.route = route;
        return this;
    }

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
