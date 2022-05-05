package edu.ucsd.cse110.zooseeker.Search;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;

public class SearchResultViewModel extends AndroidViewModel {

    private LiveData<List<Place>> searchResult;
    private PlaceDao placeDao;
    private LiveData<String> liveQuery = new MutableLiveData<>("");

    public SearchResultViewModel(@NonNull Application application) {
        super(application);
        MainDatabase db = MainDatabase.getSingleton(getApplication().getApplicationContext());
        placeDao = db.placeDao();
    }

    public LiveData<List<Place>> getSearchResult() {
        if(searchResult == null) search("");
        return searchResult;
    }


    public void search(String query) {

        // Returns empty list for empty search query
        if(query.trim().equals("") || query.length() == 0) {
            searchResult = new MutableLiveData<List<Place>>();
            searchResult = placeDao.nameAndTagSearch(query);
            return;
        }

        // By not putting the one above, we query all of the
        // exhibits, which might be a better design choice here
        // since someone can see everything that the zoo offers

        // As of right now this just searches the name, but we should
        // figure out how to also search by tag and potentially part of
        // the name as well.
        searchResult = placeDao.nameAndTagSearch(query);
    }
}