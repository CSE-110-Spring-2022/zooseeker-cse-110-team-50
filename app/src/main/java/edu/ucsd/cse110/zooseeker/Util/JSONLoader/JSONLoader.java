package edu.ucsd.cse110.zooseeker.Util.JSONLoader;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Util.Router.ZooGraphMapper;

public class JSONLoader {
    public static String BASE_PATH = "";

    public static Map<String, String> loadEdgeInfo(Context context) {
        final String PATH = BASE_PATH + "sample_edge_info.json";
        List<EdgeInfoMapper> ret;
        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<EdgeInfoMapper>>(){}.getType();
            ret =  gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            ret = Collections.emptyList();
        }
        Map<String, String> edgeInfo = new HashMap<>();
        for(EdgeInfoMapper ei : ret) edgeInfo.put(ei.id, ei.street);
        return edgeInfo;
    }

    public static List<Place> loadNodeInfo(Context context) {
        String PATH = BASE_PATH + "sample_node_info.json";
        List<NodeInfoMapper> ret;
        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeInfoMapper>>(){}.getType();
            ret =  gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            ret = Collections.emptyList();
        }

        List<Place> places = new ArrayList<>();
        for (NodeInfoMapper nodeInfoMapper : ret) {
            places.add(nodeInfoMapper.toPlace());
        }

        return places;
    }

    public static List<NodeInfoMapper> loadNodeInfoMapper(Context context) {
        String PATH = BASE_PATH + "sample_node_info.json";
        List<NodeInfoMapper> ret = null;
        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeInfoMapper>>(){}.getType();
            ret =  gson.fromJson(reader, type);
        }
        catch (IOException e) {
            e.printStackTrace();
            ret = Collections.emptyList();
        }
        return ret;
    }

    public static ZooGraphMapper loadRawGraph(Context context) {
        final String PATH = BASE_PATH + "sample_zoo_graph.json";
        ZooGraphMapper graph = null;

        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<ZooGraphMapper>(){}.getType();
            graph = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    /*
    * Below is for testing
    * */

    public static ZooGraphMapper loadTestRawGraph(Context context) {
        final String PATH = "test/sample_zoo_graph.json";
        ZooGraphMapper graph = null;

        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<ZooGraphMapper>(){}.getType();
            graph = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    public static List<Place> loadTestNodeInfo(Context context) {
        String PATH = BASE_PATH + "test/sample_node_info.json";
        List<NodeInfoMapper> ret;
        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeInfoMapper>>(){}.getType();
            ret =  gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            ret = Collections.emptyList();
        }

        List<Place> places = new ArrayList<>();
        for (NodeInfoMapper nodeInfoMapper : ret) {
            places.add(nodeInfoMapper.toPlace());
        }

        return places;
    }

    public static Map<String, String> loadTestEdgeInfo(Context context) {
        final String PATH = "test/sample_edge_info.json";
        List<EdgeInfoMapper> ret;
        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<EdgeInfoMapper>>(){}.getType();
            ret =  gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            ret = Collections.emptyList();
        }
        Map<String, String> edgeInfo = new HashMap<>();
        for(EdgeInfoMapper ei : ret) edgeInfo.put(ei.id, ei.street);
        return edgeInfo;
    }
}
