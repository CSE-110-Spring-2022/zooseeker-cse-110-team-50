package edu.ucsd.cse110.zooseeker.Route;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.zooseeker.MainActivity;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

@Config(sdk = 31)
@RunWith(RobolectricTestRunner.class)
public class GraphTest {
    MainActivity activity;
    PlaceDao placeDao;
    Router router;
    Map<String, String> placeInfoMap;

    /**
     * Sets up the graph and activity for testing.
     */
    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
        placeDao = MainDatabase.getSingleton(activity).placeDao();
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

    @Test
    public void getShortestPath() {
        // Check if putting the information into the router does not equal a null value
        assertNotNull(router.shortestPath("gators", "gorillas"));
    }
}
