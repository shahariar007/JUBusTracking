
package com.hossain.ju.bus.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public final class SharedPreferencesHelper {

    private static final String LOG_TAG = "SharedPreferencesHelper";

    // Dialogs Id
    public static final int DIALOG_ABOUT = 0;
    public static final int DIALOG_NO_CONNECTION = 1;
    public static final int DIALOG_UPDATE_PROGRESS = 2;


    public static final String REMEMBER = "remember";

    // App Preferences
    private static final String PREFS_FILE_NAME = "AppPreferences";


    private static final String USER = "user";
    private static final String NEWID = "0";

    private static final String PASS = "pass";

    private static final String EmpReg = "empReg";
    private static final String IS_LOGIN = "isLogin";

    private static final String LAST_USER_ID  = "last_user_id";
    private static final String LAST_PASSWORD = "last_password";
    private static final String TOKEN = "token";


    public static String getToken(final Context ctx) {
        return ctx.getSharedPreferences(SharedPreferencesHelper.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharedPreferencesHelper.REMEMBER, "");
    }

    public static void setToken(final Context ctx, final String token ) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                SharedPreferencesHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(SharedPreferencesHelper.REMEMBER, token);
        editor.commit();
    }



    public static boolean getRemember(final Context ctx) {
        return ctx.getSharedPreferences(SharedPreferencesHelper.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getBoolean(SharedPreferencesHelper.REMEMBER, false);
    }

    public static void setRemember(final Context ctx, final boolean flag) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                SharedPreferencesHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putBoolean(SharedPreferencesHelper.REMEMBER, flag);
        editor.commit();
    }

    public static String getUser(final Context ctx) {
        return ctx.getSharedPreferences(SharedPreferencesHelper.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharedPreferencesHelper.USER, "");
    }

    public static void setUser(final Context ctx, final String user) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                SharedPreferencesHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(SharedPreferencesHelper.USER, user);
        editor.commit();
    }
    
    public static String getNewId(final Context ctx) {
        return ctx.getSharedPreferences(SharedPreferencesHelper.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharedPreferencesHelper.NEWID, "0");
    }

    public static void setNewId(final Context ctx, final String user) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                SharedPreferencesHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(SharedPreferencesHelper.NEWID, user);
        editor.commit();
    }

    
    public static String getIsLogin(final Context ctx) {
        return ctx.getSharedPreferences(SharedPreferencesHelper.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharedPreferencesHelper.IS_LOGIN, "0");
    }

    public static void setISLogin(final Context ctx, final String isLogin) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                SharedPreferencesHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(SharedPreferencesHelper.IS_LOGIN, isLogin);
        editor.commit();
    }

    public static String getPass(final Context ctx) {
        return ctx.getSharedPreferences(SharedPreferencesHelper.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharedPreferencesHelper.PASS, "");
    }

    public static void setPass(final Context ctx, final String pass) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                SharedPreferencesHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(SharedPreferencesHelper.PASS, pass);
        editor.commit();
    }

    public static int getSplashDuration(final Context ctx) {
        int splashScreenDuration = 2000;
        try {
            final ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(
                    ctx.getPackageName(), PackageManager.GET_META_DATA);
            splashScreenDuration = ai.metaData.getInt("splash_screen_duration");
        } catch (final NameNotFoundException nnfe) {
            Log.e(SharedPreferencesHelper.LOG_TAG, "", nnfe);
        }
        return splashScreenDuration;
    }

    public static String getFlurryAnalyticsApiKey(final Context ctx) {
        String flurryAnalyticsApiKey = null;
        try {
            final ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(
                    ctx.getPackageName(), PackageManager.GET_META_DATA);
            flurryAnalyticsApiKey = ai.metaData.getString("FLURRY_API_KEY");
        } catch (final NameNotFoundException nnfe) {
            Log.e(SharedPreferencesHelper.LOG_TAG, "", nnfe);
        }
        return flurryAnalyticsApiKey;
    }

    public static String getGoogleAnalyticsProfileId(final Context ctx) {
        String googleAnalyticsProfileId = null;
        try {
            final ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(
                    ctx.getPackageName(), PackageManager.GET_META_DATA);
            googleAnalyticsProfileId = ai.metaData.getString("GOOGLE_ANALYTICS_PROFILE_ID");
        } catch (final NameNotFoundException nnfe) {
            Log.e(SharedPreferencesHelper.LOG_TAG, "", nnfe);
        }
        return googleAnalyticsProfileId;
    }

    public static String getMobclixApplicationId(final Context ctx) {
        String mobclixApplicationId = null;
        try {
            final ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(
                    ctx.getPackageName(), PackageManager.GET_META_DATA);
            mobclixApplicationId = ai.metaData.getString("com.mobclix.APPLICATION_ID");
        } catch (final NameNotFoundException nnfe) {
            Log.e(SharedPreferencesHelper.LOG_TAG, "", nnfe);
        }
        return mobclixApplicationId;
    }

    public static boolean trackFlurryAnalytics(final Context ctx) {
        boolean track = true;
        final String flurryAnalyticsApiKey = SharedPreferencesHelper.getFlurryAnalyticsApiKey(ctx);

        if (flurryAnalyticsApiKey == null
                || flurryAnalyticsApiKey.equalsIgnoreCase("xxxxxxxxxxxxxxxxxxxx")) {
            track = false;
        } else {
            track = true;
        }

        return track;
    }

    public static boolean trackGoogleAnalytics(final Context ctx) {
        boolean track = true;
        final String googleAnalyticsProfileId = SharedPreferencesHelper
                .getGoogleAnalyticsProfileId(ctx);

        if (googleAnalyticsProfileId == null
                || googleAnalyticsProfileId.equalsIgnoreCase("UA-xxxxx-xx")) {
            track = false;
        } else {
            track = true;
        }

        return track;
    }

    public static boolean trackMobclixSession(final Context ctx) {
        boolean track = true;
        final String mobclixApplicationId = SharedPreferencesHelper.getMobclixApplicationId(ctx);

        if (mobclixApplicationId == null
                || mobclixApplicationId.equalsIgnoreCase("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")) {
            track = false;
        } else {
            track = true;
        }

        return track;
    }

    // Shared getter util methods

    public static CharSequence getVersionName(final Context ctx) {
        CharSequence version_name = "";
        try {
            final PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
            version_name = packageInfo.versionName;
        } catch (final NameNotFoundException nnfe) {
            Log.e(SharedPreferencesHelper.LOG_TAG, "", nnfe);
        }
        return version_name;
    }

    public static boolean isOnline(final Context ctx) {
        final ConnectivityManager cm = (ConnectivityManager)ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return ni.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    public static String getLastUserId(final Context ctx) {
        return ctx.getSharedPreferences(SharedPreferencesHelper.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharedPreferencesHelper.LAST_USER_ID, "");
    }

    public static void setLastUserID(final Context ctx, final String id) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                SharedPreferencesHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(SharedPreferencesHelper.LAST_USER_ID, id);
        editor.apply();
    }

    public static String getLastPassword(final Context ctx) {
        return ctx.getSharedPreferences(SharedPreferencesHelper.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharedPreferencesHelper.LAST_PASSWORD, "");
    }
    public static void setLastPassword(final Context ctx, final String password) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                SharedPreferencesHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(SharedPreferencesHelper.LAST_PASSWORD, password);
        editor.apply();
    }

}
























