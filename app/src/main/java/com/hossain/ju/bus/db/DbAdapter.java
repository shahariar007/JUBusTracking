package com.hossain.ju.bus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hossain.ju.bus.location.Location;

import java.util.ArrayList;
import java.util.List;

import static com.hossain.ju.bus.db.LocationContract.TransLocation.COLUMN_NAME_FAV_ID;


/**
 * Created by Motuza on 2/17/2016.
 */
public class DbAdapter {
    private static final String TAG = "DbAdapter";

    private Context context;
    private DbHelper dbHelper;

    public DbAdapter(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    /**
     * Inserts a new location to the table locations
     */
    public long insertTransLocation(ContentValues contentValues) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowID = 0;
        try {
            rowID = db.insert(LocationContract.TransLocation.TABLE_TRANS_LOCATION_LOG, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return rowID;
    }

    public long insertFavoriteItem(ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowID = 0;
        try {
            rowID = db.insert(LocationContract.TransLocation.TABLE_FAV_LOG, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return rowID;
    }

    public List<Integer> getFavListData() {
        List<Integer> favLists = new ArrayList<>();
        try {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select fav_id from " + LocationContract.TransLocation.TABLE_FAV_LOG, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FAV_ID));
                    favLists.add(id);
                    cursor.moveToNext();
                }
            }
            if (db.isOpen()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.getStackTrace();
            return favLists = null;
        }
        return favLists;
    }


    /**
     * Inserts a new location to the table locations
     */
    public List<Location> getTracking() {

        List<Location> trackingList = new ArrayList<Location>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // String status = "A";
            //  String whereClause =  CUST_STATUS+ " = '"+status+"'";
            String[] columns = {
                    LocationContract.TransLocation._ID, LocationContract.TransLocation.COLUMN_NAME_TRANS_LAT, LocationContract.TransLocation.COLUMN_NAME_TRANS_LONG, LocationContract.TransLocation.COLUMN_NAME_TRANS_ADDRESS, LocationContract.TransLocation.COLUMN_NAME_TRANS_SAVE_DATE_TIME
            };
            Cursor cursor = db.query(LocationContract.TransLocation.TABLE_TRANS_LOCATION_LOG, columns, null, null, null, null, LocationContract.TransLocation._ID + " DESC");

            if (cursor != null && cursor.getCount() > 0) {
                //Log.d("cursor.getCount()::", cursor.getCount()+"");

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        int id = cursor.getInt(cursor.getColumnIndex(LocationContract.TransLocation._ID));
                        String lat = cursor.getString(cursor.getColumnIndex(LocationContract.TransLocation.COLUMN_NAME_TRANS_LAT));
                        String longi = cursor.getString(cursor.getColumnIndex(LocationContract.TransLocation.COLUMN_NAME_TRANS_LONG));
                        String address = cursor.getString(cursor.getColumnIndex(LocationContract.TransLocation.COLUMN_NAME_TRANS_ADDRESS));
                        String datetime = cursor.getString(cursor.getColumnIndex(LocationContract.TransLocation.COLUMN_NAME_TRANS_SAVE_DATE_TIME));

                        Log.e("datetime:", datetime + ":" + lat + "::" + longi);


                        Location location = new Location();
                        location.setLocId(String.valueOf(id));
                        location.setLatitude(lat);
                        location.setLongitude(longi);
                        location.setLocationName(address);
                        location.setSaveTime(datetime);
                        trackingList.add(location);
                        cursor.moveToNext();
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return trackingList;
    }


    public int deleteSrLocation(int id) {
        int row = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            row = db.delete(LocationContract.TransLocation.TABLE_TRANS_LOCATION_LOG, LocationContract.TransLocation._ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return row;
    }

    public int deleteID(int id) {
        int row = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            row = db.delete(LocationContract.TransLocation.TABLE_FAV_LOG, LocationContract.TransLocation.COLUMN_NAME_FAV_ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return row;
    }

}
