package edu.ucsd.cse110.zooseeker.Persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Exhibit.class}, version = 1)
public abstract class MainDatabase extends RoomDatabase {

    private static MainDatabase singleton = null;
    public abstract ExhibitDao exhibitDao();

    public synchronized static MainDatabase getSingleton(Context context) {
        if (singleton == null) singleton = makeDatabase(context);
        return singleton;
    }

    public static MainDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, MainDatabase.class, "main.db")
                .allowMainThreadQueries()
                .build();
    }
}