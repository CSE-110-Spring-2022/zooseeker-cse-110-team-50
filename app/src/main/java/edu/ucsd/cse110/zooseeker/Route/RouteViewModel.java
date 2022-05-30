package edu.ucsd.cse110.zooseeker.Route;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import edu.ucsd.cse110.zooseeker.Location.LocationModel;
import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.Util.Router.Router;

public class RouteViewModel extends AndroidViewModel {

    // Live Data
    private MutableLiveData<Boolean> isDirectionDetailed;
    private MutableLiveData<String> currentRouteToDisplay;
    private LocationModel locationModel;


    // DAOs
    private MainDatabase mainDb;
    private PlaceDao placeDao;
    private PlanItemDao planItemDao;

    // Navigator
    private ZooNavigator zooNavigator;
    private List<Router.RoutePackage> routePackageList;

    // Navigator Index
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
        zooNavigator = new ZooNavigator(placeDao, planItemDao, context);
        routePackageList = zooNavigator.getPkgList();

        // initialize LiveData
        isDirectionDetailed = new MutableLiveData<>(false);

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

    public void toggleIsDirectionDetailed() {
        isDirectionDetailed.setValue(!isDirectionDetailed.getValue());
        updateCurrentRouteToDisplay();
    }

    public void next() {
        if (routePackageList.size() > currentRouteIndex + 1) currentRouteIndex++;
        updateCurrentRouteToDisplay();
    }

    public void back() {
        if (currentRouteIndex - 1 >= 0) currentRouteIndex--;
        updateCurrentRouteToDisplay();
    }

    private void updateCurrentRouteToDisplay() {
        String routeStr = isDirectionDetailed.getValue()
                ? routePackageList.get(currentRouteIndex).toStringDetailed()
                : routePackageList.get(currentRouteIndex).toStringBrief();
        currentRouteToDisplay.setValue(routeStr);
    }
}
