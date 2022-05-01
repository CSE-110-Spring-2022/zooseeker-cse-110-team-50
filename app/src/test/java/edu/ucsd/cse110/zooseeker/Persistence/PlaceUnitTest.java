package edu.ucsd.cse110.zooseeker.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
public class PlaceUnitTest {
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
    public void testInsert() {
        Place place = new Place("lion_kingdom", "Lion Kingdom", "exhibit");
        placeDao.insert(place);

        List<Place> allFoundPlaces = placeDao.getAll();
        Place foundExhibit = placeDao.get(place.placeId);
        assertEquals(allFoundPlaces.size(), 1);
        assertNotNull(foundExhibit);
        assertEquals(foundExhibit.name, "Lion Kingdom");
    }

    @Test
    public void testInsertWithTag() {
        Place place = new Place("lion_kingdom", "Lion Kingdom", "exhibit");
        placeDao.insert(place);


    }

}
