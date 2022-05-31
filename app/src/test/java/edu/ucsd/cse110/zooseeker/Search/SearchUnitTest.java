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
//bad import, leads to build failure, does not exist according to error
//import androidx.fragment.app.testing.FragmentScenario;
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
import edu.ucsd.cse110.zooseeker.Search.SearchFragment;

@Config(sdk = 31)
@RunWith(RobolectricTestRunner.class)
public class SearchUnitTest {
    @Test
    /**
     * Check if there is a search fragment.
     */
    public void testPlanFragmentWorks() {
        SearchFragment fragment = SearchFragment.newInstance();
        assertNotNull(fragment);
    }

    @Test
    /**
     * Tests that the search text field is not null.
     */
    public void testSearchFieldNotNull() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        TextView routeButton = activity.findViewById(R.id.search_bar_text_field);
        assertNotNull(routeButton);
    }

}
