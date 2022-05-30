package edu.ucsd.cse110.zooseeker.Route;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class RouteActivity extends AppCompatActivity {

    private List<Router.RoutePackage> pkgList;
    private int routeIndex = 0;
    private PlaceDao placeDao = MainDatabase.getSingleton(this).placeDao();
    boolean isDetailedDirections = true;

    ZooNavigator zooNavigator;

    // DAOs
    MainDatabase db;
    PlanItemDao planItemDao;

    // View Elements
    TextView routeTextView;
    TextView fromTextView;
    TextView toTextView;
    TextView routeLatitude;
    TextView routeLongitude;
    Button nextButton;
    Button skipButton;
    Button backButton;
    Button reverseButton;
    Button toggleDirectionsButton;
    Button deleteAllButton;

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
        nextButton = findViewById(R.id.route_next_button);
        backButton = findViewById(R.id.route_back_button);
        skipButton = findViewById(R.id.route_skip_button);
        reverseButton = findViewById(R.id.route_reverse_button);
        toggleDirectionsButton = findViewById(R.id.toggle_directions_button);
        deleteAllButton = findViewById(R.id.route_delete_all_button);

        // ViewModel
        RouteViewModel model = new ViewModelProvider(this).get(RouteViewModel.class);

        model.getIsDirectionDetailed().observe(this, isDirectionDetailed -> {
            String btnText = isDirectionDetailed ? "Detailed\nDirections" : "Brief\nDirections";
            toggleDirectionsButton.setText(btnText);
        });

        model.getCurrentRouteToDisplay().observe(this, currentRouteToDisplay -> {
            routeTextView.setText(currentRouteToDisplay);
        });

        model.getFromAndTo().observe(this, fromAndTo -> {
            fromTextView.setText(fromAndTo.first);
            toTextView.setText(fromAndTo.second);
        });

        model.getCurrentLocationCoordinate().observe(this, currentLocationCoordinate -> {
            routeLatitude.setText("" + currentLocationCoordinate.first);
            routeLongitude.setText("" + currentLocationCoordinate.second);
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                model.next();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.back();
            }
        });

        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        toggleDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                model.toggleIsDirectionDetailed();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planItemDao.nukeTable();
                finish();
            }
        });
    }
}