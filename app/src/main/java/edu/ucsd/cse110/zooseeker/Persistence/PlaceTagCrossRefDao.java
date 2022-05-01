package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface PlaceTagCrossRefDao {
    @Insert
    void insert(PlaceTagCrossRef placeTagCrossRef);
}
