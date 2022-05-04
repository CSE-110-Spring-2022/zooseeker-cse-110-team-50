package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlanItemDao {
    @Insert
    long insert(PlanItem planItem);

    @Query("SELECT * FROM `plan_list` WHERE `placeId`=:placeId")
    PlanItem get(String placeId);

    @Query("SELECT * FROM `plan_list`")
    List<PlanItem> getAll();
}

