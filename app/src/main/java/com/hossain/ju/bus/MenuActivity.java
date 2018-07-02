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

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity {
    private GridMenuFragment mGridMenuFragment;
    GridView gridView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("TAG:", "Called2.");

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.openDrawer, R.string.closeDrawer);
        dl.addDrawerListener(t);
        t.syncState();
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.account:
                        Toast.makeText(MenuActivity.this, "My Account", Toast.LENGTH_SHORT).show();
                    case R.id.settings:
                        Toast.makeText(MenuActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                    case R.id.mycart:
                        Toast.makeText(MenuActivity.this, "My Cart", Toast.LENGTH_SHORT).show();
                    default:
                        toggle();
                        return true;
                }
            }
        });
        //makeBlurConfig();

//        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.ju_bg);
//        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        tx.replace(R.id.main_frame, mGridMenuFragment);
//        tx.addToBackStack(null);
//        tx.commit();

        //setupGridMenu();

//        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
//            @Override
//            public void onClickMenu(GridMenu gridMenu, int position) {
//                Toast.makeText(MenuActivity.this, "Title:" + gridMenu.getTitle() + ", Position:" + position,
//                        Toast.LENGTH_SHORT).show();
//
//                if (position == 1) {
//                    Intent intent = new Intent(MenuActivity.this, RoutesActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });
        gridView = (GridView) findViewById(R.id.gridView);
        GridAdapter gridAdapter = new GridAdapter(setupGridMenu(), this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Toast.makeText(MenuActivity.this, "position:=" + position, Toast.LENGTH_SHORT).show();
                                                switch (position) {
                                                    case 0:
                                                        break;
                                                    case 1:
                                                        Intent intent = new Intent(MenuActivity.this, RoutesActivity.class);
                                                        intent.putExtra("fav", false);
                                                        startActivity(intent);
                                                        break;
                                                    case 2:
                                                        break;
                                                    case 3:
                                                        break;
                                                    case 4:
                                                        Intent intent1 = new Intent(MenuActivity.this, RoutesActivity.class);
                                                        intent1.putExtra("fav", true);
                                                        startActivity(intent1);
                                                        break;
                                                    case 5:
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
        menus.add(new GridMenu("Favorite Route", R.drawable.traffic_1));
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
