package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "place")
public class Place {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String placeId;

    @NonNull
    public String name;
    public String kind;

    @Ignore
    public List<String> tags;

    public Place(@NonNull String placeId, String name, String kind) {
        this.placeId = placeId;
        this.name = name;
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Exhibit{" +
                "id='" + placeId + '\'' +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                '}';
    }
}
