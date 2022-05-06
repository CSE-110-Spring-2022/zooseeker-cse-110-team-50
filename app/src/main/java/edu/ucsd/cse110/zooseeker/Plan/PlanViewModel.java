package edu.ucsd.cse110.zooseeker.Plan;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;

public class PlanViewModel extends AndroidViewModel {
    private LiveData<List<PlanItem>> planItems;
    private PlaceDao placeDao;
    //private LiveData<Integer> planCount;
    //private int planCount = 0;

    private PlanItemDao planItemDao;

    public PlanViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        MainDatabase db = MainDatabase.getSingleton(context);
        planItemDao = db.planItemDao();
        placeDao = db.placeDao();
    }

    public LiveData<List<PlanItem>> getPlanItems() {
        if (planItems == null) {
            this.loadUsers();
        }
        return planItems;
    }

    private void loadUsers() {
        this.planItems = this.planItemDao.getAllLive();
    }

    public void insertPlanItem(PlanItem planItem) {
        this.planItemDao.insert(planItem);
    }

    public void deletePlanItem(PlanItem planItem) {
        this.planItemDao.delete(planItem);
    }

    public void updatePlanItem(PlanItem planItem) {
        this.planItemDao.update(planItem);
    }

    public void updateDistance(PlanItem planItem, double distance) {
        planItem.setDistance(distance);
        this.planItemDao.update(planItem);
    }

    public int getPlanCount(){
        //planCount = planItems.size();
        return planItemDao.getAll().size();
    }

}