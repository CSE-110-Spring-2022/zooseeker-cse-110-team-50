package edu.ucsd.cse110.zooseeker.NewNavigator;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //past implementation stuff
    //private int routeIndex = 0;
//    Graph<String, EdgeWithId> graph;
//    Map<String, String> edgeInfo;
//    Map<String, String> placeInfo;
    //List<RoutePackage> route;
    //RouteMaker routeMaker = RouteMaker.builder();

    //Current implementation stuff
    List<MetaVertex> futureNodes;
    List<MetaVertex> pastNodes;
    List<MetaVertex> allExhibits;
    List<MetaVertex> routeExhibits;

    private List<Router.RoutePackage> pkgList;

    MetaVertex current;
    MetaVertex next;

    // Sometime in the future for checks
    Boolean firstPrev = false;

    //comment

    public ZooNavigator(PlaceDao placeDao, PlanItemDao planItemDao, Context context){
        futureNodes = new ArrayList<>();
        pastNodes = new ArrayList<>();
        allExhibits = new ArrayList<>();
        routeExhibits = new ArrayList<>();

        // Put all of planItemDao places to routeExhibits. Put all of placeDao places to allExhibits

        List<Place> allPlaceExhibits = placeDao.getAll();

        // Convert allPlaceExhibit places to MetaVertex

        // Go through each place
        for(Place place : allPlaceExhibits){

            if(place.parentId == null){
                allExhibits.add(new MetaVertex(place));
            }
            else{
                // There is a place with a parentId. Do duplicate checks
                boolean isDuplicate = false;
                if(place.kind.equals("exhibit_group")){
                    continue;
                }
                for(MetaVertex mt : allExhibits){
                    if(mt.id.equals(place.parentId)){
                        mt.addPlace(place);
                        isDuplicate = true;
                        break;
                    }
                }
                if(!isDuplicate){
                    allExhibits.add(new MetaVertex(place));
                }
            }

            // Now we add lats and longs associated with parents in each meta vertex

            // Look up in placeDao each mt.id, make it a place, get its lats and longs, and store it
            // in mt

            for(MetaVertex mt : allExhibits){
                if(mt.hasParent){
                    Place parent = placeDao.get(mt.id);
                    mt.setName(parent.name);
                    mt.setLatLng(parent.lat, parent.lng);
                }
            }
        }
        Map<String, String> edgeInfo = JSONLoader.loadEdgeInfo(context);
        RawGraph rawGraph = JSONLoader.loadRawGraph(context);

        final Map<String, String> placeInfoMap = new HashMap<>();
        for (MetaVertex mt: allExhibits)
            placeInfoMap.put(mt.id, mt.name);
        Router routeMaker = Router.builder()
                .loadEdgeInfo(edgeInfo)
                .loadFromRawGraph(rawGraph)
                .loadPlaceInfo(placeInfoMap)
                .build();

        List<PlanItem> allPlanItems = planItemDao.getAll();

        List<String> allNodes = new ArrayList<>();
        for(PlanItem item : allPlanItems){
            Place place = placeDao.get(item.placeId);
            if(place.parentId == null){
                allNodes.add(item.placeId);
            }
            else{
                // it has a parent
                // check for duplicates
                boolean isDuplicate = false;
                for(String stringItem : allNodes){
                    if(stringItem.equals(place.parentId)){
                        // there is a duplicate. go to next iteration and don't add to allNodes
                        isDuplicate = true;
                    }
                }
                if(!isDuplicate){
                    allNodes.add(place.parentId);
                }
            }
        }
        pkgList = routeMaker.route(allNodes);
    }

    public List<Router.RoutePackage> getPkgList(){
        return pkgList;
    }

    public void next(){

    }

    public void back(){

    }

    public void reverse(){

    }

    public void skip(){

    }

    public String getCurrNode(){
        return null;
    }

    public String getNextNode(){
        return null;
    }

    public List<String> getRouteBrief(){
        return null;
    }

    public List<String> getRouteDetailed(){
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
