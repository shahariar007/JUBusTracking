package com.hossain.ju.bus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goka.blurredgridmenu.BlurredGridMenuConfig;
import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.menuActivity.MenuHolderActivity;
import com.hossain.ju.bus.model.user.User;
import com.hossain.ju.bus.utils.CustomProgressDialog;
import com.hossain.ju.bus.utils.Utils;
import com.hossain.ju.bus.views.UI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hossain.ju.bus.utils.Utils.TAG;


public class MenuActivity extends AppCompatActivity {
    private GridMenuFragment mGridMenuFragment;
    GridView gridView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private Context mContext;

     String profileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Utils.applyCustomFont(mContext,getWindow().getDecorView().getRootView());
        mContext =  this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("TAG:", "Called2.");


        dl = (DrawerLayout) findViewById(R.id.activity_main);

        t = new ActionBarDrawerToggle(this, dl, R.string.openDrawer, R.string.closeDrawer);
        dl.addDrawerListener(t);
        t.syncState();
        nv = (NavigationView) findViewById(R.id.nv);
        View v  =  nv.getHeaderView(0);
        TextView name = (TextView) v.findViewById(R.id.txtProfileName);
       name.setText( getIntent().getStringExtra("NAME"));

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = new Intent(MenuActivity.this, MenuHolderActivity.class);
                switch (id) {
                    case R.id.change_profile_picture:
                        intent.putExtra("position", 0);
                        intent.putExtra("title", "Change Profile Picture");
                        break;
                    case R.id.edit_profile:
                        intent.putExtra("position", 1);
                        intent.putExtra("title", "Edit Profile");
                        break;
                    case R.id.change_password:
                        intent.putExtra("position", 2);
                        intent.putExtra("title", "Change Password");
                        break;
                    case R.id.privacy:
                        intent.putExtra("position", 3);
                        intent.putExtra("title", "Privacy");
                        break;
                    case R.id.logout:
                        intent.putExtra("position", 4);
                        intent.putExtra("title", "Logout");
                        break;
                }
                toggle();
                startActivity(intent);
                return true;
            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        GridAdapter gridAdapter = new GridAdapter(setupGridMenu(), this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                //Toast.makeText(MenuActivity.this, "position:=" + position, Toast.LENGTH_SHORT).show();
                                                switch (position) {
                                                    case 0:
                                                        Utils.toast(mContext,"Upcoming....");
                                                        break;
                                                    case 1:
                                                        Intent intent = new Intent(MenuActivity.this, RoutesActivity.class);
                                                        intent.putExtra("fav", false);
                                                        startActivity(intent);
                                                        break;
                                                    case 2:
                                                        Utils.toast(mContext,"Upcoming....");
                                                        break;
                                                    case 3:
                                                        Utils.toast(mContext,"Upcoming....");
                                                        break;
                                                    case 4:
                                                        Intent intent1 = new Intent(MenuActivity.this, RoutesActivity.class);
                                                        intent1.putExtra("fav", true);
                                                        startActivity(intent1);
                                                        break;

                                                }
                                            }
                                        }
        );
    }

    // Configuration (You can call on Application)
    private void makeBlurConfig() {
        Log.e("TAG:", "Called3.");
        BlurredGridMenuConfig
                .build(new BlurredGridMenuConfig.Builder()
                        .radius(1)
                        .downsample(1)
                        .overlayColor(Color.parseColor("#AA000000")));
    }

    private List<GridMenu> setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("Timetables", R.drawable.calendar_1));
        menus.add(new GridMenu("Routes", R.drawable.route_1));
        menus.add(new GridMenu("Stops", R.drawable.bus_stop_1));
        menus.add(new GridMenu("Traffic", R.drawable.traffic_1));
        menus.add(new GridMenu("Favorites", R.drawable.ic_menu_favourite_list_1));
        return menus;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void toggle() {
        if (dl.isDrawerOpen(GravityCompat.END)) {
            dl.closeDrawer(GravityCompat.END);
        } else {
            dl.openDrawer(GravityCompat.END);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.menus: {
                toggle();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setTitle("BUS Tracking");
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                MenuActivity.this.finish();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alertDialog.show();

    }

    public class GridAdapter extends BaseAdapter {
        List<GridMenu> menus;
        Context context;

        public GridAdapter(List<GridMenu> menus, Context context) {
            this.menus = menus;
            this.context = context;
        }

        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public Object getItem(int position) {
            return menus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View v = LayoutInflater.from(context).inflate(R.layout.root_grid_view_layout, parent, false);
            ImageView imageView = (ImageView) v.findViewById(R.id.imageGrid);
            TextView textView = (TextView) v.findViewById(R.id.textTitle);
            imageView.setImageResource(menus.get(position).getIcon());
            textView.setText(menus.get(position).getTitle());
            return v;
        }
    }




}
