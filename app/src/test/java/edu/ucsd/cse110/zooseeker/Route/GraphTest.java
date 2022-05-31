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
import edu.ucsd.cse110.zooseeker.Util.Router.EdgeWithId;
import edu.ucsd.cse110.zooseeker.Util.Router.LegacyRouter;
import edu.ucsd.cse110.zooseeker.Util.Router.MetaNode;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

@Config(sdk = 31)
@RunWith(RobolectricTestRunner.class)
public class GraphTest {
    public MainActivity activity;
    public MainDatabase mainDb;
    public PlaceDao placeDao;
    public Router router;
    public Map<String, String> placeInfoMap;
    public final String ENTRANCE_EXIT_GATE = "entrance_exit_gate";

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
        router = new Router(
                JSONLoader.loadTestNodeInfo(context, "test/t1-ms1-delivery/sample_node_info.json"),
                JSONLoader.loadTestEdgeInfo(context, "test/t1-ms1-delivery/sample_edge_info.json"),
                JSONLoader.loadTestRawGraph(context, "test/t1-ms1-delivery/sample_zoo_graph.json")
        );
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
        List<EdgeWithId> pathActual
                = router.shortestPath(ENTRANCE_EXIT_GATE, "fern_canyon");
        assertNotNull(pathActual);

        // a child exhibit
        pathActual = router.shortestPath(ENTRANCE_EXIT_GATE, "toucan");
        assertNotNull(pathActual);
    }

    /**
     * Tests if the path is the shorted path between two vertices.
     */
    @Test
    public void testIsShortestPathToExhibit() {
        List<EdgeWithId> edgeListActual
                = router.shortestPath("entrance_exit_gate", "koi");

        List<String> edgeListIDExpected = new ArrayList<String>();
        edgeListIDExpected.add("gate_to_front");
        edgeListIDExpected.add("front_to_lagoon1");
        edgeListIDExpected.add("lagoon1_to_koi");

        for (int i = 0; i < edgeListActual.size(); i++) {
            String idActual = edgeListActual.get(i).id;
            assertEquals(edgeListIDExpected.get(i), idActual);
        }

    }

    /**
     * Tests if the paths to the exhibit group and one of its child exhibits are identical
     */
    @Test
    public void testIsShortestPathToExhibitGroup() {
        List<EdgeWithId> edgeListToChild
                = router.shortestPath("entrance_exit_gate", "toucan");

        List<EdgeWithId> edgeListToExhibitGroup
                = router.shortestPath("entrance_exit_gate", "parker_aviary");

        assertEquals(edgeListToChild.size(), edgeListToExhibitGroup.size());

        for (int i = 0; i < edgeListToChild.size(); i++) {
            assertEquals(edgeListToChild.get(i).id, edgeListToExhibitGroup.get(i).id);
        }

    }

    /**
     * Checks if putting the information into the router is a null value
     * when a target vertex does not exist
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPathTargetNotExists() {
        assertNull(router.shortestPath("gators", "no_a_place"));
    }

    /**
     * Checks if putting the information into the router is a null value
     * when a source vertex does not exist
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPathSourceNotExists() {
        assertNull(router.shortestPath("no_a_place", "gators"));
    }


    @Test
    public void testPreviewExists() {
        List<String> exhibitToVisit = new ArrayList<>();
        exhibitToVisit.add("siamang");
        exhibitToVisit.add("hippo");
        exhibitToVisit.add("crocodile");
        exhibitToVisit.add("owens_aviary");
        exhibitToVisit.add("toucan");
        String route = router.routePreview(ENTRANCE_EXIT_GATE, ENTRANCE_EXIT_GATE, exhibitToVisit);
//        assertNotNull(route);
    }
}
