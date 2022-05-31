package edu.ucsd.cse110.zooseeker.Util.SaveLoad;


import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.Serializable;

import edu.ucsd.cse110.zooseeker.NewNavigator.ZooNavigator;

public class ZooNavigatorSaverLoader implements Serializable {
    ZooNavigator zooNavigator = null;
    public ZooNavigatorSaverLoader(SharedPreferences preferences){

        Gson gson = new Gson();
        String json = preferences.getString("ZOONAVIGATOR", "");
        if(!json.equals("")){
            ZooNavigator zooNavigator = gson.fromJson(json, ZooNavigator.class);
            this.zooNavigator = zooNavigator;
        }
    }

    public boolean hasZooNavigator(){
        if(zooNavigator == null){
            return false;
        }
        return true;
    }

    public ZooNavigator getZooNavigator(){
        return zooNavigator;
    }

    public void saveZooNavigator(SharedPreferences preferences, ZooNavigator zooNavigatorToSave){
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(zooNavigatorToSave);
        prefsEditor.putString("ZOONAVIGATOR", json);
        this.zooNavigator = zooNavigatorToSave;
        prefsEditor.commit();
    }

    public void deleteZooNavigator(SharedPreferences preferences){
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.remove("ZOONAVIGATOR");
    }
}
