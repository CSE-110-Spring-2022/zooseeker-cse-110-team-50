package edu.ucsd.cse110.zooseeker.Plan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import edu.ucsd.cse110.zooseeker.MainActivity;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;

@Config(sdk = 31)
@RunWith(RobolectricTestRunner.class)
public class PlanTest {

    private MainDatabase mainDb;
    private PlaceDao placeDao;
    private PlanItemDao planItemDao;
    private PlanViewModel planViewModel;

    /**
     * Create the Database
     */
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mainDb = Room.inMemoryDatabaseBuilder(context, MainDatabase.class)
                .allowMainThreadQueries()
                .build();

        placeDao = mainDb.placeDao();
        planItemDao = mainDb.planItemDao();
    }

    /**
     * Close the Database
     */
    @After
    public void closeDb() throws IOException {
        mainDb.close();
    }
    /**
     * Check if we can successfully get the plan fragment
     */
    @Test
    public void checkPlanFragmentWorks() {
        PlanFragment fragment = PlanFragment.newInstance();
        assertNotNull(fragment);
    }
    /**
     * Check if the fragment unsuccessfully gets the button if we do not call correctly
     */
    @Test
    public void checkIfButtonNull() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        //PlanFragment planFragment = new PlanFragment();
        //Fragment frag = startFragment(planFragment);
        //LayoutInflater inflater = new LayoutInflater(new Context());
        //activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, planFragment).commit();
        Button routeButton = activity.findViewById(R.id.start_route_button);

        assertNull(routeButton);

    }

    /**
     * Check if the DAO is working properly according to the plan fragment
     */
    @Test
    public void checkPlanItemDaoInsert(){
        //  MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        //  PlanFragment planFragment = PlanFragment.newInstance();
        //  assertNull(planFragment.planAmount);


        MainActivity activity = Robolectric.setupActivity(MainActivity.class);

        PlanFragment planFragment = new PlanFragment();

        //  Fragment frag = startFragment(planFragment);

        // LayoutInflater inflater = new LayoutInflater(activity::getApplicationContext());

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, planFragment).commit();

        PlanItem planItem = new PlanItem("lion_kingdom", -1);
        PlanItem planItem_2 = new PlanItem("arctic foxes", 20);
        PlanItem planItem_3= new PlanItem("entrance plaza", 30);

        planItemDao.insert(planItem);
        planItemDao.insert(planItem_2);
        planItemDao.insert(planItem_3);
        List<PlanItem> allFoundPlaces = planItemDao.getAll();

        assertEquals(3,allFoundPlaces.size() );

    }
    
    @Test
    public void checkIfPlanDeletion() {
        PlanItem planItem = new PlanItem("lion_kingdom", -1);
        PlanItem planItem_2 = new PlanItem("arctic foxes", 20);
        PlanItem planItem_3= new PlanItem("entrance plaza", 30);

        planItemDao.insert(planItem);
        planItemDao.insert(planItem_2);
        planItemDao.insert(planItem_3);

        planItemDao.nukeTable();

        List<PlanItem> allFoundPlaces = planItemDao.getAll();

        assertEquals(0,allFoundPlaces.size() );

    }
}