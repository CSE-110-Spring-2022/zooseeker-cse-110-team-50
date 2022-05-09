package edu.ucsd.cse110.zooseeker.Route;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;
import edu.ucsd.cse110.zooseeker.Util.JSONLoader;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class RouteActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Router.RoutePackage> pkgList;
    private int routeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Button nextButton = findViewById(R.id.route_next_button);
        nextButton.setOnClickListener(this);

        JSONLoader.loadRawGraph(getApplicationContext());

        Router router = Router.builder()
                .loadEdgeInfo(JSONLoader.loadEdgeInfo(getApplicationContext()))
                .loadFromRawGraph(JSONLoader.loadRawGraph(getApplicationContext()));

        MainDatabase db = MainDatabase.getSingleton(this);
        PlanItemDao planItemDao = db.planItemDao();
        List<PlanItem> allPlanItems = planItemDao.getAll();
        List<String> allNodes = allPlanItems.stream().map((item) -> item.placeId).collect(Collectors.toList());
        pkgList = router.route(allNodes);
//        for (Router.RoutePackage pkg : pkgList) {
//            routeStr += pkg.toString() + "\n";
//        }

        TextView routeTextView = findViewById(R.id.route_text_view);
        routeTextView.setText(pkgList.get(routeIndex).toString());
    }

    @Override
    public void onClick(View view) {
        if(routeIndex + 1 < pkgList.size()) routeIndex++;

        TextView routeTextView = findViewById(R.id.route_text_view);
        routeTextView.setText(pkgList.get(routeIndex).toString());
    }
}