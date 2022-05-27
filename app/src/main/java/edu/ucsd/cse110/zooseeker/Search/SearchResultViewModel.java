package edu.ucsd.cse110.zooseeker.Search;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;

public class SearchResultViewModel extends AndroidViewModel {

    private LiveData<List<Place>> searchResult;
    private PlaceDao placeDao;
    private LiveData<String> liveQuery = new MutableLiveData<>("");

    private PlanItemDao planItemDao;

    public SearchResultViewModel(@NonNull Application application) {
        super(application);
        MainDatabase db = MainDatabase.getSingleton(getApplication().getApplicationContext());
        placeDao = db.placeDao();
        planItemDao = db.planItemDao();
    }

    public LiveData<List<Place>> getSearchResult() {
        if(searchResult == null) search("");
        return searchResult;
    }


    public void search(String query) {

        // Returns empty list for empty search query
        if(query.trim().equals("") || query.length() == 0) {
            searchResult = new MutableLiveData<List<Place>>();
//            searchResult = placeDao.nameAndTagSearch(query);
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

    public void addToPlan(Place place, double distance) {
        PlanItem newPlanItem = new PlanItem(place.placeId);
        planItemDao.insert(newPlanItem);
    }

    public void addToPlan(Place place) {
        Log.d("SearchResultViewModel", "addToPlan: " + place);
        PlanItem newPlanItem = new PlanItem(place.placeId);
        // TODO: set to a meaningful value from the algorithm
        planItemDao.insert(newPlanItem);
    }
}