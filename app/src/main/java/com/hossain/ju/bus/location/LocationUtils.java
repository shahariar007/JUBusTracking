package com.hossain.ju.bus.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by mohammod.hossain on 2/28/2017.
 */

public class LocationUtils {
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    // Location updates intervals in sec
    public static int UPDATE_INTERVAL = 1000; // 10 sec
    public static int FASTEST_INTERVAL = 5000; // 5 sec
    public static int DISPLACEMENT = 0; // 1 meters
    public static final int REQUEST_LOCATION = 2;

    /**
     * Creating location request object
     * */
    public static void createLocationRequest(LocationRequest mLocationRequest) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    public static boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int status = api.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
          //  Log.e(TAG, " ***** Update google play service ");
            return false;
        }
    }


    public static  boolean isBetterLocation(Location location, Location currentBestLocation, long TIME) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME;
        boolean isSignificantlyOlder = timeDelta < -TIME;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    public  static String getAddress(Context context, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses == null || addresses.size()  == 0) {
            }else {
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    result.append(address.getAddressLine(0) == null ? "" : address.getAddressLine(0)).append(",");
                    result.append(address.getSubLocality() == null ? "" : address.getSubLocality()).append(",");
                    //result.append(address.getLocality() == null ? "" : address.getLocality()).append(",");
                    if(address.getPostalCode() != null){
                        result.append(address.getPostalCode()).append(",");
                    }
                   // result.append(address.getCountryName());
                }
            }
        } catch (IOException e) {
            //	Log.e(TAG, e.getMessage());
        }
        return result.toString();
    }
}
