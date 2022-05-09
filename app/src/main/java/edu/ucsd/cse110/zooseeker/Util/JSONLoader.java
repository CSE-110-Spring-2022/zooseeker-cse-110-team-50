package edu.ucsd.cse110.zooseeker.Util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Util.Router.EdgeInfo;
import edu.ucsd.cse110.zooseeker.Util.Router.RawGraph;

public class JSONLoader {
//    private static RawGraph graph = null;

    public static List<Place> loadExamplePlaceData(Context context, String path) {
        List<Place> ret;
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Place>>(){}.getType();
            ret =  gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            ret = Collections.emptyList();
        }
        for (Place place : ret) {
            place.placeId = place.id;
        }
        return ret;
    }

    // Graph singleton
    public static RawGraph loadRawGraph(Context context) {
        final String PATH = "sample_zoo_graph.json";
//        if (graph != null) return graph;
        RawGraph graph = null;

        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<RawGraph>(){}.getType();
            graph = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    public static Map<String, String> loadEdgeInfo(Context context) {
        final String PATH = "sample_edge_info.json";
        List<EdgeInfo> ret;
        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<EdgeInfo>>(){}.getType();
            ret =  gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            ret = Collections.emptyList();
        }
        Map<String, String> edgeInfo = new HashMap<>();
        for(EdgeInfo ei : ret) edgeInfo.put(ei.id, ei.street);
        return edgeInfo;
    }

    public static RawGraph loadTestRawGraph(Context context) {
        final String PATH = "test/sample_zoo_graph.json";
//        if (graph != null) return graph;
        RawGraph graph = null;

        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<RawGraph>(){}.getType();
            graph = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    public static Map<String, String> loadTestEdgeInfo(Context context) {
        final String PATH = "test/sample_edge_info.json";
        List<EdgeInfo> ret;
        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<EdgeInfo>>(){}.getType();
            ret =  gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            ret = Collections.emptyList();
        }
        Map<String, String> edgeInfo = new HashMap<>();
        for(EdgeInfo ei : ret) edgeInfo.put(ei.id, ei.street);
        return edgeInfo;
    }

}
