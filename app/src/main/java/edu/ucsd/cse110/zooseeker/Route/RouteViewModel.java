package edu.ucsd.cse110.zooseeker.Route;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;
import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigatorJsonMapper;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class RouteViewModel extends AndroidViewModel {

    private static int SHOULD_REROUTE_THROTTLE = 0;
    private static String ZOO_NAVIGATOR_KEY = "SERIALIZED_DATA";
    private static String ZOO_NAVIGATOR_HASH_KEY = "DATA_HASH";

    // Live Data
    private final MutableLiveData<Boolean> isDirectionDetailed;
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

    // shared preference
    SharedPreferences zooNavigatorPref;

    // Ctor
    public RouteViewModel(@NonNull Application application) {
        super(application);

        // initialize DataBase related fields
        Context context = getApplication().getApplicationContext();
        mainDb = MainDatabase.getSingleton(context);
        placeDao = mainDb.placeDao();
        planItemDao = mainDb.planItemDao();

        // Initialize SharePreference file
        zooNavigatorPref =
                application.getSharedPreferences("ZooNavigator", Context.MODE_PRIVATE);

        // initialize new router
        Router router = new Router(getApplication());

        // get shared preference
        ZooNavigatorJsonMapper serializedZooNavigatorJsonMapper =
                retrieveSerializedZooNavigatorMapper();

        ZooNavigator newNavigator = initializeNewZooNavigator(router);

        if (serializedZooNavigatorJsonMapper == null
                || newNavigator.getInitialPlanListHashCode() !=
                serializedZooNavigatorJsonMapper.initialPlanListHashCode) {
            zooNavigator = newNavigator;
        }
        else {
            zooNavigator = serializedZooNavigatorJsonMapper.toZooNavigator(router);
        }

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

    private ZooNavigatorJsonMapper retrieveSerializedZooNavigatorMapper() {
        String serializedData = zooNavigatorPref.getString(ZOO_NAVIGATOR_KEY, null);
        if (serializedData == null) {
            return null;
        }
        return ZooNavigatorJsonMapper.fromJson(serializedData);
    }

    private ZooNavigator initializeNewZooNavigator(Router router) {
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
        return new ZooNavigator(zooNavigatorIds, router);
    }


    private void updateSerializedZooNavigatorMapper() {
        String json = ZooNavigatorJsonMapper.fromZooNavigator(zooNavigator).toSerializedJson();
        SharedPreferences.Editor editor = zooNavigatorPref.edit();
        editor.putString(ZOO_NAVIGATOR_KEY, json);
        editor.commit();
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
        updateSerializedZooNavigatorMapper();
        return fromAndTo;
    }

    public LiveData<Pair<Double, Double>> getCurrentLocationCoordinate() {
        updateSerializedZooNavigatorMapper();
        return currentLocationCoordinate;
    }

    public LiveData<Boolean> getIsLocationMocked() {
        return isLocationMocked;
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
            setUiMessage("You are off route! Do you want to reroute from " +
                            placeDao.get(nodeToRerouteFrom).name + "?");
            setEnableReroute(true);
            nodeIdToRerouteFrom = nodeToRerouteFrom;
        } else {
            resetUiMessage();
            setEnableReroute(false);
        }
    }

    public void shouldReroute() {
        shouldReroute(
                currentLocationCoordinate.getValue().first,
                currentLocationCoordinate.getValue().second
        );
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
        updateSerializedZooNavigatorMapper();
        updateCurrentRouteToDisplay();
        shouldReroute();
    }

    public void back() {
        zooNavigator.back();
        updateSerializedZooNavigatorMapper();
        updateCurrentRouteToDisplay();
        shouldReroute();
    }

    public void skip() {
        zooNavigator.skip();
        updateSerializedZooNavigatorMapper();
        updateCurrentRouteToDisplay();
        shouldReroute();
    }

    public void reverse() {
        zooNavigator.reverse();
        updateSerializedZooNavigatorMapper();
        updateCurrentRouteToDisplay();
    }

    public void reroute() {
        resetUiMessage();
        zooNavigator.reroute(nodeIdToRerouteFrom);
        setEnableReroute(false);
        updateSerializedZooNavigatorMapper();
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
