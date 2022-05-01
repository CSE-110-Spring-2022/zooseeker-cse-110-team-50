package edu.ucsd.cse110.zooseeker.Search;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.R;

public class SearchFragment extends Fragment {


    private RecyclerView searchResultRecyclerView;
    private SearchResultViewModel searchResultViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.search_fragment, container, false);

        searchResultViewModel = new ViewModelProvider(requireActivity()).get(SearchResultViewModel.class);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter();
        searchResultAdapter.setHasStableIds(true);
        searchResultViewModel.getSearchResult().observe(getViewLifecycleOwner(), searchResultAdapter::setSearchResults);

        List<Place> list = new ArrayList<Place>();
        list.add(new Place("1", "Bear", "exhibit"));
        list.add(new Place("2", "Money", "exhibit"));
        list.add(new Place("3", "Parrot", "exhibit"));
        searchResultAdapter.setSearchResults(list);

        searchResultRecyclerView = view.findViewById(R.id.search_result_list);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        searchResultRecyclerView.setAdapter(searchResultAdapter);

        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        searchResultViewModel = new ViewModelProvider(this).get(SearchResultViewModel.class);
//        // TODO: Use the ViewModel
//    }

}