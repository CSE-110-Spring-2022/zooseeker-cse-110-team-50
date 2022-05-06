package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class PlaceDao {

    @Insert
    public abstract void insert(Place place);

    @Insert
    public abstract void insertAll(List<Place> place);

    @Query("SELECT * FROM `place` WHERE `placeId`=:placeId")
    public abstract Place get(String placeId);

    @Query("SELECT * FROM `place`")
    public abstract List<Place> getAll();

    @Query("SELECT * FROM `place`")
    public abstract LiveData<List<Place>> getAllLive();

    @Transaction
    @Query("SELECT * FROM Place")
    public abstract List<PlaceWithTags> getAllExhibitsWithTags();

    @Transaction
    @Query("SELECT * FROM `place` WHERE 'exhibitId'=:exhibitId")
    public abstract PlaceWithTags getExhibitWithTagsById(String exhibitId);

    @Query("SELECT DISTINCT place.placeId, place.name, place.kind\n" +
            "FROM place_tag_cross_ref\n" +
            "JOIN place ON place.placeId = place_tag_cross_ref.placeId\n" +
            "JOIN tag ON tag.tagId = place_tag_cross_ref.tagId\n" +
            "WHERE tag.name LIKE '%' || :query || '%' OR place.name LIKE '%' || :query || '%'OR place.kind LIKE '%' || :query || '%'\n" +
            "ORDER BY place.name ASC\n")
    public abstract LiveData<List<Place>> nameAndTagSearch(String query);

//    @Transaction
//    void insertExhibitWithTags(PlaceWithTags exhibitWithTags) {
//        return;
//    }
}
