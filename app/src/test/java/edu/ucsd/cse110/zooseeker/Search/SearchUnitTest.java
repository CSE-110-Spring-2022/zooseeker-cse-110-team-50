package edu.ucsd.cse110.zooseeker.Search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader;

import edu.ucsd.cse110.zooseeker.R;


@RunWith(AndroidJUnit4.class)
public class SearchUnitTest {

    MainDatabase testDb;
    PlaceDao placeDao;

    /**
     * Every time a test in this class is run,
     * the database will be reset to the contents of our JSON asset.
     */

    @Before
    public void resetDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();
        MainDatabase.injectDatabase(testDb);

        int NUM_TESTS = 10;
        List<Place> places = new ArrayList<Place>();
        for (int i = 0; i < NUM_TESTS; i++) {
            String id = "" + i;
            String name = "test" + i;
            places.add(new Place(id, name, "test"));
        }
        placeDao = testDb.placeDao();
        placeDao.insertAll(places);
    }

    @Test
    public void testSearchExistingItem() {
        FragmentScenario<SearchFragment> scenario
                = FragmentScenario.launch(SearchFragment.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

//        scenario.onFragment(searchFragment -> {
//            RecyclerView recyclerView = searchFragment.getSearchResultRecyclerView();
//            RecyclerView.ViewHolder firstVH = recyclerView;
//            assertNotNull(firstVH);
//            long id = firstVH.getItemId();
//
//            EditText searchText = firstVH.itemView.findViewById(R.id.search_bar_text_field);
//            searchText.requestFocus();
//            searchText.setText("test1");
//            searchText.clearFocus();
//
//            Place editedItem = placeDao.get("test1");
//            assertEquals("test1", editedItem.name);
//        });

    }

    @Test
    public void testFails() {
        FragmentScenario<SearchFragment> scenario
                = FragmentScenario.launch(SearchFragment.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onFragment(searchFragment -> {
            RecyclerView recyclerView = searchFragment.getSearchResultRecyclerView();
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForLayoutPosition(-1);
            assertNull(firstVH);
//            long id = firstVH.getItemId();
//
//            EditText searchText = firstVH.itemView.findViewById(R.id.search_bar_text_field);
//            searchText.requestFocus();
//            searchText.setText("test1");
//            searchText.clearFocus();
//
//            Place editedItem = placeDao.get("test1");
//            assertEquals("test1", editedItem.name);
        });

    }

}