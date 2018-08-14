package com.hossain.ju.bus.location;

import android.location.*;
import android.location.Location;

/**
 * Created by mohammod.hossain on 8/14/2018.
 */

public class LocationData {
   android.location.Location location;

    public LocationData(android.location.Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
