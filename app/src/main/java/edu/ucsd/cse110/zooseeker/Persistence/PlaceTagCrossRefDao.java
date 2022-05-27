package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PlaceTagCrossRefDao {
    @Insert
    void insert(PlaceTagCrossRef placeTagCrossRef);

    @Query("SELECT * FROM `place_tag_cross_ref` WHERE `placeId`=:placeId AND `tagId`=:tagId")
    PlaceTagCrossRef get(String placeId, long tagId);
}
