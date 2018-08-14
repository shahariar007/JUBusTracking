package com.hossain.ju.bus.location;

/**
 * Created by mohammod.hossain on 3/16/2017.
 */

import android.Manifest;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.hossain.ju.bus.db.DbAdapter;
import com.hossain.ju.bus.db.LocationContract;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.utils.Constants;
import com.hossain.ju.bus.utils.DateUtils;
import com.hossain.ju.bus.utils.Utils;


import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class LocationUpdateIntentService extends Service implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private final static String TAG = "LocationUpdateService";

    protected ResultReceiver mReceiver;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 1 * 10 ;// 10 sec

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    private static int DISPLACEMENT = 0;
    /**
     * Provides the entry point to Google Play services.
     */
    private LocationManager mLocationMgrGPS;

    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;
    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.hasExtra(Constants.RECEIVER))
            mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            if (isGooglePlayServicesAvailable()) {
                initLocationManager();
                buildGoogleApiClient();
                mGoogleApiClient.connect();
                Log.e(TAG,"mGoogleApiClient CALLED!!!!!!!!!!!!!");
            }
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Log.e(TAG,"mGoogleApiClient CALLED and connected!!!!!!!!!!!!!");
                startLocationUpdates();
            }
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent intent) {
        Log.e(TAG,"stopService called");
        stopLocationUpdates();
        return super.stopService(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "Service is Destroying...");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
           // mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //updateUI();

        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        if(LocationUtils.isBetterLocation(location, mCurrentLocation,UPDATE_INTERVAL_IN_MILLISECONDS)){
            updateLocation(location);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int status = api.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            Log.e(TAG, " ***** Update google play service ");
            return false;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
           // show a dialog
        } else {
            // permission has been granted, continue as usual
            Location lastKnownLocationGPS = null;
            if(mLocationMgrGPS != null){
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
                lastKnownLocationGPS = mLocationMgrGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if(lastKnownLocationGPS != null && LocationUtils.isBetterLocation(lastKnownLocationGPS, mCurrentLocation,UPDATE_INTERVAL_IN_MILLISECONDS)){
                updateLocation(lastKnownLocationGPS);
            }
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void insertBusLocations(Location location, int batteryLevel){
        if(location == null ) return;
        DbAdapter adapter = new DbAdapter(this);
        ContentValues values = new ContentValues();
        String address = LocationUtils.getAddress(this,location.getLatitude(),location.getLongitude());
        values.put(LocationContract.TransLocation.COLUMN_NAME_USER_ID, SharedPreferencesHelper.getUser(getApplicationContext()));
        values.put(LocationContract.TransLocation.COLUMN_NAME_TRANS_LAT,location.getLatitude());
        values.put(LocationContract.TransLocation.COLUMN_NAME_TRANS_LONG,location.getLongitude());
        values.put(LocationContract.TransLocation.COLUMN_NAME_TRANS_ADDRESS,address);
        values.put(LocationContract.TransLocation.COLUMN_NAME_TRANS_SAVE_DATE_TIME, DateUtils.getTodaysDateTime());
        values.put(LocationContract.TransLocation.COLUMN_NAME_TRANS_DEVICE_BATTERY_LEVEL,batteryLevel );
        values.put(LocationContract.TransLocation.COLUMN_NAME_STATUS,"0");

        long rowId = adapter.insertTransLocation(values);
        Log.e("TAG", rowId+"");

    }


    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
//        tv.append("\n\nExternal Media: readable="
//                +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
    }

    private void writeToSDFile(Location location){
        File root = Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/bus_tracking");
        if(!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir, "location.txt");
        if(file.exists()){
            try {
                FileOutputStream f = new FileOutputStream(file,true);
                OutputStreamWriter pw = new OutputStreamWriter(f);
                pw.append("00001410" + "," + location.getLatitude() + "," + location.getLongitude());
                pw.append("\n");
                pw.flush();
                pw.close();
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i(TAG, "******* File not found. Did you" +
                        " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //tv.append("\n\nFile written to "+file);

        Log.e("LOG","write complete..");
    }

    private void initLocationManager() {
        mLocationMgrGPS = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationMgrGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationMgrGPS = null;
        }
    }

    private void updateLocation(Location location){
        mCurrentLocation = location;


        if(mCurrentLocation != null){

            deliverResultToReceiver(Constants.SUCCESS_RESULT, null,mCurrentLocation);
        }else{
            // retry location if not found
        }

    }


    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverResultToReceiver(int resultCode, String message,Location mCurrentLocation) {
        Bundle bundle = new Bundle();
       // bundle.putString(Constants.RESULT_DATA_KEY, message);
        if(mCurrentLocation != null){
            Log.e(TAG, "LAT:"+mCurrentLocation.getLatitude()+":: "+"Longitude:"+mCurrentLocation.getLongitude());
            bundle.putParcelable(Constants.CURRENT_LOCATION, mCurrentLocation);
            EventBus.getDefault().post(new LocationData(mCurrentLocation));
            if(mReceiver != null){
                Log.e(TAG, "LAT2:"+mCurrentLocation.getLatitude()+":: "+"Longitude:"+mCurrentLocation.getLongitude());
               // Utils.toast(this,"Current mCurrentLocation: "+ mCurrentLocation.getLatitude());
                mReceiver.send(resultCode, bundle);
            }else{
                Log.d("ResultToReceiver:", "deliverResultToReceiver: "+" null");

            }
        }
    }


}