package edu.ucsd.cse110.zooseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import edu.ucsd.cse110.zooseeker.Location.LocationPermissionChecker;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Plan.PlanFragment;
import edu.ucsd.cse110.zooseeker.Search.SearchFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainDatabase.getSingleton(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(this);

        askForLocationPermission();
    }


    SearchFragment searchFragment = new SearchFragment();
    PlanFragment planFragment = new PlanFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, searchFragment).commit();
                return true;

            case R.id.planFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, planFragment).commit();
                return true;
        }
        return false;
    }

    private void askForLocationPermission() {
        LocationPermissionChecker locationPermissionChecker = new LocationPermissionChecker(this);
        boolean isPermissionAlreadyGranted = locationPermissionChecker.ensurePermissions();
    }
}

//dummy comment