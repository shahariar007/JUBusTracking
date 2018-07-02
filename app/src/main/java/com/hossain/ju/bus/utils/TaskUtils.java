package com.hossain.ju.bus.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by mohammod.hossain on 2/7/2016.
 */
public class TaskUtils {
    private static String TAG  = "TaskUtils";

    private Context context;


    public TaskUtils(Context context){
        this.context = context;
    }

    public  static String getAddress(List<Address> addresses) {
        StringBuilder result = new StringBuilder();
        try {
            if (addresses == null || addresses.size()  == 0) {
            }else {
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    result.append(address.getAddressLine(0)).append(",");
                    result.append(address.getSubLocality()).append(",");
                    result.append(address.getLocality()).append(",");
                    if(address.getPostalCode() != null){
                        result.append(address.getPostalCode()).append(",");
                    }

                    result.append(address.getCountryName());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result.toString();
    }

    public String getAddress(Context context, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses == null || addresses.size()  == 0) {
            }else {
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    result.append(address.getAddressLine(0)).append(",");
                    result.append(address.getSubLocality()).append(",");
                    result.append(address.getLocality()).append(",");
                    result.append(address.getPostalCode()).append(",");
                    result.append(address.getCountryName());
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return result.toString();
    }


    public ArrayList<HashMap<String, String>> readJson(){
        ArrayList<HashMap<String, String>> taskList = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(context));
            if(jsonArray != null && jsonArray.length() > 0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> task = new HashMap<String, String>();
                    task.put("task_id", jsonObject.getString("task_id"));
                    task.put("task_title", jsonObject.getString("task_title"));
                    task.put("visiting_person", jsonObject.getString("visiting_person"));
                    task.put("visit_date", jsonObject.getString("visit_date"));
                    task.put("address", jsonObject.getString("address"));
                    task.put("remarks", jsonObject.getString("remarks"));
                    taskList.add(task);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  taskList;
    }

    private String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("task.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
