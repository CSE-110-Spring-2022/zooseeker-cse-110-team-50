package edu.ucsd.cse110.zooseeker.RouteSummary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;
import edu.ucsd.cse110.zooseeker.R;

public class RouteSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_summary);

        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        ZooNavigator zooNavigator = (ZooNavigator) extras.getSerializable("ZOONAVIGATOR");

        String routeSummary = zooNavigator.getRoutePreview();

        TextView routeSummaryText = (TextView) findViewById(R.id.route_summary_view);
        routeSummaryText.setText(routeSummary);
        Button startRouteButton = (Button) findViewById(R.id.start_button);
        startRouteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}