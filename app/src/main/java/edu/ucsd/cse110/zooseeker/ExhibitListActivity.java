package edu.ucsd.cse110.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class ExhibitListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ExhibitListItem> exhibits = ExhibitListItem.loadJSON(this, "sample_node_info.json");
        Log.d("ExhibitListActivity", exhibits.toString());
        ExhibitListAdapter adapter = new ExhibitListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.exhibit_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setExhibitListItems(ExhibitListItem.loadJSON(this, "sample_node_info.json"));
    }
}
