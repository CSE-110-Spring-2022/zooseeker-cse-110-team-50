package edu.ucsd.cse110.zooseeker.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
public class PlanUnitTest {
    private MainDatabase mainDb;
    private PlaceDao placeDao;
    private PlanItemDao planItemDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mainDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();

        placeDao = mainDb.placeDao();
        planItemDao = mainDb.planItemDao();
    }

    @After
    public void closeDb() throws IOException {
        mainDb.close();
    }

    @Test
    public void testNothing() {
        List<PlanItem> allFoundPlaces = planItemDao.getAll();
        assertEquals(allFoundPlaces.size(), 0);
    }

    @Test
    public void testInsert() {
        PlanItem planItem = new PlanItem("lion_kingdom", -1);
        planItemDao.insert(planItem);

        List<PlanItem> allFoundPlaces = planItemDao.getAll();
        PlanItem foundExhibit = planItemDao.get(planItem.placeId);
        assertEquals(allFoundPlaces.size(), 1);
        assertNotNull(foundExhibit);
        assertEquals(new Double(foundExhibit.distance), new Double(-1));
    }

    @Test
    public void testUpdate() {
        String placeId = "lion_kingdom";
        PlanItem planItem = new PlanItem(placeId, -1);
        planItemDao.insert(planItem);
        planItemDao.update(new PlanItem(placeId, -2));

        List<PlanItem> allFoundPlaces = planItemDao.getAll();
        PlanItem foundExhibit = planItemDao.get(planItem.placeId);
        assertEquals(allFoundPlaces.size(), 1);
        assertNotNull(foundExhibit);
        assertEquals(new Double(foundExhibit.distance), new Double(-2));
    }

    @Test
    public void testDelete() {
        PlanItem planItem = new PlanItem("lion_kingdom", -1);
        planItemDao.insert(planItem);

        planItemDao.delete(planItem);
        List<PlanItem> allFoundPlaces = planItemDao.getAll();
        assertEquals(allFoundPlaces.size(), 0);

    }


}
