package edu.ucsd.cse110.zooseeker.Route;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.jgrapht.GraphPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker.MainActivity;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader.JSONLoader;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

@Config(sdk = 31)
@RunWith(RobolectricTestRunner.class)
public class GraphTest {
    MainActivity activity;
    MainDatabase mainDb;
    PlaceDao placeDao;
    Router router;
    Map<String, String> placeInfoMap;

    /**
     * Sets up the graph and activity for testing.
     */
    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);

        Context context = ApplicationProvider.getApplicationContext();
        mainDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();

        placeDao = mainDb.placeDao();

        JSONLoader.loadTestRawGraph(getApplicationContext());

        placeInfoMap = new HashMap<>();
        for (Place place: placeDao.getAll())
            placeInfoMap.put(place.placeId, place.name);
        router = Router.builder()
                .loadEdgeInfo(JSONLoader.loadTestEdgeInfo(getApplicationContext()))
                .loadFromRawGraph(JSONLoader.loadTestRawGraph(getApplicationContext()))
                .loadPlaceInfo(placeInfoMap)
                .build();
        assertNotNull(router);
    }

    @After
    /**
     * Close the test database after each unit test.
     */
    public void closeDb() throws IOException {
        mainDb.close();
    }

    /**
     * Checks if putting the information into the router does not equal a null value.
     */
    @Test
    public void getShortestPathExists() {
        GraphPath<String, Router.EdgeWithId> pathActual
                = router.shortestPath("gators", "gorillas");
    }

    /**
     * Tests if the path is the shorted path between two places.
     */
    @Test
    public void testIsShortestPath() {
        GraphPath<String, Router.EdgeWithId> pathActual
                = router.shortestPath("gators", "gorillas");

        List<Router.EdgeWithId> edgeListActual = pathActual.getEdgeList();

        List<String> edgeListIDExpected = new ArrayList<String>();
        edgeListIDExpected.add("edge-5");
        edgeListIDExpected.add("edge-1");

        for (int i = 0; i < edgeListActual.size(); i++) {
            String idActual = edgeListActual.get(i).edgeId;
            assertEquals(edgeListIDExpected.get(i), idActual);
        }

    }

    /**
     * Checks if putting the information into the router is a null value
     * when a target vertex does not exist
     */
    @Test
    public void getPathTargetNotExists() {
        assertNull(router.shortestPath("gators", "no_a_place"));
    }

    /**
     * Checks if putting the information into the router is a null value
     * when a source vertex does not exist
     */
    @Test(expected = IllegalArgumentException.class)
    public void getPathSourceNotExists() {
        assertNull(router.shortestPath("no_a_place", "gators"));
    }
}
