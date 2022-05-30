package edu.ucsd.cse110.zooseeker.Route;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import edu.ucsd.cse110.zooseeker.Navigator.*;
import edu.ucsd.cse110.zooseeker.NewNavigator.RouteMaker;
import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader.JSONLoader;
import edu.ucsd.cse110.zooseeker.Util.Router.RawGraph;
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
        nextButton = findViewById(R.id.route_next_button);
        backButton = findViewById(R.id.route_back_button);
        skipButton = findViewById(R.id.route_skip_button);
        reverseButton = findViewById(R.id.route_reverse_button);
        toggleDirectionsButton = findViewById(R.id.toggle_directions_button);
        deleteAllButton = findViewById(R.id.route_delete_all_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                nextExhibit(v);
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
                previousExhibit(view);
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
                toggleDirections(v);
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planItemDao.nukeTable();
                finish();
            }
        });



        Context context = getApplicationContext();
        zooNavigator = new ZooNavigator(placeDao, planItemDao, context);

        pkgList = zooNavigator.getPkgList();
        routeTextView = findViewById(R.id.route_text_view);
        if(isDetailedDirections){
            routeTextView.setText(pkgList.get(routeIndex).toStringDetailed());
        }
        else{
            routeTextView.setText(pkgList.get(routeIndex).toStringBrief());
        }
    }

    public void toggleDirections(View v){
        if(isDetailedDirections){
            routeTextView.setText(pkgList.get(routeIndex).toStringBrief());
            isDetailedDirections = false;
            toggleDirectionsButton.setText("Detailed\nDirections");
        }
        else{
            routeTextView.setText(pkgList.get(routeIndex).toStringDetailed());
            isDetailedDirections = true;
            toggleDirectionsButton.setText("Brief\nDirections");
        }
    }

    public void nextExhibit(View v){
        if (routeIndex + 1 < pkgList.size()) routeIndex++;

        if (isDetailedDirections)
            routeTextView.setText(pkgList.get(routeIndex).toStringDetailed());
        else
            routeTextView.setText(pkgList.get(routeIndex).toStringBrief());
    }

    public void previousExhibit(View v){
        if(routeIndex - 1 >= 0) routeIndex--;

        if (isDetailedDirections)
            routeTextView.setText(pkgList.get(routeIndex).toStringDetailed());
        else
            routeTextView.setText(pkgList.get(routeIndex).toStringBrief());

    }
}