package edu.ucsd.cse110.zooseeker.Util;

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

import edu.ucsd.cse110.zooseeker.Persistence.Place;

public class JSONLoader {

    public static List<Place> loadExamplePlaceData(Context context) {
        final String PATH = "place_data.json";

        try {
            InputStream input = context.getAssets().open(PATH);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Place>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
