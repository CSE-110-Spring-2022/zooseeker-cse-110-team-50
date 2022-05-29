package edu.ucsd.cse110.zooseeker.Navigator;


import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker.Util.Router.Router;

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
    Graph<String, EdgeWithId> graph;
    Map<String, String> edgeInfo;
    Map<String, String> placeInfo;

    public static ZooNavigator builder(){ return new ZooNavigator(); }

    public ZooNavigator build(){ return this; }

    public void nextExhibit(){

        routeIndex++;
    }

    public void previousExhibit(){
        routeIndex--;
    }

    public List<RoutePackage> route(List<String> nodes){
        return null;
    }

    public void reroute(){

    }
}
