package edu.ucsd.cse110.zooseeker.Search;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


        // Recycler View Init code
        searchResultViewModel = new ViewModelProvider(requireActivity()).get(SearchResultViewModel.class);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter();
        searchResultAdapter.setHasStableIds(true);
        searchResultViewModel.getSearchResult().observe(getViewLifecycleOwner(), searchResultAdapter::setSearchResults);

        searchResultRecyclerView = view.findViewById(R.id.search_result_list);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        searchResultRecyclerView.setAdapter(searchResultAdapter);

        // Search Bar Text Field Config
        EditText searchBar = view.findViewById(R.id.search_bar_text_field);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("SEARCH_BAR", charSequence.toString());
                if(charSequence.length() == 0){
                    searchResultViewModel.search("");
                }
                else{
                    searchResultViewModel.search(charSequence.toString());
                }

                // put the LiveData on the activity screen
                // searchResultViewModel.getSearchResult();
                searchResultViewModel.getSearchResult().observe(getViewLifecycleOwner(), searchResultAdapter::setSearchResults);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }



//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        searchResultViewModel = new ViewModelProvider(this).get(SearchResultViewModel.class);
//        // TODO: Use the ViewModel
//    }

}