package com.hossain.ju.bus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.location.GetDataFromUrl;
import com.hossain.ju.bus.location.GetDirections;
import com.hossain.ju.bus.location.LocationListener;
import com.hossain.ju.bus.location.LocationUpdateIntentService;
import com.hossain.ju.bus.location.LocationUtils;
import com.hossain.ju.bus.model.DistanceDisplayModel;
import com.hossain.ju.bus.model.schedule.RouteSchedule;
import com.hossain.ju.bus.networking.APIClient;
import com.hossain.ju.bus.networking.APIServices;
import com.hossain.ju.bus.networking.ResponseWrapperObject;
import com.hossain.ju.bus.utils.Constants;
import com.hossain.ju.bus.utils.CustomProgressDialog;
import com.hossain.ju.bus.utils.TempData;
import com.hossain.ju.bus.utils.Utils;
import com.hossain.ju.bus.views.UI;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = "MainActivity";

    public static final int REQ_PERMISSIONS_REQUEST = 5;

    private AddressResultReceiver mResultReceiver;

    private Context mContext;
    private Location mLastLocation;
    private double latitude, longitude;

    SupportMapFragment mapFragment;
    ListView listView;
    /**
     * The formatted location address.
     */
    protected String mAddressOutput;

    private APIServices apiServices;

    private TextView txtBusLocation, txtDistance;
    private GoogleMap gMap;
    Disposable ds;

    private ProgressBar mProgressBar;
    int i = 0;

    List<LatLng> latLngList;

    ArrayList<LatLng> points = null;
    PolylineOptions lineOptions = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        setUpToolbar();
        apiServices = APIClient.getInstance().create(APIServices.class);
        txtBusLocation = (TextView) findViewById(R.id.txtBusLocation);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(MainActivity.this);
        startIntentService();

       if(Utils.isConnected(mContext)){
           getBusLocation(getIntent().getExtras().getInt(Utils.SCHEDULE_ID));
       }else{
           Utils.toast(mContext,getString(R.string.error_internet_connection));
       }


        //setDistanceDisplay();
    }

    public void setDistanceDisplay() {
        List<DistanceDisplayModel> distanceDisplayModels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            distanceDisplayModels.add(new DistanceDisplayModel("Name No:" + i, i));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.root_recycler_view_text, distanceDisplayModels);
        listView.setAdapter(arrayAdapter);
        listView.setDividerHeight(20);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        mResultReceiver = new AddressResultReceiver(new Handler());

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ds != null)
            ds.dispose();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ds != null)
            ds.dispose();
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, LocationUpdateIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        // intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    private Marker mMarkerA;
    private Marker mMarkerB;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_action_bus);
        LatLng latLng, latLng2;
        if (TempData.CURRENT_TRANSPORT_LOC != null) {
            latLng = new LatLng(TempData.LAST_LATITUDE, TempData.LAST_LONGITUDE);
        } else {
            latLng = new LatLng(23.777176, 90.399452);
        }

        if (TempData.CURRENT_USER_LOC != null) {
            latLng2 = new LatLng(TempData.USER_LAT, TempData.USER_LONG);
        } else {
            latLng2 = new LatLng(24.777176, 90.399452);
        }

        mMarkerA = gMap.addMarker(new MarkerOptions().position(latLng).title(TempData.CURRENT_TRANSPORT_LOC).icon(icon));

        mMarkerB = gMap.addMarker(new MarkerOptions().position(latLng2).title(TempData.CURRENT_USER_LOC).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


        //gMap.addMarker(new MarkerOptions().position(latLng).title(TempData.CURRENT_TRANSPORT_LOC).icon(icon));
        float zoomLevel = (float) 12.0f;
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setAllGesturesEnabled(true);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFail() {
        Log.i(TAG, "Failed to get directions from Google...");

    }


    @Override
    public void onSuccessfullRouteFetch(final List<List<HashMap<String, String>>> result) {

        //if it takes a long time, we will do it in a seperate thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                MarkerOptions markerOptions = new MarkerOptions();

                for (List<HashMap<String, String>> path : result) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    // Get all the points for this route
                    for (HashMap<String, String> point : path) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.RED);
                }

                //Do all UI operations on the UI thread only...
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Drawing polyline in the Google Map for the this route
                        gMap.addPolyline(lineOptions);
                    }
                });

            }
        }).start();

    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.

            if (resultData == null) {
                return;
            }

            // mAddressOutput      = resultData.getString(Constants.RESULT_DATA_KEY);

            Location location = resultData.getParcelable(Constants.CURRENT_LOCATION);

            TempData.USER_LAT = location.getLatitude();
            TempData.USER_LONG = location.getLongitude();
            updateWidget(mAddressOutput, TempData.USER_LAT, TempData.USER_LONG);
            Log.e(TAG, "onReceiveResult: " + TempData.USER_LAT + " " + TempData.USER_LONG);
            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                //  Utils.toast(mContext, getString(R.string.address_found));

                //txtDistance.setText("" + Utils.round(Utils.calculationByDistance(new LatLng(TempData.USER_LAT, TempData.USER_LONG), new LatLng(TempData.LAST_LATITUDE, TempData.LAST_LONGITUDE))));
            }
        }
    }


    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    private void updateWidget(String address, double lat, double longitude) {
        // txtBusLocation.setText(address);
        // Utils.toast(mContext, "LAT:" + lat + " LONG: " + longitude);
    }


    private void getBusLocation(int id) {

        final CustomProgressDialog progressDialog = UI.show(MainActivity.this);
        String token = Utils.BEARER + SharedPreferencesHelper.getToken(mContext);
        Log.e("SCHE_ID::", id + "");

        apiServices.getBusLocationBySchedule(token, id).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                Log.d(TAG, "apply: " + i);
                return objectObservable.delay(Utils.REQUEST_DELAY, TimeUnit.SECONDS);
            }
        }).repeat().observeOn(AndroidSchedulers.mainThread()).timeout(50, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).subscribe(new Observer<ResponseWrapperObject<RouteSchedule>>() {
            @Override
            public void onSubscribe(Disposable d) {
                ds = d;
            }

            @Override
            public void onNext(ResponseWrapperObject<RouteSchedule> routeScheduleResponseWrapperObject) {

                Log.e("TXXXXX", "HIT");
                try {

                    if (routeScheduleResponseWrapperObject.getStatus().contains("ok")) {
                        RouteSchedule route = routeScheduleResponseWrapperObject.getData();
                        DecimalFormat df = new DecimalFormat("##.000000");
                        Log.e("DATA:", "" + route.getLongitude() + ":" + df.format(new BigDecimal(route.getLongitude()).doubleValue()));
                        String address = LocationUtils.getAddress(mContext, Double.valueOf(route.getLatitude()), Double.valueOf(route.getLongitude()));

                        TempData.CURRENT_TRANSPORT_LOC = address;
                        TempData.LAST_LATITUDE = Double.valueOf(route.getLatitude());
                        TempData.LAST_LONGITUDE = Double.valueOf(route.getLongitude());
                        txtBusLocation.setText(address);

                        //double distance = Utils.calculationByDistance(new LatLng(TempData.USER_LAT, TempData.USER_LONG), new LatLng(TempData.LAST_LATITUDE, TempData.LAST_LONGITUDE));
                        // Log.e(TAG, "DIStance::" + distance);
                       // txtDistance.setText("" + Utils.round(distance) + " Km");

                        // Getting URL to the Google Directions API

                        LatLng dest = new LatLng(TempData.USER_LAT, TempData.USER_LONG);
                        LatLng origin = new LatLng(TempData.LAST_LATITUDE, TempData.LAST_LONGITUDE);

                        try {

                            Log.e(TAG, "onNext: " + TempData.USER_LAT + " " + TempData.USER_LONG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // refresh marker
                        mMarkerA.setPosition(origin);
                        mMarkerB.setPosition(dest);

//                        String url = GetDataFromUrl.getDirectionsUrl(origin, dest);
//                        GetDirections getDirections = new GetDirections(MainActivity.this);
//                        getDirections.startGettingDirections(url);
                        progressDialog.dismissAllowingStateLoss();
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                    // Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismissAllowingStateLoss();
                Utils.toast(mContext, "data Failed!");
            }

            @Override
            public void onComplete() {
                Log.d("TXXXXX", "HIT");
                Toast.makeText(mContext, "Complete", Toast.LENGTH_SHORT).show();
            }
        });
//        Call<ResponseWrapperObject<RouteSchedule>> response = apiServices.getBusLocationBySchedule(token, id);
//
//        response.enqueue(new Callback<ResponseWrapperObject<RouteSchedule>>() {
//            @Override
//            public void onResponse(Call<ResponseWrapperObject<RouteSchedule>> call, Response<ResponseWrapperObject<RouteSchedule>> response) {
//                progressDialog.dismissAllowingStateLoss();
//
//                Log.e(TAG, response.body().getData().toString());
//                try {
//                    if (response != null && response.isSuccessful()) {
//                        Log.e(TAG, response.body().getData().toString());
//                        Log.e(TAG, response.body().getData() + "");
//
//                        RouteSchedule route = (RouteSchedule) response.body().getData();
//                        Log.e(TAG, route.getDeviceId() + "::" + route.getLatitude() + "::" + route.getLongitude());
//                        String address = LocationUtils.getAddress(mContext, Double.valueOf(route.getLatitude()), Double.valueOf(route.getLongitude()));
//
//                        TempData.CURRENT_TRANSPORT_LOC = address;
//                        TempData.LAST_LATITUDE = Double.valueOf(route.getLatitude());
//                        TempData.LAST_LONGITUDE = Double.valueOf(route.getLongitude());
//                        txtBusLocation.setText(address);
//
//                        double distance = Utils.calculationByDistance(new LatLng(TempData.USER_LAT, TempData.USER_LONG), new LatLng(TempData.LAST_LATITUDE, TempData.LAST_LONGITUDE));
//                        Log.e(TAG, "DIStance::" + distance);
//                        txtDistance.setText("" + Utils.round(distance));
//
//
//                        mapFragment.getMapAsync((OnMapReadyCallback) mContext);
//
//                    } else {
//                        // parse the response body …
//                        try {
//                            APIError error = ErrorUtils.parseError(response);
//                            if (error != null)
//                                Utils.toast(mContext, error.message());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e(TAG, e.getMessage());
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseWrapperObject<RouteSchedule>> call, Throwable t) {
//                // there is more than just a failing request (like: no internet connection)
//                progressDialog.dismissAllowingStateLoss();
//
//                Log.e(TAG, t.getMessage() + "");
//                Utils.toast(mContext, "data Failed!");
//            }
//        });

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    final int MAP_BOUND_PADDING = 180;  /* In dp */

    // Draw polyline on map
    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(5);
        polyOptions.addAll(list);

        gMap.clear();
        gMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }

        final LatLngBounds bounds = builder.build();

        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, MAP_BOUND_PADDING);
        gMap.animateCamera(cu);
    }

}
