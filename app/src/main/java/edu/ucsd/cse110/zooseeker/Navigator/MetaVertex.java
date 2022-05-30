package edu.ucsd.cse110.zooseeker.Navigator;

import java.util.Arrays;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.Place;

public class MetaVertex {
    public String id;
    public boolean isParent;
    public List<String> placeIds;
    public Double lat, lng;

    public MetaVertex(Place place, Double lat, Double lng) {
        if (place.parentId == null) {
            this.id = place.placeId;
            this.isParent = true;
        } else {
            this.id = place.parentId;
            this.isParent = false;
        }

        placeIds = Arrays.asList(place.placeId);
        this.lat = lat;
        this.lng = lng;
    }

    public boolean addPlace(Place place) {

        if (!isParent)
            return false;

        if (this.placeIds.contains(place.placeId))
            return false;

        this.placeIds.add(place.placeId);
        return true;
    }

    @Override
    public String toString() {
        return "MetaVertex{" +
                "id='" + id + '\'' +
                ", isParent=" + isParent +
                ", placeIds=" + placeIds +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
