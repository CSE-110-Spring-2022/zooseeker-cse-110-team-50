package edu.ucsd.cse110.zooseeker.Location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

/**
 * Code courtesy of example code provided by CSE 110 Staff!
 * https://github.com/CSE-110-Spring-2022/Location-Mocking-Example/blob/main/app/src/main/java/edu/ucsd/cse110/locationmockingexample/location/Coords.java
 */

public class LocationPermissionChecker {
    private ComponentActivity activity;

    final ActivityResultLauncher<String[]> requestPermissionLauncher;

    public LocationPermissionChecker(ComponentActivity activity) {
        this.activity = activity;
        requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
            perms.forEach((perm, isGranted) -> {
                Log.i("LAB7", String.format("Permission %s granted: %s", perm, isGranted));
            });
        });
    }

    // Sets up permissions for location
    // return true if permission granted false if not
    public boolean ensurePermissions() {
        var requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        var hasNoLocationPerms = Arrays.stream(requiredPermissions)
                .map(perm -> ContextCompat.checkSelfPermission(activity, perm))
                .allMatch(status -> status == PackageManager.PERMISSION_DENIED);

        if (!hasNoLocationPerms)
            return true;

        // Does not already have permission so request it
        requestPermissionLauncher.launch(requiredPermissions);

        return false; // Doesn't have permission yet
    }
}