package edu.ucsd.cse110.zooseeker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class SearchListItem {
    public long id = 0;
    public String kind;
    public String name;
    public List<String> tags;

    public SearchListItem(String kind, String name, List<String> tags) {
        this.kind = kind;
        this.name = name;
        this.tags = tags;
    }

    public static List<SearchListItem> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<SearchListItem>>(){}.getType();
            return gson .fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "SearchListItem{" +
                "id=" + id +
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
