package com.hossain.ju.bus;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.hossain.ju.bus.db.DbAdapter;
import com.hossain.ju.bus.location.Location;
import com.hossain.ju.bus.location.LocationViewAdapter;

import java.util.List;

public class TrackingActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private LocationViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_tracking_log);
        mContext = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        DbAdapter adapter =  new DbAdapter(mContext);
        List<Location> list = adapter.getTracking();
        if(list != null && list.size() > 0 ){
            Log.e("TAG", list.size()+"");
            mAdapter = new LocationViewAdapter(mContext,list);
            mRecyclerView.setAdapter(mAdapter);

           mAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(mContext,"No data",Toast.LENGTH_SHORT).show();
        }

    }
}
