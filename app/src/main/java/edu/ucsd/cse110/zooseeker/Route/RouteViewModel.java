package edu.ucsd.cse110.zooseeker.Route;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import edu.ucsd.cse110.zooseeker.Location.LocationModel;
import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.RouteSummary.RouteSummary;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class RouteViewModel extends AndroidViewModel {

    // Live Data
    private MutableLiveData<Boolean> isDirectionDetailed;
    private MutableLiveData<String> currentRouteToDisplay;
    private MutableLiveData<Pair<String, String>> fromAndTo;
    private MutableLiveData<Pair<Double, Double>> currentLocationCoordinate;
    private MutableLiveData<Boolean> isLocationMocked;
    private LocationModel locationModel;


    // DAOs
    private MainDatabase mainDb;
    private PlaceDao placeDao;
    private PlanItemDao planItemDao;

    // Navigator
    public ZooNavigator zooNavigator;

    // Navigator related user data
    private int currentRouteIndex = 0;


    // Ctor
    public RouteViewModel(@NonNull Application application) {
        super(application);
        //this.locationModel = new ViewModelProvider(this).get(LocationModel.class);

        // initialize DataBase related fields
        Context context = getApplication().getApplicationContext();
        mainDb = MainDatabase.getSingleton(context);
        placeDao = mainDb.placeDao();
        planItemDao = mainDb.planItemDao();

        // initialize navigator
        List<PlanItem> planItemList = planItemDao.getAll();
        List<String> zooNavigatorIds = new ArrayList<>();
        for(PlanItem planItem : planItemList){
            Place place = placeDao.get(planItem.placeId);
            if(place.parentId == null){
                zooNavigatorIds.add(place.placeId);
            }
            else{
                if(!zooNavigatorIds.contains(place.parentId)){
                    zooNavigatorIds.add(place.parentId);
                }
            }
        }
        Router router = new Router(getApplication());
        zooNavigator = new ZooNavigator(zooNavigatorIds, router);


        // initialize LiveData
        isDirectionDetailed = new MutableLiveData<>(false);
        fromAndTo = new MutableLiveData<>(new Pair<>("", ""));
        currentLocationCoordinate = new MutableLiveData<>();
        isLocationMocked = new MutableLiveData<>();
        setCurrentLocationCoordinate(17.123124123, 17.8787);
        setIsLocationMocked(false);
    }

    public void setViewModel(LocationModel locationModel){
        this.locationModel = locationModel;
    }

    public LiveData<Boolean> getIsDirectionDetailed() {
        return isDirectionDetailed;
    }

    public LiveData<String> getCurrentRouteToDisplay() {
        if (currentRouteToDisplay == null) {
            currentRouteToDisplay = new MutableLiveData<>("");
            updateCurrentRouteToDisplay();
        }
        return currentRouteToDisplay;
    }

    public LiveData<Pair<String, String>> getFromAndTo() {
        updateCurrentRouteToDisplay();
        return fromAndTo;
    }

    public LiveData<Pair<Double, Double>> getCurrentLocationCoordinate() {
        return currentLocationCoordinate;
    }

    public LiveData<Boolean> getIsLocationMocked() {
        return getIsLocationMocked();
    }

    public void setIsLocationMocked(boolean isLocationMocked) {
        this.isLocationMocked.setValue(isLocationMocked);
    }

    public void setRealCurrentLocationCoordinate(double latitude, double longitude) {
        if (!isLocationMocked.getValue())
            setCurrentLocationCoordinate(latitude, longitude);
    }

    // Set coordinate pair interface
    public void setCurrentLocationCoordinate(Pair<Double, Double> coordinate) {
        currentLocationCoordinate.setValue(coordinate);
    }

    // Set Coordinate Double, Double interface
    public void setCurrentLocationCoordinate(Double latitude, Double longitude) {
        currentLocationCoordinate.setValue(new Pair<>(latitude, longitude));
    }

    public void toggleIsDirectionDetailed() {
        isDirectionDetailed.setValue(!isDirectionDetailed.getValue());
        updateCurrentRouteToDisplay();
    }

    public void next() {
        zooNavigator.next();
        updateCurrentRouteToDisplay();
    }

    public void back() {
        zooNavigator.back();
        updateCurrentRouteToDisplay();
    }

    public void skip() {
        zooNavigator.skip();
        updateCurrentRouteToDisplay();
    }

    public void reverse() {
        zooNavigator.reverse();
        updateCurrentRouteToDisplay();
    }

    private void updateCurrentRouteToDisplay() {
        String routeStr = isDirectionDetailed.getValue()
                ? zooNavigator.getRouteDetailed()
                : zooNavigator.getRouteBrief();

        String from = placeDao.get(zooNavigator.getCurrNode()).name;
        String to = placeDao.get(zooNavigator.getNextNode()).name;

        // limit max length of from and to to 8 characters
        if (from.length() > 10) from = from.substring(0, 8).trim() + "...";
        if (to.length() > 10) to = to.substring(0, 8).trim() + "...";

        fromAndTo.setValue(new Pair<>(from, to));
        currentRouteToDisplay.setValue(routeStr);
    }
}
