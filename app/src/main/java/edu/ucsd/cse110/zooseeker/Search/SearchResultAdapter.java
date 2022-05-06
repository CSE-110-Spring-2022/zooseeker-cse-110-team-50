package edu.ucsd.cse110.zooseeker.Search;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    // Search Result Data
    private List<Place> searchResults = Collections.emptyList();
    private Consumer<Place> onAddButtonClicked;  // for checkbox

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.search_result_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setPlace(searchResults.get(position));
    }

    public void setSearchResults(List<Place> searchResults) {
        this.searchResults.clear();
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void setOnAddButtonClicked(Consumer<Place> onAddButtonClicked) {
        this.onAddButtonClicked = onAddButtonClicked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Place place;
        private final Button addButton;

        // UI element refs
        private TextView resultNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.resultNameTextView = itemView.findViewById(R.id.search_result_item_name);
            this.addButton = itemView.findViewById(R.id.add_btn);

            this.addButton.setOnClickListener(view -> {
                onAddButtonClicked.accept(place);
            });

        }

        public Place getPlace() { return place; }

        public void setPlace(Place place) {
            this.place = place;
            this.resultNameTextView.setText(place.name);
        }


    }
}
