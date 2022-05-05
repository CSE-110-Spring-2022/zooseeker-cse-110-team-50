package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "plan_list")
public class PlanItem {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String placeId;

    @NonNull
    public double distance;

    public PlanItem(@NonNull String placeId, double distance) {
        this.placeId = placeId;
        this.distance = distance;
    }

    public void setDistance(@NonNull double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "PlanItem{" +
                "placeId='" + placeId + '\'' +
                ", distance=" + distance +
                '}';
    }
}
