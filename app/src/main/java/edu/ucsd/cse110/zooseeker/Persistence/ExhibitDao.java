package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ExhibitDao {

    @Transaction
    @Query("SELECT * FROM Exhibit")
    public List<ExhibitWithTags> getAllExhibitsWithTags();

    @Transaction
    @Query("SELECT * FROM Exhibit WHERE 'exhibitId'=:exhibitId")
    public ExhibitWithTags getExhibitWithTagsById(String exhibitId);
}
