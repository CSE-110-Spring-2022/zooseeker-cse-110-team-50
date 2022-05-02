package edu.ucsd.cse110.zooseeker.Search;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.R;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    // Search Result Data
    private List<Place> searchResults = Collections.emptyList();

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Place place;

        // UI element refs
        private TextView resultNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            resultNameTextView = itemView.findViewById(R.id.search_result_item_name);
        }

        public Place getPlace() { return place; }

        public void setPlace(Place place) {
            this.place = place;
            this.resultNameTextView.setText(place.name);
        }
    }
}
