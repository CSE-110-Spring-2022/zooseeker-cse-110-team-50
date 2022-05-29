package edu.ucsd.cse110.zooseeker.Search;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Collections;

import edu.ucsd.cse110.zooseeker.R;

public class SearchFragment extends Fragment {


    private RecyclerView searchResultRecyclerView;
    private SearchResultViewModel searchResultViewModel;
    private Button addButton;

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

        searchResultAdapter.setOnAddButtonClicked(searchResultViewModel::addToPlan);

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
                Log.d("SEARCH_BAR", "Search string: " + charSequence.toString());

                searchResultViewModel.search(charSequence.toString());

                // put the LiveData on the activity screen
                searchResultViewModel.getSearchResult()
                        .observe(getViewLifecycleOwner(), searchResultAdapter::setSearchResults);


            }

            @Override
            public void afterTextChanged(Editable editable) {
                // empty search bar
                if (TextUtils.isEmpty(editable.toString().trim()))
                    searchResultAdapter.setSearchResults(Collections.emptyList());
            }
        });

        return view;
    }

    public RecyclerView getSearchResultRecyclerView() {
        return searchResultRecyclerView;
    }

    public void onClickAddBtn(View view){
        //return;
    }

    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        searchResultViewModel = new ViewModelProvider(this).get(SearchResultViewModel.class);
//        // TODO: Use the ViewModel
//    }

}