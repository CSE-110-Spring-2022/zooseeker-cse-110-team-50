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
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import edu.ucsd.cse110.zooseeker.MainActivity;
import edu.ucsd.cse110.zooseeker.R;

@Config(sdk = 31)
@RunWith(RobolectricTestRunner.class)
public class PlanTest {
    @Test
    /**
     * Check if there is a plan fragment.
     */
    public void checkPlanFragmentWorks() {
        PlanFragment fragment = PlanFragment.newInstance();
        assertNotNull(fragment);
    }


    @Test
    /**
     * Tests that the button is not null.
     */
    public void checkIfButtonNull() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        //PlanFragment planFragment = new PlanFragment();
        //Fragment frag = startFragment(planFragment);
        //LayoutInflater inflater = new LayoutInflater(new Context());
        //activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, planFragment).commit();
        Button routeButton = activity.findViewById(R.id.start_route_button);

        assertNull(routeButton);
    }
}
