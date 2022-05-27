package edu.ucsd.cse110.zooseeker.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

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
    /**
     * Create a database for testing before each unit test.
     */
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mainDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();

        placeDao = mainDb.placeDao();
        tagDao = mainDb.tagDao();
    }


    @After
    /**
     * Close the test database after each unit test.
     */
    public void closeDb() throws IOException {
        mainDb.close();
    }


    @Test
    /**
     * Make sure that there is nothing in the test database in the beginning.
     */
    public void testNothing() {
        List<Place> allFoundPlaces = placeDao.getAll();
        assertEquals(allFoundPlaces.size(), 0);
    }

    @Test
    /**
     * Insert a new plan into the database.
     */
    public void testInsert() {
        Place place = new Place("lion_kingdom",
                "Lion Kingdom",
                "exhibit",
                null,
                -0.0,
                -0.0
        );
        placeDao.insert(place);

        List<Place> allFoundPlaces = placeDao.getAll();
        Place foundExhibit = placeDao.get(place.placeId);
        assertEquals(allFoundPlaces.size(), 1);
        assertNotNull(foundExhibit);
        assertEquals(foundExhibit.name, "Lion Kingdom");
    }


    @Test(expected = SQLiteConstraintException.class)
    /**
     * Insert a duplicate plan into the database.
     * Make sure it throws an error.
     */
    public void testInsertDuplicate() {
        Place place = new Place("lion_kingdom",
                "Lion Kingdom",
                "exhibit",
                null,
                -0.0,
                -0.0
        );
        placeDao.insert(place);
        placeDao.insert(place);

        List<Place> allFoundPlaces = placeDao.getAll();
        assertEquals(allFoundPlaces.size(), 1);
    }

}
