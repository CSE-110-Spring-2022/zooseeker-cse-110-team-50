package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"placeId", "tagId"}, indices = {@Index("tagId")})
public class PlaceTagCrossRef {

    @NonNull
    public String placeId;

    @NonNull
    public long tagId;

    public PlaceTagCrossRef(@NonNull String placeId, long tagId) {
        this.placeId = placeId;
        this.tagId = tagId;
    }
}
