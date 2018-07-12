package com.hossain.ju.bus.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.hossain.ju.bus.MenuActivity;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.ACTIVITY_SERVICE;

public class Utils {

	public final static String TAG = "Utils";
	private ProgressDialog pDialog;
	private Context context;

	public final static int MY_SOCKET_TIMEOUT_MS                                    = 20000;
	public final static int NEW_ORDER_FRAGMENT_INDEX                                = 0;

	public final static String USER_ID                              = "admin";
	public final static String PASSWORD                             = "1234";
	public final static String BEARER                             = "Bearer ";

	public final static String SCHEDULE_ID                         = "SCHEDULE_ID ";
	public final static int  REQUEST_DELAY                         = 30;

	public final static String RESET_PASSWORD_URL              = "https://gps.zists.com/password/reset";



	public Utils(Context context){
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("loading...");
	}

    public static boolean isConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnectedOrConnecting();

	}

	public static void hideSoftKeyboard(Activity activity) {
		Log.d(TAG, ":: " + activity);
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);

		if (inputMethodManager != null && activity != null
				&& activity.getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}


	public static String formattedUserId(String userId) {
		if (userId.length() > 0) {
			return String.format("%08d", Integer.parseInt(userId));
		}
		return userId;
	}

	public static final String checkDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}

	public static Date getDate(String frmDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = formatter.parse(frmDate);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String lTrim(String s) {
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return s.substring(i);
	}

	public static String rTrim(String s) {
		int i = s.length() - 1;
		while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
			i--;
		}
		return s.substring(0, i + 1);
	}



	public static void toast(Context context, String str){
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void getOverflowMenu(Context context) {
		try {
			ViewConfiguration config = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void applyCustomFont(Context context, View rootView) {
		if (rootView instanceof ViewGroup) {
			ViewGroup list = (ViewGroup)rootView;
			for (int i = 0; i < list.getChildCount(); i++) {
				View view = list.getChildAt(i);
				if (view instanceof ViewGroup) {
					applyCustomFont(context, (ViewGroup) view);
				} else if (view instanceof TextView) {
					TextView textView = (TextView) view;
					Typeface customTypeface = textView.getTypeface();
					if (customTypeface != null) {
						if (customTypeface.getStyle() == Typeface.BOLD) {
							customTypeface = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-SemiBold.ttf");
						} else {
							customTypeface = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-ExtraLight.ttf");
						}
						textView.setTypeface(customTypeface);
					} else {
						customTypeface = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-ExtraLight.ttf");
						textView.setTypeface(customTypeface);
					}
				}
			}
		} else if (rootView instanceof TextView) {
			TextView textView = (TextView) rootView;
			Typeface customTypeface = textView.getTypeface();
			if (customTypeface != null) {
				if (customTypeface.getStyle() == Typeface.BOLD) {
					customTypeface = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-SemiBold.ttf");
				} else {
					customTypeface = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-ExtraLight.ttf");
				}
				textView.setTypeface(customTypeface);
			} else {
				customTypeface = Typeface.createFromAsset(context.getAssets(), "TitilliumWeb-ExtraLight.ttf");
				textView.setTypeface(customTypeface);
			}
		}
	}

	public static String getAppVersion(Context context) {
		String str = "Hossain V: ";
		try {
			str += context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public void showProgressDialog() {
		if (pDialog != null && !pDialog.isShowing())
			pDialog.show();
	}

	public void hideProgressDialog() {
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
	}

	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will launch Settings Options
	 * */
	public static void showSettingsAlert(final Activity activity) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		alertDialog.setTitle("GPS  settings");
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				activity.startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.show();
	}


	// check device battery level
	public static float getBatteryLevel(Context context) {
		Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		// Error checking that probably isn't needed but I added just in case.
		if(level == -1 || scale == -1) {
			return 50.0f;
		}

		return ((float)level / (float)scale) * 100.0f;
	}

	private  static boolean isLocServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.hossain.ju.tracking.location.LocationUpdateIntentService".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}


	public static  double calculationByDistance(LatLng StartP, LatLng EndP) {
		int Radius = 6371;// radius of earth in Km
		double lat1 = StartP.latitude;
		double lat2 = EndP.latitude;
		double lon1 = StartP.longitude;
		double lon2 = EndP.longitude;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double valueResult = Radius * c;
		double km = valueResult / 1;
		DecimalFormat newFormat = new DecimalFormat("####");
		int kmInDec = Integer.valueOf(newFormat.format(km));
		double meter = valueResult % 1000;
		int meterInDec = Integer.valueOf(newFormat.format(meter));
		Log.e("Radius Value", "" + valueResult + "   KM  " + kmInDec
				+ " Meter   " + meterInDec);

		return Radius * c;
	}

	public static String round(double value) {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		System.out.println("round val:: " + formatter.format(value));
		return formatter.format(value);
	}

	public static boolean logout(final Context mcContext){

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcContext);
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setTitle("BUS Tracking");
		alertDialog.setMessage("Do you want to logout?");
		alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				//MenuActivity.this.finish();

				if(SharedPreferencesHelper.getLastUserId(mcContext) != null){
					SharedPreferencesHelper.setLastUserID(mcContext ,"");
					SharedPreferencesHelper.setLastPassword(mcContext ,"");
					SharedPreferencesHelper.setPass(mcContext ,"");
					SharedPreferencesHelper.setISLogin(mcContext ,"0");
					SharedPreferencesHelper.setToken(mcContext ,"");
				}
			}
		});

		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});

		alertDialog.show();

		return  true;
	}



}
