package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public abstract class ExhibitDao {

    @Insert
    abstract void insert(Exhibit exhibit);

    @Insert
    abstract void insertAll(List<Exhibit> exhibits);

    @Query("SELECT * FROM `exhibit` WHERE `exhibitId`=:exhibitId")
    abstract Exhibit get(String exhibitId);

    @Query("SELECT * FROM `exhibit`")
    abstract List<Exhibit> getAll();

    @Transaction
    @Query("SELECT * FROM Exhibit")
    abstract List<ExhibitWithTags> getAllExhibitsWithTags();

    @Transaction
    @Query("SELECT * FROM Exhibit WHERE 'exhibitId'=:exhibitId")
    abstract ExhibitWithTags getExhibitWithTagsById(String exhibitId);

    @Transaction
    void insertExhibitWithTags(ExhibitWithTags exhibitWithTags) {
        return;
    }
}
