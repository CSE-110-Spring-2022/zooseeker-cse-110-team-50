package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    long insert(Tag tag);

    @Query("SELECT * FROM `tag` WHERE `tagId`=:tagId")
    Tag get(long tagId);

    @Query("SELECT * FROM `tag`")
    List<Tag> getAll();
}
