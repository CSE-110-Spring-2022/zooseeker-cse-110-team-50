package edu.ucsd.cse110.zooseeker.Util.JSONLoader;

import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceWithTags;

public class NodeInfoMapper {
    public String id;
    public String kind;
    public String name;
    public String parentId;
    public List<String> tags;
    public double lat;
    public double lng;

    public Place toPlace() {
        return new Place(id, name, kind, parentId, lng, lat);
    }
}
