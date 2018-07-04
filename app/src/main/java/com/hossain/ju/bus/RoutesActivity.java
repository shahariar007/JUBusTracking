package com.hossain.ju.bus;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hossain.ju.bus.db.DbAdapter;
import com.hossain.ju.bus.db.LocationContract;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.model.route.Route;
import com.hossain.ju.bus.model.route.Schedule;
import com.hossain.ju.bus.networking.APIClient;
import com.hossain.ju.bus.networking.APIServices;
import com.hossain.ju.bus.networking.ResponseWrapperArray;
import com.hossain.ju.bus.route.RouteViewAdapter;
import com.hossain.ju.bus.utils.APIError;
import com.hossain.ju.bus.utils.CustomProgressDialog;
import com.hossain.ju.bus.utils.ErrorUtils;
import com.hossain.ju.bus.utils.Utils;
import com.hossain.ju.bus.views.UI;
import com.hossain.ju.bus.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

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
    ListView listViewOfBottomSheet;
    BottomSheetBehavior sheetBehavior;
    TextView txtSchTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        mContext = this;
        bundle = getIntent().getExtras();
        init();

        setupToolbar();
        apiServices = APIClient.getInstance().create(APIServices.class);

        populatesRoutes();
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
        txtSchTitle.setText("hffygedf");
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
                progressDialog.dismissAllowingStateLoss();
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
                                            Toast.makeText(RoutesActivity.this, " Location=" + position, Toast.LENGTH_SHORT).show();
                                            break;
                                        case R.id.layoutRoot: {
                                            toggleBottomSheet(position);
//                                            Intent intent = new Intent(mContext, MainActivity.class);
//                                            intent.putExtra("id", routeList.get(position).getId());
//                                            Log.d("TISS", routeList.get(position).getId() + "");
//                                            mContext.startActivity(intent);
                                            break;
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(mContext, "No data", Toast.LENGTH_SHORT).show();
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
                Utils.toast(mContext, "data Failed!");
            }
        });

    }

    public void generateList( final List<Schedule> listOfSubRoute) {
        if (listOfSubRoute != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.custom_text_layout, listOfSubRoute);
            listViewOfBottomSheet.setAdapter(arrayAdapter);
            listViewOfBottomSheet.setDividerHeight(10);
            arrayAdapter.notifyDataSetChanged();
            listViewOfBottomSheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Utils.toast(mContext,"POS: "+ listOfSubRoute.get(i).getId() );
                    Intent intent = new Intent(mContext,MainActivity.class);
                     Schedule schedule = listOfSubRoute.get(i);
                    intent.putExtra(Utils.SCHEDULE_ID,schedule.getId() );
                    startActivity(intent);
                }
            });

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
            Toast.makeText(mContext, "Fev Update Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteFavID(int id) {
        DbAdapter adapter = new DbAdapter(this);
        if (adapter.deleteID(id) != 0) {
            Toast.makeText(mContext, "Not Favourite", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
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
}
