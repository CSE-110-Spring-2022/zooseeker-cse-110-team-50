package edu.ucsd.cse110.zooseeker.Persistence;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TagUnitTest {
    private MainDatabase mainDb;
    private PlaceDao placeDao;
    private TagDao tagDao;

    /*
     * Method to create database and the necessary Dao declarations for testing
     */

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mainDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();

        placeDao = mainDb.placeDao();
        tagDao = mainDb.tagDao();
    }

    /*
     * Method to close database
     */
    @After
    public void closeDb() throws IOException {
        mainDb.close();
    }

    /*
     *Tests the Tag insert() method to confirm that the tag is successfully added
     * Returns true if the first Tag's ID is the same as the inserted id and
     * if the amount of tags is increased
     */
    @Test
    public void insertTagTest() {
        Tag tag = new Tag("animal");
        long newTagId = tagDao.insert(tag);

        List<Tag> allTags = tagDao.getAll();
        assertEquals(allTags.size(), 1);
        Tag foundTag = tagDao.get(newTagId);
        assertEquals(foundTag.name, "animal");
    }

    /*
     *Tests the Tag insertAll() method to confirm that the tags are successfully added
     * Returns true if the Tag names are the same as the inserted id and
     * if the amount of tags is increased
     */
    @Test
    public void insertAllTagTest(){
        Tag tag1 = new Tag("mammal");
        Tag tag2 = new Tag("bird");
        Tag tag3 = new Tag("reptile");
        ArrayList<Tag> tags = new ArrayList<Tag>();
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);

        List<Long> allTags = tagDao.insertAll(tags);
        assertEquals(allTags.size(), 3);

    assertEquals(tagDao.get(allTags.get(0)).name, "mammal");
    assertEquals(tagDao.get(allTags.get(1)).name, "bird");
    assertEquals(tagDao.get(allTags.get(2)).name, "reptile");
    }

}
