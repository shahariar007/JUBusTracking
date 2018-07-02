package com.hossain.ju.bus.views;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hossain.ju.bus.utils.CustomProgressDialog;


/**
 * Created by Tariqul.Islam on 8/3/17.
 */

public class UI {

    public static void displayToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void log(String str) {
        Log.i("jotno-default-log", str);
    }

    public static CustomProgressDialog show(Context context) {

        FragmentManager manager = ((Activity) context).getFragmentManager();
        Fragment frag = manager.findFragmentByTag("FRagsDialog");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        CustomProgressDialog customProgressDialog = new CustomProgressDialog();

        customProgressDialog.show(manager, "FRagsDialog");
        return customProgressDialog;
    }

}

