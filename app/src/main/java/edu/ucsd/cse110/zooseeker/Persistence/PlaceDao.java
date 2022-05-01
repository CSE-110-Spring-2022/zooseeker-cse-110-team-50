package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public abstract class PlaceDao {

    @Insert
    abstract void insert(Place exhibit);

    @Insert
    abstract void insertAll(List<Place> exhibits);

    @Query("SELECT * FROM `place` WHERE `placeId`=:placeId")
    abstract Place get(String placeId);

    @Query("SELECT * FROM `place`")
    abstract List<Place> getAll();

    @Query("SELECT * FROM `place`")
    abstract LiveData<List<Place>> getAllLive();

    @Transaction
    @Query("SELECT * FROM Place")
    abstract List<PlaceWithTags> getAllExhibitsWithTags();

    @Transaction
    @Query("SELECT * FROM `place` WHERE 'exhibitId'=:exhibitId")
    abstract PlaceWithTags getExhibitWithTagsById(String exhibitId);

    @Transaction
    void insertExhibitWithTags(PlaceWithTags exhibitWithTags) {
        return;
    }
}
