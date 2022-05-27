package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "plan_list")
public class PlanItem {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "place_id")
    @SerializedName("place_id")
    @NonNull
    public String placeId;

    public PlanItem(@NonNull String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "PlanItem{" +
                "placeId='" + placeId + '\'' +
                '}';
    }
}
