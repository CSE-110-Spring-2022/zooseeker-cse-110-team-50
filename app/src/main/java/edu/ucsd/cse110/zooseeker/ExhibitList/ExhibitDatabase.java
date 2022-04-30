package edu.ucsd.cse110.zooseeker.ExhibitList;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {ExhibitListItem.class}, version = 1)
public abstract class ExhibitDatabase extends RoomDatabase {
    private static ExhibitDatabase singleton = null;

    public abstract ExhibitListItemDao exhibitListItemDao();

    public synchronized static ExhibitDatabase getSingleton(Context context){
        if (singleton == null) {
            singleton = ExhibitDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static ExhibitDatabase makeDatabase(Context context){
        return Room.databaseBuilder(context, ExhibitDatabase.class, "exhibit_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback(){
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db){
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() ->{
                            List<ExhibitListItem> exhibits = ExhibitListItem
                                    .loadJSON(context, "sample_node_info.json");
                            getSingleton(context).exhibitListItemDao().insertAll(exhibits);
                        });
                    }
                }).build();
    }

    @VisibleForTesting  // should not be called outside of tests
    public static void injectTestDatabase(ExhibitDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}

