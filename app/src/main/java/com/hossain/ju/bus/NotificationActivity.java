package com.hossain.ju.bus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.model.Notice;
import com.hossain.ju.bus.networking.APIClient;
import com.hossain.ju.bus.networking.APIServices;
import com.hossain.ju.bus.networking.ResponseWrapperArray;
import com.hossain.ju.bus.utils.APIError;
import com.hossain.ju.bus.utils.CustomProgressDialog;
import com.hossain.ju.bus.utils.ErrorUtils;
import com.hossain.ju.bus.utils.Utils;
import com.hossain.ju.bus.views.UI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private Context mContext;
    RecyclerView notificationRecyclerView;
    APIServices apiServices;
    private String TAG = "NotificationActivity";
    private RecyclerViewAdapter recyclerViewAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_notification);
        setTitle("Notices");
       setupToolbar();

        notificationRecyclerView = (RecyclerView) findViewById(R.id.notificationRecyclerView);
        apiServices = APIClient.getInstance().create(APIServices.class);

        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerViewAdapter.setListOfNotification(new ArrayList<Notice>());
        notificationRecyclerView.setAdapter(recyclerViewAdapter);
        Utils.applyCustomFont(mContext,getWindow().getDecorView().getRootView());
        getNotification();
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holders> {

        List<Notice> notices;
        Context context;

        public RecyclerViewAdapter(Context context) {
            this.context = context;
        }

        public void setListOfNotification(List<Notice> notices) {
            this.notices = notices;
            Log.d("TTTT22", notices.size() + "");
        }

        @Override
        public Holders onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.root_notification_layout, parent, false);
            Utils.applyCustomFont(mContext,view);
            return new Holders(view);
        }

        @Override
        public void onBindViewHolder(Holders holder, int position) {
            Log.d("TTTT22", notices.get(position).getNotice());
            holder.title.setText(notices.get(position).getNotice());
        }

        @Override
        public int getItemCount() {
            return notices.size();
        }

        public class Holders extends RecyclerView.ViewHolder {
            TextView title;

            public Holders(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.titles);
            }
        }
    }

    private void getNotification() {

        final CustomProgressDialog progressDialog = UI.show(this);
        String token = Utils.BEARER + SharedPreferencesHelper.getToken(this);
        // Log.e("Token::",token);
        Call<ResponseWrapperArray<Notice>> response = apiServices.getAllNotification(token);

        response.enqueue(new Callback<ResponseWrapperArray<Notice>>() {
            @Override
            public void onResponse(Call<ResponseWrapperArray<Notice>> call, Response<ResponseWrapperArray<Notice>> response) {
                progressDialog.dismissAllowingStateLoss();

                Log.e(TAG, response.body().getData().toString());
                try {
                    if (response != null && response.isSuccessful()) {
                        Log.d("TTTTT", response.body().getData().size() + "");
                        recyclerViewAdapter.setListOfNotification(response.body().getData());
                        recyclerViewAdapter.notifyDataSetChanged();
                        notificationRecyclerView.invalidate();
                    } else {
                        // parse the response body …
                        try {
                            APIError error = ErrorUtils.parseError(response);
                            if (error != null)
                                Utils.toast(NotificationActivity.this, error.message());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapperArray<Notice>> call, Throwable t) {
                // there is more than just a failing request (like: no internet connection)
                progressDialog.dismissAllowingStateLoss();

                Utils.toast(NotificationActivity.this, "Data Failed!");
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
