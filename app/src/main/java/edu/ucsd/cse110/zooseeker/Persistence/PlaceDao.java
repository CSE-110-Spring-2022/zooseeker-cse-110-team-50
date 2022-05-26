package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public abstract class PlaceDao {

    private final TagDao tagDao;
    private final PlaceTagCrossRefDao placeTagCrossRefDao;

    public PlaceDao(MainDatabase database) {
        this.tagDao = database.tagDao();
        this.placeTagCrossRefDao = database.placeTagCrossRefDao();
    }

    @Insert
    public abstract void insert(Place place);

    @Insert
    public abstract void insertAll(List<Place> place);

    @Transaction
    public void insertWithTag(Place place, List<String> tags) {
        insert(place);
        for (String tag : tags) {
            if(tagDao.get(tag) == null) tagDao.insert(new Tag(tag));
            long newTagId = tagDao.get(tag).tagId;
            if (placeTagCrossRefDao.get(place.placeId, newTagId) == null) {
                placeTagCrossRefDao.insert(new PlaceTagCrossRef(place.placeId, newTagId));
            }
        }
    }

    @Query("SELECT * FROM `place` WHERE place_id=:placeId")
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

    @Query("SELECT DISTINCT place.place_id, place.name, place.kind\n" +
            "FROM place_tag_cross_ref\n" +
            "JOIN place ON place.place_id = place_tag_cross_ref.place_id\n" +
            "JOIN tag ON tag.tag_id = place_tag_cross_ref.tag_id\n" +
            "WHERE tag.name LIKE '%' || :query || '%' OR place.name LIKE '%' || :query || '%'OR place.kind LIKE '%' || :query || '%'\n" +
            "ORDER BY place.name ASC\n")
    public abstract LiveData<List<Place>> nameAndTagSearch(String query);

//    @Transaction
//    void insertExhibitWithTags(PlaceWithTags exhibitWithTags) {
//        return;
//    }
}
