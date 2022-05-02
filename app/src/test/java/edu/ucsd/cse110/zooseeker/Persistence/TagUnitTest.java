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
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TagUnitTest {
    private MainDatabase mainDb;
    private PlaceDao placeDao;
    private TagDao tagDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mainDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();

        placeDao = mainDb.placeDao();
        tagDao = mainDb.tagDao();
    }

    @After
    public void closeDb() throws IOException {
        mainDb.close();
    }

    @Test
    public void insertTagTest() {
        Tag tag = new Tag("animal");
        long newTagId = tagDao.insert(tag);

        List<Tag> allTags = tagDao.getAll();
        assertEquals(allTags.size(), 1);
        Tag foundTag = tagDao.get(newTagId);
        assertEquals(foundTag.name, "animal");
    }

}
