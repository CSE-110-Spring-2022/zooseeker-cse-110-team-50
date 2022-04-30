package edu.ucsd.cse110.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExhibitListItemDao {
    @Insert
    long insert(ExhibitListItem exhibitListItem);

    @Query("SELECT * FROM `exhibit_list_items` WHERE `id`=:id")
    ExhibitListItem get(long id);

    @Query("SELECT * FROM `exhibit_list_items` ORDER BY `name`")
    List<ExhibitListItem> getAll();

    @Query("SELECT * FROM `exhibit_list_items` ORDER BY `name`")
    LiveData<List<ExhibitListItem>> getAllLive();

    @Query("SELECT `name` + 1 FROM `exhibit_list_items` ORDER BY `name` DESC LIMIT 1")
    int getOrderForAppend();

    @Insert
    List<Long> insertAll(List<ExhibitListItem> exhibitListItem);

    @Update
    int update(ExhibitListItem exhibitListItem);

    @Delete
    int delete(ExhibitListItem exhibitListItem);
}

