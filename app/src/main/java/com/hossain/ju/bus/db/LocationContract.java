package com.hossain.ju.bus.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mohammod.hossain on 3/16/2017.
 */

public class LocationContract {

    public static final String AUTHORITY = "com.hossain.ju.tracking";
    public static final String SCHEME = "content://";
    public static final String SLASH = "/";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public LocationContract() {

    }

    /* Inner class that defines the table contents */
    public static abstract class TransLocation implements BaseColumns {

        public static final String TABLE_TRANS_LOCATION_LOG                 = "TABLE_TRANSPORT_LOCATION_LOG";
        public static final String TABLE_FAV_LOG                 = "TABLE_FAV_LOG";

        public static final String COLUMN_NAME_LOC_ID                           = "_id";
        public static final String COLUMN_NAME_USER_ID                          = "user_id";
        public static final String COLUMN_NAME_TRANS_LAT                        = "trans_lat";
        public static final String COLUMN_NAME_TRANS_LONG                       = "trans_long";
        public static final String COLUMN_NAME_TRANS_ADDRESS                    = "address";
        public static final String COLUMN_NAME_TRANS_SAVE_DATE_TIME             = "save_date_time";
        public static final String COLUMN_NAME_TRANS_DEVICE_BATTERY_LEVEL       = "device_battery_level";
        public static final String COLUMN_NAME_STATUS                           = "status";
        public static final String COLUMN_NAME_CREATED_DATE                     = "created_date";
        public static final String COLUMN_NAME_UPDATED_DATE                     = "updated_date";


        /**
         * fev id
         */
        public static final String COLUMN_NAME_FAV_ID                          = "fav_id";

        /**
         * The content style URI
         */
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_TRANS_LOCATION_LOG);
        /**
         * The content URI base for a single row. An ID must be appended.
         */
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_TRANS_LOCATION_LOG + SLASH);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_LOC_ID + " ASC";

        /**
         * MIME type for lists of tasks.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.hossain.ju.tracking.locations";
        /**
         * MIME type for individual tasks.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.hossain.ju.tracking.location";

    }
}
