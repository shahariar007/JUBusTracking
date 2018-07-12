package com.hossain.ju.bus.menuActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hossain.ju.bus.R;
import com.hossain.ju.bus.fragment.ChangePasswordFragment;
import com.hossain.ju.bus.fragment.ProfileEditFragment;

import java.util.HashMap;

public class MenuHolderActivity extends AppCompatActivity {
    Toolbar toolbar;
    int position = 0;
    HashMap<String, String> parameter;
    private String title = "";

    public void init() {
        setExtraData();
        toolbar = (Toolbar) findViewById(R.id.toolbar_menuActivity);
        setSupportActionBar(toolbar);
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setExtraData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
            parameter = (HashMap<String, String>) bundle.get("parameter");
            title = bundle.getString("title");
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        setFragmentGenerator(position);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }

    public void setFragmentGenerator(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                fragmentTransaction.replace(R.id.fragment, ProfileEditFragment.newInstance("", ""));
                break;
            case 1:
                fragmentTransaction.replace(R.id.fragment, ProfileEditFragment.newInstance("", ""));
                break;
            case 2:
                fragmentTransaction.replace(R.id.fragment, ChangePasswordFragment.newInstance("", ""));
                break;
            case 3:
                fragmentTransaction.replace(R.id.fragment, ProfileEditFragment.newInstance("", ""));
                break;
            case 4:
                fragmentTransaction.replace(R.id.fragment, ProfileEditFragment.newInstance("", ""));
                break;
        }
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
