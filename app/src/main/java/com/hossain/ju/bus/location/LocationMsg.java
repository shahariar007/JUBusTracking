package com.hossain.ju.bus.location;

import android.content.Context;

import com.hossain.ju.bus.utils.Utils;


/**
 * Created by mohammod.hossain on 6/21/2016.
 */
public class LocationMsg {

    public final static String MSG_SEPARATION = "|";
    public final static String MSG_TERMINATOR = ";";

    private Context mContext;
    public LocationMsg(Context context){
        this.mContext = context;
    }

    //LOC_STR Message pattern: lat,long|address |location time|battery level;

   // Sample  msg:1054|23.7807855,90.406775|11 Bir Uttam AK Khandaker Road,Gulshan,Dhaka,1212,Bangladesh|2016-06-22 15:46:50;
  //  id|12.56984455| 45.05588880|1 Bir Uttam AK Khandaker Road,Gulshan,Dhaka,1212,Bangladesh|2016-06-22 15:46:50|10.30;


    public String getLocationMsg(String userId, String lat, String longitude, String address, String datetime, int bl){

        StringBuilder sb = new StringBuilder();
        sb.append(userId);
        sb.append(MSG_SEPARATION);
        sb.append(lat);
        sb.append(MSG_SEPARATION);
        sb.append(longitude);
        sb.append(MSG_SEPARATION);
        sb.append(address);
        sb.append(MSG_SEPARATION);
        sb.append(datetime );
        sb.append(MSG_SEPARATION);
        sb.append( bl);
        sb.append(MSG_TERMINATOR);
        return  sb.toString();
    }

    private float getBatteryLevel(){
        return Utils.getBatteryLevel(mContext);
    }

}
