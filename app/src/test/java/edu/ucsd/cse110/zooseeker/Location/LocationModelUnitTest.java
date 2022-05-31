package edu.ucsd.cse110.zooseeker.Location;

import android.app.Application;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationModelUnitTest {
    private LocationModel locationModel;
    Application application = new Application();

    @Test
    /**
     * Make sure that getLastKnownCoords() works with mocked coords
     */
    public void testGetLastKnownCoords(){
        locationModel = new LocationModel(application);
        double lat = 12.08;
        double lng = 19.05;
        Coord coord1 = new Coord(lat, lng);
        locationModel.mockLocation(coord1);
        //curr1 = locationModel.getLastKnownCoords().observe(this, );
        Coord curr1 = locationModel.getLastKnownCoords().getValue();
        Assert.assertTrue(coord1.lat == lat);
        Assert.assertTrue(coord1.lng == lng);
    }

}
