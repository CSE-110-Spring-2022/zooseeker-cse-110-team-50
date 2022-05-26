package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "place")
public class Place {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "place_id")
    @SerializedName("place_id")
    @NonNull
    public String placeId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @NonNull
    public String name;

    @ColumnInfo(name = "kind")
    @SerializedName("kind")
    @NonNull
    public String kind;

    @ColumnInfo(name = "parent_id")
    @SerializedName("parent_id")
    public String parentId;

    @ColumnInfo(name = "lng")
    @SerializedName("lng")
    public Double lng;

    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    public Double lat;

    @Ignore
    public List<String> tags;


    public Place(
            @NonNull String placeId,
            @NonNull String name,
            @NonNull String kind,
            String parentId,
            Double lng,
            Double lat) {
        this.placeId = placeId;
        this.name = name;
        this.kind = kind;
        this.parentId = parentId;
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId='" + placeId + '\'' +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", parentId='" + parentId + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
