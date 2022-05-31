package edu.ucsd.cse110.zooseeker.Route;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.location.LocationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ucsd.cse110.zooseeker.Location.LocationModel;
import edu.ucsd.cse110.zooseeker.Location.LocationPermissionChecker;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;
import edu.ucsd.cse110.zooseeker.RouteSummary.RouteSummary;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;


public class RouteActivity extends AppCompatActivity implements GPSSettingDialogFragment.DialogListener{

    // ViewModel
    RouteViewModel routeViewModel;
    LocationModel locationModel;

    // DAOs
    MainDatabase db;
    PlanItemDao planItemDao;

    // View Elements
    TextView routeTextView;
    TextView fromTextView;
    TextView toTextView;
    TextView routeLatitude;
    TextView routeLongitude;
    TextView uiMessage;
    Button gpsSettingButton;
    Button nextButton;
    Button skipButton;
    Button backButton;
    Button reverseButton;
    Button toggleDirectionsButton;
    Button deleteAllButton;
    Button rerouteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        getSupportActionBar().hide();

        // DADOs
        db = MainDatabase.getSingleton(this);
        planItemDao = db.planItemDao();

        // initialize all view elements
        routeTextView = findViewById(R.id.route_text_view);
        fromTextView = findViewById(R.id.route_from_text);
        toTextView = findViewById(R.id.route_to_text);
        routeLatitude = findViewById(R.id.route_latitude);
        routeLongitude = findViewById(R.id.route_longitude);
        gpsSettingButton = findViewById(R.id.route_gps_setting_button);
        nextButton = findViewById(R.id.route_next_button);
        backButton = findViewById(R.id.route_back_button);
        skipButton = findViewById(R.id.route_skip_button);
        reverseButton = findViewById(R.id.route_reverse_button);
        toggleDirectionsButton = findViewById(R.id.toggle_directions_button);
        deleteAllButton = findViewById(R.id.route_delete_all_button);
        rerouteButton = findViewById(R.id.route_reroute_button);
        uiMessage = findViewById(R.id.route_ui_message);

        // Get location permission from user
        LocationPermissionChecker locationPermissionChecker = new LocationPermissionChecker(this);
        boolean perm = locationPermissionChecker.ensurePermissions();
        Log.d("ROUTE ACTIVITY", "" + perm);

        // ViewModels
        routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
        locationModel = new ViewModelProvider(this).get(LocationModel.class);
        var provider = LocationManager.GPS_PROVIDER;
        var locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        locationModel.addLocationProviderSource(locationManager, provider);


        locationModel.getLastKnownCoords().observe(this, lastKnownCoord -> {
            Log.d("ROUTE ACTIVITY", "" + lastKnownCoord.lat + "" + lastKnownCoord.lng);
            routeViewModel.setRealCurrentLocationCoordinate(lastKnownCoord.lat, lastKnownCoord.lng);
        });

        routeViewModel.getIsDirectionDetailed().observe(this, isDirectionDetailed -> {
            String btnText = isDirectionDetailed ? "Detailed\nDirections" : "Brief\nDirections";
            toggleDirectionsButton.setText(btnText);
        });

        routeViewModel.getCurrentRouteToDisplay().observe(this, currentRouteToDisplay -> {
            routeTextView.setText(currentRouteToDisplay);
        });

        routeViewModel.getFromAndTo().observe(this, fromAndTo -> {
            fromTextView.setText(fromAndTo.first);
            toTextView.setText(fromAndTo.second);
        });

        routeViewModel.getCurrentLocationCoordinate().observe(this, currentLocationCoordinate -> {
            routeLatitude.setText("" + currentLocationCoordinate.first);
            routeLongitude.setText("" + currentLocationCoordinate.second);
        });

        routeViewModel.getEnableReroute().observe(this, enableReroute -> {
            rerouteButton.setClickable(enableReroute);
            rerouteButton.setText(enableReroute ? "Reroute Enabled" : "Reroute Disabled");
        });

        routeViewModel.getIsLocationMocked().observe(this, isLocationMocked -> {
            gpsSettingButton.setText(isLocationMocked ? "Mock GPS" : "Real GPS");
        });

        routeViewModel.getUIMessage().observe(this, uiMessage -> {
            this.uiMessage.setText(uiMessage);
        });

        gpsSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGPSSettingDialog();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                routeViewModel.next();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeViewModel.skip();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeViewModel.back();
            }
        });

        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeViewModel.reverse();
            }
        });

        toggleDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                routeViewModel.toggleIsDirectionDetailed();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planItemDao.nukeTable();
                finish();
            }
        });

        rerouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeViewModel.reroute();
            }
        });

        // Start Route Summary
        Intent intent = new Intent(this, RouteSummary.class);
        intent.putExtra("ZOONAVIGATOR", routeViewModel.zooNavigator);
        startActivity(intent);
    }

    public void showGPSSettingDialog() {
        boolean isMock = routeViewModel.getIsLocationMocked().getValue();
        double lat = routeViewModel.getCurrentLocationCoordinate().getValue().first;
        double log = routeViewModel.getCurrentLocationCoordinate().getValue().second;
        DialogFragment dialog = new GPSSettingDialogFragment(isMock, lat, log);
        dialog.show(getSupportFragmentManager(), "GPSSettingDialog");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, boolean isMock, double latitude, double longitude) {
        routeViewModel.setIsLocationMocked(isMock);
        if (isMock) {
            routeViewModel.setMockCurrentLocationCoordinate(latitude, longitude);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }
}