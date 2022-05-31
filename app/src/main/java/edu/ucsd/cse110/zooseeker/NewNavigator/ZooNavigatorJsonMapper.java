package edu.ucsd.cse110.zooseeker.NewNavigator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import edu.ucsd.cse110.zooseeker.Util.JSONLoader.EdgeInfoMapper;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class ZooNavigatorJsonMapper {
    public List<String> futureNodes;
    public List<String> pastNodes;
    public List<String> planList;
    public String currentVertex;
    public String nextVertex;

    public static ZooNavigatorJsonMapper fromZooNavigator(ZooNavigator nav) {
        ZooNavigatorJsonMapper mapper = new ZooNavigatorJsonMapper();
        mapper.futureNodes = new ArrayList<>(nav.futureNodes);
        mapper.pastNodes = new ArrayList<>(nav.pastNodes);
        mapper.planList = new ArrayList<>(nav.planList);
        mapper.currentVertex = nav.currentVertex;
        mapper.nextVertex = nav.nextVertex;

        return mapper;
    }

    public static ZooNavigatorJsonMapper fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ZooNavigatorJsonMapper.class);
    }

    public String toSerializedJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ZooNavigator toZooNavigator(Router router) {
        ZooNavigator zooNavigator = new ZooNavigator(planList, router);
        zooNavigator.pastNodes = new ArrayList<>(pastNodes);
        zooNavigator.futureNodes = new ArrayList<>(futureNodes);
        zooNavigator.currentVertex = currentVertex;
        zooNavigator.nextVertex = nextVertex;
        return zooNavigator;
    }
}
