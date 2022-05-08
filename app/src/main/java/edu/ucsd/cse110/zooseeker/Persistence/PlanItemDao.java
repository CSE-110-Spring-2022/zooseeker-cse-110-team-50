package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlanItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(PlanItem planItem);

    @Query("SELECT * FROM `plan_list` WHERE `placeId`=:placeId")
    PlanItem get(String placeId);

    @Query("SELECT * FROM `plan_list` ORDER BY `distance` ASC")
    List<PlanItem> getAll();

    @Query("SELECT * FROM `plan_list` ORDER BY `distance` ASC")
    LiveData<List<PlanItem>> getAllLive();

    @Update
    int update(PlanItem planItem);

    @Delete
    int delete(PlanItem planItem);

    @Query("DELETE FROM plan_list")
    void nukeTable();
}

