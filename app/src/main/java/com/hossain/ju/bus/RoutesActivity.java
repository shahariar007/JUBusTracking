package com.hossain.ju.bus;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.hossain.ju.bus.db.DbAdapter;
import com.hossain.ju.bus.db.LocationContract;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.location.LocationUtils;
import com.hossain.ju.bus.model.route.Route;
import com.hossain.ju.bus.model.route.Schedule;
import com.hossain.ju.bus.model.schedule.RouteSchedule;
import com.hossain.ju.bus.networking.APIClient;
import com.hossain.ju.bus.networking.APIServices;
import com.hossain.ju.bus.networking.ResponseWrapperArray;
import com.hossain.ju.bus.networking.ResponseWrapperObject;
import com.hossain.ju.bus.route.RouteViewAdapter;
import com.hossain.ju.bus.utils.APIError;
import com.hossain.ju.bus.utils.CustomProgressDialog;
import com.hossain.ju.bus.utils.ErrorUtils;
import com.hossain.ju.bus.utils.TempData;
import com.hossain.ju.bus.utils.Utils;
import com.hossain.ju.bus.views.UI;
import com.hossain.ju.bus.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutesActivity extends AppCompatActivity {

    private static String TAG = "RoutesActivity";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RouteViewAdapter mAdapter;
    List<Route> routeList;
    APIServices apiServices;
    Bundle bundle;
    List<Schedule> listOfSubRoute;
    List<Schedule> listOfSubRoutessss;
    ListView listViewOfBottomSheet;
    BottomSheetBehavior sheetBehavior;
    TextView txtSchTitle;
    private LocationRequest mLocationRequest;
    private static final int LOCATION = 120;

    Disposable ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        mContext = this;
        bundle = getIntent().getExtras();
        init();
        Utils.applyCustomFont(mContext,getWindow().getDecorView().getRootView());

        setupToolbar();
        apiServices = APIClient.getInstance().create(APIServices.class);

        if (Utils.isConnected(mContext)) {
            populatesRoutes();
        } else {
            Utils.toast(mContext, getString(R.string.error_internet_connection));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_route);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(linearLayout);
        txtSchTitle = linearLayout.findViewById(R.id.txtSchTitle);
        txtSchTitle.setText("");
        sheetBehavior.setPeekHeight(0);

        sheetBehavior.setHideable(true);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull final View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        listViewOfBottomSheet = (ListView) findViewById(R.id.listViewBottomSheet);


    }

    public void toggleBottomSheet(int position) {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setHideable(false);

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        sheetBehavior.setHideable(false);
        txtSchTitle.setText(routeList.get(position).getName());
        generateList(routeList.get(position).getSchedule());
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void populatesRoutes() {
        routeList = new ArrayList<>();
        final CustomProgressDialog progressDialog = UI.show(mContext);
        String token = Utils.BEARER + SharedPreferencesHelper.getToken(mContext);
        Log.e("Token::", token);
        Call<ResponseWrapperArray<Route>> response = apiServices.getAllRoutes(token);

        response.enqueue(new Callback<ResponseWrapperArray<Route>>() {
            @Override
            public void onResponse(Call<ResponseWrapperArray<Route>> call, Response<ResponseWrapperArray<Route>> response) {
                if (progressDialog != null) {
                    progressDialog.dismissAllowingStateLoss();
                }

                Log.e(TAG, response.toString());
                try {
                    if (response != null && response.isSuccessful()) {
                        Log.e(TAG, response.body().getData().toString());
                        routeList = (ArrayList<Route>) response.body().getData();
                        if (routeList != null && routeList.size() > 0) {
                            Log.e("SIZE:::", routeList.size() + "");
                            mAdapter = new RouteViewAdapter(mContext, routeList, getFavList(), bundle.getBoolean("fav"));
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            mAdapter.setOnClickListener(new RecyclerViewClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    switch (view.getId()) {
                                        case R.id.imgFav:
                                            if (view.getTag().equals(0)) {
                                                insertFavoriteList(routeList.get(position).getId());
                                                mAdapter.setFavList(getFavList());
                                                mAdapter.notifyDataSetChanged();
//                                                if (view instanceof ImageView) {
//                                                    ((ImageView) view).setImageResource(R.drawable.favorite_heart_button);
//                                                }

                                            } else {
                                                deleteFavID(routeList.get(position).getId());
                                                mAdapter.setFavList(getFavList());
                                                mAdapter.notifyDataSetChanged();
//                                                if (view instanceof ImageView) {
//                                                    ((ImageView) view).setImageResource(R.drawable.favorite);
//                                                }
                                            }

                                            break;
                                        case R.id.imgLocation:
                                          //  Toast.makeText(RoutesActivity.this, " Location=" + position, Toast.LENGTH_SHORT).show();
                                            break;
                                        case R.id.layoutRoot: {
                                            toggleBottomSheet(position);
//                                            Intent intent = new Intent(mContext, MapActivity.class);
//                                            intent.putExtra("id", routeList.get(position).getId());
//                                            Log.d("TISS", routeList.get(position).getId() + "");
//                                            mContext.startActivity(intent);
                                            break;
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(mContext, "No data found!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // parse the response body …
                        try {
                            APIError error = ErrorUtils.parseError(response);
                            if (error != null)
                                Utils.toast(mContext, error.message());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<ResponseWrapperArray<Route>> call, Throwable t) {
                // there is more than just a failing request (like: no internet connection)
                progressDialog.dismissAllowingStateLoss();
                Log.e(TAG, t.getMessage() + "");
                Utils.toast(mContext, "data dot found!!");
            }
        });

    }

    public int position;

    public void generateList(List<Schedule> listOfSubRoute) {
        listOfSubRoutessss = listOfSubRoute;
        if (listOfSubRoute != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.custom_text_layout, listOfSubRoute);
            listViewOfBottomSheet.setAdapter(arrayAdapter);
            listViewOfBottomSheet.setDividerHeight(10);
            arrayAdapter.notifyDataSetChanged();
            listViewOfBottomSheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    position = i;
                    askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
                }
            });

        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(RoutesActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RoutesActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                Log.e(TAG, "onRequestPermissionsResult: " +" denied:0" );
               // showSettingsAlert(RoutesActivity.this);
                ActivityCompat.requestPermissions(RoutesActivity.this, new String[]{permission}, requestCode);

            } else {
                Log.e(TAG, "onRequestPermissionsResult: " +" denied:1" );
                showLocationPermission(this);
                ActivityCompat.requestPermissions(RoutesActivity.this, new String[]{permission}, requestCode);

            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onRequestPermissionsResult: " +" granted:2" );
            callMapIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case LOCATION:
                     callMapIntent();
                    break;

            }
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
          // Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onRequestPermissionsResult: " +" denied:3" );

        }
    }

    public void callMapIntent() {

        if(!LocationUtils.isGPSOn(mContext)){
            Log.e(TAG, "GPS OFF: " );
           // LocationUtils.turnGPSOn(mContext);
           LocationUtils.showSettingsAlert(RoutesActivity.this);
        }else{
            Log.e(TAG, "GPS ON: " );
            if (listOfSubRoutessss == null || listOfSubRoutessss.size() == 0) {
               // Utils.toast(mContext, "NULLL");
            } else {

                Utils.toast(mContext, "POS: " + listOfSubRoutessss.get(position).getId());
                getBusLocation(listOfSubRoutessss.get(position).getId());

            }
        }
    }

    public List<Integer> getFavList() {
        DbAdapter adapter = new DbAdapter(this);
        if (adapter.getFavListData() != null) {
            Log.d("FFFFFFFF", adapter.getFavListData().toString());
            return adapter.getFavListData();
        }
        return null;
    }

    public void insertFavoriteList(int id) {
        DbAdapter adapter = new DbAdapter(this);
        ContentValues values = new ContentValues();
        values.put(LocationContract.TransLocation.COLUMN_NAME_FAV_ID, id);
        long rowId = adapter.insertFavoriteItem(values);
        if (rowId != 0) {
            Toast.makeText(mContext, "Favourite route Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteFavID(int id) {
        DbAdapter adapter = new DbAdapter(this);
        if (adapter.deleteID(id) != 0) {
            Toast.makeText(mContext, "Not Favourite Added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.routes_menu, menu);
        MenuItem item = menu.findItem(R.id.ic_favourites_menu);
        if(bundle.get("fav") != null && bundle.getBoolean("fav") == true ){
            item.setVisible(false);
        }else{
            item.setVisible(true);
        }

        invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                return true;

            case R.id.ic_favourites_menu:
                Intent intent = new Intent(this, RoutesActivity.class);
                intent.putExtra("fav", true);
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setHideable(true);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ds!= null)ds.dispose();
    }

    public void showLocationPermission(final Activity activity ){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);

    }
    public static   void turnGPSOn(Context mContext){
//        Log.d("turnGPSOn", "turnGPSOn: ");
//        String provider = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//
//        if(!provider.contains("gps")){ //if gps is disabled
//            final Intent poke = new Intent();
//            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//            poke.setData(Uri.parse("3"));
//            mContext.sendBroadcast(poke);
//        }
        Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        mContext.sendBroadcast(intent);
    }


    private void getBusLocation(int id) {

        final CustomProgressDialog progressDialog = UI.show(mContext);
        String token = Utils.BEARER + SharedPreferencesHelper.getToken(mContext);
        Log.e("SCHE_ID::", id + "");

        apiServices.getBusLocationBySchedule(token, id).observeOn(AndroidSchedulers.mainThread()).timeout(30, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(new Observer<ResponseWrapperObject<RouteSchedule>>() {
            @Override
            public void onSubscribe(Disposable d) {
                ds = d;
            }

            @Override
            public void onNext(ResponseWrapperObject<RouteSchedule> routeScheduleResponseWrapperObject) {
                long x = System.currentTimeMillis();
                Log.e("TXXXXX", "HIT");
                Log.e("TXXXXX", routeScheduleResponseWrapperObject.getData()+"");

                try {
                    if (routeScheduleResponseWrapperObject.getStatus().contains("ok")) {
                        RouteSchedule route = routeScheduleResponseWrapperObject.getData();

                        if (route != null && (route.getLatitude() != null || !route.getLatitude().isEmpty()) && (route.getLongitude() != null || !route.getLongitude().isEmpty())) {

                            Intent intent = new Intent(mContext, MapActivity.class);
                            Schedule schedule = listOfSubRoutessss.get(position);
                            intent.putExtra(Utils.SCHEDULE_ID, schedule.getId());
                            startActivity(intent);

                        }else if(route != null && route.equals("[]")) {

                            Utils.toast(mContext,"Today no trip is  available");
                        }

                    } else if(routeScheduleResponseWrapperObject != null && routeScheduleResponseWrapperObject.getStatus().equalsIgnoreCase("failed"))                    {
                        Log.d(TAG, "onNext: "+routeScheduleResponseWrapperObject.getStatus());

                    }else if(routeScheduleResponseWrapperObject != null && routeScheduleResponseWrapperObject.getData().equals("[]")){
                        Utils.toast(mContext,"Today no trip is  available");

                    }else{

                    }
                } catch (Exception e) {
                    e.getStackTrace();
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                 progressDialog.dismissAllowingStateLoss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isFinishing()){

                        }
                        //Utils.toast(mContext, "data Failed!");

                    }
                });

                Log.d(TAG, "onError: "+e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                progressDialog.dismissAllowingStateLoss();
                Log.d(TAG, "HITOUT");
                // Toast.makeText(mContext, "Complete", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
