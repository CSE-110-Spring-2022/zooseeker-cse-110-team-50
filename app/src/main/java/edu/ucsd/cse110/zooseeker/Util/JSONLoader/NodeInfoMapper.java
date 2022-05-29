package edu.ucsd.cse110.zooseeker.Util.JSONLoader;

import android.util.Log;

import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceWithTags;

public class NodeInfoMapper {
    public String id;
    public String kind;
    public String name;
    public String parent_id;
    public List<String> tags;
    public Double lat;
    public Double lng;

    public Place toPlace() {
        Log.d("NodeInfoMapper", toString());
        return new Place(id, name, kind, parent_id, lng, lat);
    }

    @Override
    public String toString() {
        return "NodeInfoMapper{" +
                "id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", tags=" + tags +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
