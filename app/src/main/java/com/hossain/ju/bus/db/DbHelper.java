package com.hossain.ju.bus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mohammod.hossain on 2/13/2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String LOGCAT = "DbHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "transport_tracking.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static final String SQL_CREATE_TRANS_LOCATION_LOG =
            "CREATE TABLE " + LocationContract.TransLocation.TABLE_TRANS_LOCATION_LOG + " (" +
                    LocationContract.TransLocation._ID + INTEGER_TYPE + "  PRIMARY KEY," +
                    LocationContract.TransLocation.COLUMN_NAME_USER_ID + TEXT_TYPE + COMMA_SEP +
                    LocationContract.TransLocation.COLUMN_NAME_TRANS_LAT + TEXT_TYPE + COMMA_SEP +
                    LocationContract.TransLocation.COLUMN_NAME_TRANS_LONG + TEXT_TYPE + COMMA_SEP +
                    LocationContract.TransLocation.COLUMN_NAME_TRANS_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    LocationContract.TransLocation.COLUMN_NAME_TRANS_SAVE_DATE_TIME + TEXT_TYPE + COMMA_SEP +
                    LocationContract.TransLocation.COLUMN_NAME_TRANS_DEVICE_BATTERY_LEVEL + INTEGER_TYPE + COMMA_SEP +
                    LocationContract.TransLocation.COLUMN_NAME_STATUS + TEXT_TYPE +
                    LocationContract.TransLocation.COLUMN_NAME_CREATED_DATE + TEXT_TYPE + COMMA_SEP +
                    LocationContract.TransLocation.COLUMN_NAME_UPDATED_DATE + TEXT_TYPE + " )";

    private static final String SQL_CREATE_FAV =
            "CREATE TABLE " + LocationContract.TransLocation.TABLE_FAV_LOG + " (" +
                    LocationContract.TransLocation._ID + INTEGER_TYPE + "  PRIMARY KEY," +
                    LocationContract.TransLocation.COLUMN_NAME_FAV_ID + INTEGER_TYPE + " )";

    /**
     * SQL statement to drop "entry" table.
     */
    private static final String SQL_DELETE_TRANS_LOC =
            "DROP TABLE IF EXISTS " + "TABLE_TRANS_LOCATION_LOG";
    /**
     * Favorite table Delete
     */

    private static final String SQL_DELETE_FAV =
            "DROP TABLE IF EXISTS " + LocationContract.TransLocation.TABLE_FAV_LOG;

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate::", "CALLED........");

        db.execSQL(SQL_CREATE_TRANS_LOCATION_LOG);
        db.execSQL(SQL_CREATE_FAV);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Log the version upgrade.
        Log.w(DbHelper.class.getName(), "Upgrading from version" +
                newVersion + " to " + oldVersion + ", which will destroy all old data");
        Log.d(LOGCAT, "DB created.....000........");

        db.execSQL(SQL_DELETE_TRANS_LOC);
        db.execSQL(SQL_DELETE_FAV);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
