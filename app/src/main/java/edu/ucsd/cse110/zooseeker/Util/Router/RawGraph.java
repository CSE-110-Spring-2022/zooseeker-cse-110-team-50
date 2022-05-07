package edu.ucsd.cse110.zooseeker.Util.Router;

import java.util.List;

public class RawGraph {
    public static class Node {
        public String id;
    }

    public static class Edge {
        public String id;
        public String source;
        public String target;
        public double weight;
    }

    public List<Node> nodes;
    public List<Edge> edges;
}
