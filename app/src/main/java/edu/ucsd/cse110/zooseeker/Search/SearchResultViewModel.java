package edu.ucsd.cse110.zooseeker.Search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.Place;

public class SearchResultViewModel extends ViewModel {

    private LiveData<List<Place>> searchResult;

    public LiveData<List<Place>> getSearchResult() {
        if (true) loadSearchResult();
        return searchResult;
    }

    private void loadSearchResult() {
        List<Place> list = new ArrayList<Place>();
        list.add(new Place("1", "Bear", "exhibit"));
        list.add(new Place("2", "Money", "exhibit"));
        list.add(new Place("3", "Parrot", "exhibit"));
        LiveData<List<Place>> liveData = new MutableLiveData<List<Place>>(list);
        searchResult = liveData;
    }
}