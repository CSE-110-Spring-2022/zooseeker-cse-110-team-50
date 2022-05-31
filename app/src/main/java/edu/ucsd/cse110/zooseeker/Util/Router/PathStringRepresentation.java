package edu.ucsd.cse110.zooseeker.Util.Router;

import org.jgrapht.GraphPath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PathStringRepresentation implements Serializable {
    public static String toStringDetailed(GraphPath<MetaNode, EdgeWithId> graphPath) {

        int cnt = 1;
        String stringRep = "From " + graphPath.getStartVertex().name + "\n\n\n";
        List<EdgeWithId> edgeList = graphPath.getEdgeList();

        for (EdgeWithId edge : edgeList) {

            stringRep += (cnt++ + ". Proceed on " +
                    edge.name +
                    " " + edge.distance +
                    " ft toward " + edge.target.name + "\n\n");
        }

        stringRep += "\nDestination: " + graphPath.getEndVertex().name + "\n";
        return stringRep;
    }

    public static String toStringBrief(GraphPath<MetaNode, EdgeWithId> graphPath) {
        int cnt = 1;
        String ret = "From " + graphPath.getStartVertex().name + "\n\n\n";
        List<String> compressedEdges = new ArrayList();       // edge names
        List<Double> compressedDistances = new ArrayList();   // edge weights
        List<String> endOfPaths = new ArrayList();            // edge targets' names
        int index = -1;

        List<EdgeWithId> edgeList = graphPath.getEdgeList();
        for(EdgeWithId edge : edgeList){
            if(index == -1){
                // first time. We will add the place and the distances
                compressedEdges.add(edge.name);
                index = 0;
                compressedDistances.add(edge.distance);
                endOfPaths.add(edge.target.name);
                continue;
            }
            if(!compressedEdges.get(index).equals(edge.name)){
                // We are in a new place. Add to the compressedEdges
                index++;
                compressedEdges.add(edge.name);
                compressedDistances.add(edge.distance);
                endOfPaths.add(edge.target.name);
            }
            else{
                compressedDistances.set(index, compressedDistances.get(index) + edge.distance);
                endOfPaths.set(index, edge.target.name);
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
        ret += "\nDestination: " + graphPath.getEndVertex().name + "\n";
        return ret;
    }

    public static String toStringPreview(
            List<GraphPath<MetaNode, EdgeWithId>> graphPaths,
            List<String> toVisit
    ) {
        int count = 1;
        String stringRep = "";
        for (GraphPath<MetaNode, EdgeWithId> graphPath : graphPaths) {
            stringRep += ((count++) + ". " +
                    graphPath.getStartVertex().name +
                    " \n" + graphPath.getWeight() + " ft" + "\n");
        }

        stringRep += count + ". Entrance and Exit Gate";
        return stringRep;
    }
}
