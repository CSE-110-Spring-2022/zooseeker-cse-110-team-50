package edu.ucsd.cse110.zooseeker.Persistence;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

import edu.ucsd.cse110.zooseeker.Util.JSONLoader;

@Database(entities = {Place.class, PlaceTagCrossRef.class, Tag.class, PlanItem.class}, version = 3, exportSchema = true)
public abstract class MainDatabase extends RoomDatabase {

    private static MainDatabase singleton = null;

    // DAOs
    public abstract PlaceDao placeDao();
    public abstract TagDao tagDao();
    public abstract PlaceTagCrossRefDao placeTagCrossRefDao();
    public abstract PlanItemDao planItemDao();


    public synchronized static MainDatabase getSingleton(Context context) {
        if (singleton == null) singleton = makeDatabase(context);
        return singleton;
    }

    public static MainDatabase makeDatabase(Context context) {
        List<Place> places = JSONLoader.loadExamplePlaceData(context);
        int index = 0;
        for(Place i : places){
            Log.i(i.toString(), "Index " + index++);
        }
        return Room.databaseBuilder(context, MainDatabase.class, "main_db.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<Place> places = JSONLoader.loadExamplePlaceData(context);

                            getSingleton(context).placeDao().insertAll(places);

                        });
                    }
                })
                .build();
    }

    private static void resetDatabase(Context context) {
        List<Place> places = JSONLoader.loadExamplePlaceData(context);
    }

    @VisibleForTesting
    public static void injectDatabase(MainDatabase db) {
        if (singleton != null) singleton.close();
        singleton = db;
    }

}
