package edu.ucsd.cse110.zooseeker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "exhibit_list_items")
public class ExhibitListItem {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;

    @NonNull
    public String kind;
    public String name;
    public List<String> tags;

    //Constructor
    public ExhibitListItem(String kind, String name, List<String> tags) {
        this.kind = kind;
        this.name = name;
        this.tags = tags;
    }

    public static List<ExhibitListItem> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<ExhibitListItem>>(){}.getType();
            return gson .fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "ExhibitListItem{" +
                "id=" + id +
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
