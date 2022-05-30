package edu.ucsd.cse110.zooseeker.NewNavigator;

import java.util.Arrays;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Location.Coord;
import edu.ucsd.cse110.zooseeker.Persistence.Place;

public class MetaVertex {
    public String id;
    public String name;
    public boolean hasParent;
    public List<String> placeIds;
    public double lat, lng;
    public Coord coord;

    public MetaVertex(Place place) {
        if (place.parentId == null) {
            this.id = place.placeId;
            this.hasParent = false;
            this.lat = place.lat;
            this.lng = place.lng;
            this.name = place.name;
            this.coord = new Coord(lat, lng);
        } else {
            this.id = place.parentId;
            this.hasParent = true;
            placeIds = Arrays.asList(place.placeId);
        }
    }

    public boolean addPlace(Place place) {

        if (!hasParent)
            return false;

        if (this.placeIds.contains(place.placeId))
            return false;

        this.placeIds.add(place.placeId);
        return true;
    }
    public boolean setLatLng(double lat, double lng) {

        if (!hasParent)
            return false;

        this.lat = lat;
        this.lng = lng;
        this.coord = new Coord(lat, lng);
        return true;
    }

    public void setName(String nameInput){
        this.name = nameInput;
    }

    @Override
    public String toString() {
        return "MetaVertex{" +
                "id='" + id + '\'' +
                ", isParent=" + hasParent +
                ", placeIds=" + placeIds +
                ", lat=" + lat +
                ", lng=" + lng +
                ", Coord=" + coord +
                '}';
    }
}
