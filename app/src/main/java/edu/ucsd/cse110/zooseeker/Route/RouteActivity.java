package edu.ucsd.cse110.zooseeker.Route;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class RouteActivity extends AppCompatActivity {

    private List<Router.RoutePackage> pkgList;
    private int routeIndex = 0;
    private PlaceDao placeDao = MainDatabase.getSingleton(this).placeDao();
    boolean isDetailedDirections = true;
    TextView routeTextView;
    Button toggleDirectionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Button nextButton = findViewById(R.id.route_next_button);

        toggleDirectionsButton = findViewById(R.id.toggle_directions_button);

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                nextExhibit(v);
            }
        });

        toggleDirectionsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toggleDirections(v);
            }
        });

        JSONLoader.loadRawGraph(getApplicationContext());
        final Map<String, String> placeInfoMap = new HashMap<>();
        for (Place place: placeDao.getAll())
            placeInfoMap.put(place.placeId, place.name);


        Router router = Router.builder()
                .loadEdgeInfo(JSONLoader.loadEdgeInfo(getApplicationContext()))
                .loadFromRawGraph(JSONLoader.loadRawGraph(getApplicationContext()))
                .loadPlaceInfo(placeInfoMap)
                .build();

        MainDatabase db = MainDatabase.getSingleton(this);
        PlanItemDao planItemDao = db.planItemDao();
        List<PlanItem> allPlanItems = planItemDao.getAll();
        List<String> allNodes = allPlanItems.stream().map((item) -> item.placeId).collect(Collectors.toList());
        pkgList = router.route(allNodes);
        toggleDirectionsButton.setText("Brief\nDirections");
//        for (Router.RoutePackage pkg : pkgList) {
//            routeStr += pkg.toString() + "\n";
//        }

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
        if(routeIndex + 1 < pkgList.size()) routeIndex++;

        routeTextView = findViewById(R.id.route_text_view);
        if(isDetailedDirections){
            routeTextView.setText(pkgList.get(routeIndex).toStringDetailed());
        }
        else{
            routeTextView.setText(pkgList.get(routeIndex).toStringBrief());
        }
    }
}