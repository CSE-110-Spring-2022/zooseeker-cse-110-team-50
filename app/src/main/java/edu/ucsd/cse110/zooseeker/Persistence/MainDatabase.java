package edu.ucsd.cse110.zooseeker.Persistence;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Exhibit.class, ExhibitTagCrossRef.class, Tag.class}, version = 1)
public abstract class MainDatabase extends RoomDatabase {

    private static MainDatabase singleton = null;

    // DAOs
    public abstract ExhibitDao exhibitDao();
    public abstract TagDao tagDao();


    public synchronized static MainDatabase getSingleton(Context context) {
        if (singleton == null) singleton = makeDatabase(context);
        return singleton;
    }

    public static MainDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, MainDatabase.class, "main.db")
                .allowMainThreadQueries()
                .build();
    }

    @VisibleForTesting
    public static void injectDatabase(MainDatabase db) {
        if (singleton != null) singleton.close();
        singleton = db;
    }

}
