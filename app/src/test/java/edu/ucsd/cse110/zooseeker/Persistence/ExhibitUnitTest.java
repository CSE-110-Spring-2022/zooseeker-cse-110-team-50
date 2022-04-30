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
public class ExhibitUnitTest {
    private MainDatabase mainDb;
    private ExhibitDao exhibitDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mainDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();

        exhibitDao = mainDb.exhibitDao();
    }

    @After
    public void closeDb() throws IOException {
        mainDb.close();
    }

    @Test
    public void testInsert() {
        Exhibit exhibit = new Exhibit("lion_kingdom", "Lion Kingdom", "exhibit");
        exhibitDao.insert(exhibit);

        List<Exhibit> allFoundExhibits = exhibitDao.getAll();
        Exhibit foundExhibit = exhibitDao.get(exhibit.exhibitId);
        assertEquals(allFoundExhibits.size(), 1);
        assertNotNull(foundExhibit);
        assertEquals(foundExhibit.name, "Lion Kingdom");
    }

}
