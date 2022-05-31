package edu.ucsd.cse110.zooseeker.Route;

import android.app.Application;
import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class RouteViewModel extends AndroidViewModel {

    private static int SHOULD_REROUTE_THROTTLE = 0;

    // Live Data
    private MutableLiveData<Boolean> isDirectionDetailed;
    private MutableLiveData<String> currentRouteToDisplay;
    private MutableLiveData<Pair<String, String>> fromAndTo;
    private MutableLiveData<Pair<Double, Double>> currentLocationCoordinate;
    private MutableLiveData<Boolean> isLocationMocked;
    private MutableLiveData<String> uiMessage;
    private MutableLiveData<Boolean> enableReroute;

    // DAOs
    private MainDatabase mainDb;
    private PlaceDao placeDao;
    private PlanItemDao planItemDao;

    // Navigator
    public ZooNavigator zooNavigator;
    private String nodeIdToRerouteFrom;

    // Ctor
    public RouteViewModel(@NonNull Application application) {
        super(application);

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
        isLocationMocked = new MutableLiveData<>(false);
        uiMessage = new MutableLiveData<>("");
        enableReroute = new MutableLiveData<>(false);
        resetUiMessage();
        setCurrentLocationCoordinate(17.123124123, 17.8787);
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

    public LiveData<String> getUIMessage() {
        return uiMessage;
    }

    public void setIsLocationMocked(boolean isLocationMocked) {
        this.isLocationMocked.setValue(isLocationMocked);
    }

    public void setRealCurrentLocationCoordinate(double lat, double log) {
        if (!isLocationMocked.getValue()) {
            setCurrentLocationCoordinate(lat, log);
            shouldRerouteThrottled(lat, log);
        }
    }

    public void setMockCurrentLocationCoordinate(double lat, double log) {
        if (isLocationMocked.getValue()) {
            setCurrentLocationCoordinate(lat, log);
            shouldReroute(lat, log);
        }
    }

    public void shouldRerouteThrottled(double lat, double log) {
        int THROTTLE_THRESHOLD = 100;
        if (SHOULD_REROUTE_THROTTLE % THROTTLE_THRESHOLD == 0) {
            shouldReroute(lat, log);
            SHOULD_REROUTE_THROTTLE = 0;
        }
        else {
            SHOULD_REROUTE_THROTTLE++;
        }
    }

    public void shouldReroute(double lat, double log) {
        String nodeToRerouteFrom = zooNavigator.shouldReroute(lat, log);

        if (nodeToRerouteFrom != null) {
            setUiMessage("You are off route! Do you want to reroute from " + nodeToRerouteFrom + "?");
            setEnableReroute(true);
            nodeIdToRerouteFrom = nodeToRerouteFrom;
        }
    }

    public void setEnableReroute(boolean enable) {
        enableReroute.setValue(enable);
    }

    public LiveData<Boolean> getEnableReroute() {
        return enableReroute;
    }

    public void setUiMessage(String mes) {
        uiMessage.setValue(mes);
    }

    public void resetUiMessage() {
        uiMessage.setValue("Have Fun!");
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

    public void reroute() {
        resetUiMessage();
        zooNavigator.reroute(nodeIdToRerouteFrom);
        setEnableReroute(false);
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
