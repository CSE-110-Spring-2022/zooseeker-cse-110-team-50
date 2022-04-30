package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exhibit")
public class Exhibit {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String exhibitId;

    @NonNull
    public String name;
    public String kind;

    Exhibit(@NonNull String exhibitId, String name, String kind) {
        this.exhibitId = exhibitId;
        this.name = name;
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Exhibit{" +
                "id='" + exhibitId + '\'' +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                '}';
    }
}
