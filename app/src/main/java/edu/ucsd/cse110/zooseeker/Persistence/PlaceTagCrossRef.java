package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "place_tag_cross_ref",
        primaryKeys = {"place_id", "tag_id"},
        indices = {@Index("tag_id")})
public class PlaceTagCrossRef {

    @NonNull
    @ColumnInfo(name = "place_id")
    @SerializedName("place_id")
    public String placeId;

    @NonNull
    @ColumnInfo(name = "tag_id")
    @SerializedName("tag_id")
    public long tagId;

    public PlaceTagCrossRef(@NonNull String placeId, long tagId) {
        this.placeId = placeId;
        this.tagId = tagId;
    }
}
