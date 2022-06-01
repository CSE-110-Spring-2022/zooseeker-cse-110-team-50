package edu.ucsd.cse110.zooseeker.Navigator;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertNotNull;

import android.app.Application;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker.MainActivity;
import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader.JSONLoader;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

@Config(sdk = 31)
@RunWith(RobolectricTestRunner.class)
public class ZooNavigatorUnitTest {
    public ZooNavigator zooNavigator;
    public Router router;
    public MainActivity activity;
    public MainDatabase mainDb;
    public Map<String, String> placeInfoMap;
    public PlaceDao placeDao;

    @Before
    /**
     * Creates router and zooNavigator with specific exhibits
     */
    public void setUp(){

        List<String> ids = new ArrayList<String>();
        activity = Robolectric.setupActivity(MainActivity.class);
        Context context = ApplicationProvider.getApplicationContext();

        mainDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();

        placeDao = mainDb.placeDao();

        JSONLoader.loadTestRawGraph(getApplicationContext());

        ids.add("crocodile");
        ids.add("gorilla");
        ids.add("flamingo");
        ids.add("hippo");
        ids.add("siamang");

        placeInfoMap = new HashMap<>();
        for (Place place: placeDao.getAll())
            placeInfoMap.put(place.placeId, place.name);

        router = new Router(
                JSONLoader.loadTestNodeInfo(context, "test/t1-ms1-delivery/sample_node_info.json"),
                JSONLoader.loadTestEdgeInfo(context, "test/t1-ms1-delivery/sample_edge_info.json"),
                JSONLoader.loadTestRawGraph(context, "test/t1-ms1-delivery/sample_zoo_graph.json")
        );
        assertNotNull(router);
        this.zooNavigator = new ZooNavigator(ids, router, 0, 0);
    }

    @After
    /**
     * Close test database after each unit test
     */
    public void closeDb() throws IOException{
        mainDb.close();
    }

    @Test
    /**
     * Tests ZooNavigator to make sure getClosestNode() gives the closest node from entrance
     */
    public void testGetClosestNode(){
        String closestNode = zooNavigator.getNextNode();
        Assert.assertEquals("flamingo", closestNode);
        //Assert.assertTrue(1 == 1);
    }

    @Test
    /**
     * Tests next and previous for ZooNavigator
     */
    public void testNextAndPrevious(){
        zooNavigator.next();
        String nextNextNode = zooNavigator.getNextNode();
        Assert.assertEquals("siamang", nextNextNode);
        zooNavigator.back();
        String prevNextNode = zooNavigator.getNextNode();
        Assert.assertEquals("flamingo", prevNextNode);
    }

    @Test
    /**
     * Tests skip functionality
     */
    public void testSkip(){
        zooNavigator.skip();
        String skipNextNode = zooNavigator.getNextNode();
        Assert.assertEquals("siamang", skipNextNode);
        zooNavigator.skip();
        String skipNode2 = zooNavigator.getNextNode();
        Assert.assertEquals("hippo",skipNode2);
    }
}
