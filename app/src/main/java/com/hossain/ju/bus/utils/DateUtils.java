package com.hossain.ju.bus.utils;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tariqul.islam on 07-09-15.
 */

public class DateUtils {

    private final static String TAG = "DateUtils";
    /**
     * Example: 2015-09-07 02:56:03 PM
     */
    public static String FORMAT1                     = "yyyy-MM-dd hh:mm:ss a";

    /**
     * Example: 07/09/2015
     */
    public static String FORMAT2                     = "dd/MM/yyyy";

    /**
     * Example: Sep 07, 2015
     */
    public static String FORMAT3                     = "MMM dd, yyyy";

    /**
     * Example: Monday
     */
    public static String FORMAT4                     = "EEEE";

    /**
     * Example: 2015-10-11
     */
    public static String FORMAT5                     = "yyyy-MM-dd";

    /**
     * Example: 2015-10-11
     */
    public static String FORMAT6                     = "dd MMM yyyy";


//    public static String getTodaysDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH);
//        Date date = new Date(System.currentTimeMillis());
//        String todaysDate = dateFormat.format(date);
//        // Log.d(TAG,""+todaysDate);
//        InvoiceDetail todaysDate;
//    }

//    public static String getTodaysDay(){
//        String weekdays[] = new DateFormatSymbols(Locale.ENGLISH).getWeekdays();
//        InvoiceDetail weekdays[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)];
//    }

    public static String getToday(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    public static String getNextDay(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        return dateFormat.format(calendar.getTime());
    }

    public static String getYesterday(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return dateFormat.format(calendar.getTime());
    }

    public static String getWeeksFirstDay(String format, int weekStartDay){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.setFirstDayOfWeek(weekStartDay);
        c.set(Calendar.DAY_OF_WEEK, weekStartDay);
        return dateFormat.format(c.getTime());
    }

    public static String getPrevWeeksFirstDay(String format, int weekStartDay){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.setFirstDayOfWeek(weekStartDay);
        c.set(Calendar.DAY_OF_WEEK, weekStartDay);
        c.add(Calendar.WEEK_OF_MONTH, -1);
        return dateFormat.format(c.getTime());
    }

    public static String getPrevWeeksLastDay(String format, int weekEndDay){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.setFirstDayOfWeek(weekEndDay);
        c.set(Calendar.DAY_OF_WEEK, weekEndDay);
        return dateFormat.format(c.getTime());
    }

    public static String getMonthsFirstDay(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(Calendar.DAY_OF_MONTH, 1);
//        Log.i(tag, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static String getPrevMonthsFirstDay(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
//        Log.i(tag, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static String getTodaysDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        String todaysDate = dateFormat.format(date);
      //  Log.d(TAG, todaysDate + "");
        return todaysDate;
    }

    /**
     * Gets previous or past day, based on current day.
     * Use this method to calculate the date which is x days past or after today.
     * To calculate the date which is 5 day before today: getDateFromToday(-5, MMM dd, yyyy)
     * @param numberOfDays -ve or +ve for previous or later days.
     * @param format
     * @return the calculated date
     */
    public static String getDateFromToday(int numberOfDays, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, numberOfDays);
        return dateFormat.format(calendar.getTime());
    }

    public static long convertDateToLong(String date, String currentFormat){
        SimpleDateFormat dateFormat = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
        try {
            Date longDate = dateFormat.parse(date);
            return longDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static Date getSystemDate(){
        return new Date();
    }

    public static boolean isSameDay(Date firstDate, Date lastDate){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(firstDate);
        cal2.setTime(lastDate);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    static String FORMAT11 = "yyyy-MM-dd hh:mm a";
    public static String checkTaskDate(String taskDate){
        String str = null;
        if(taskDate  != null) {

            if(taskDate.equals(Day.TODAY)){
                Log.d("TAG", "Today");
                return   "TODAY";
            }else if(taskDate.equals(Day.YESTERDAY)){
                Log.d("TAG", "YESTERDAY");
                return "YESTERDAY";
            }else if(taskDate.equals(Day.TOMORROW )){
                Log.d("TAG", "TOMORROW");
                return "TOMORROW";
            }else{
                Log.d("TAG", taskDate);
                return  taskDate;
            }
        }
        return  null;
    }

    public static String getFormattedDate(String inputString){
        SimpleDateFormat fromUser = new SimpleDateFormat(FORMAT6);
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String reformattedStr = null;
        try {
             reformattedStr = myFormat.format(fromUser.parse(inputString));
            Log.d("TAG",reformattedStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return reformattedStr;
    }


    public static String getTodaysDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Date date = new Date(System.currentTimeMillis());
        String todaysDate = dateFormat.format(date);
        // Log.d(TAG,""+todaysDate);
        return todaysDate;
    }

    public static String getTodaysDay(){
        String weekdays[] = new DateFormatSymbols(Locale.ENGLISH).getWeekdays();
        return weekdays[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)];
//		InvoiceDetail "Monday";
    }

    public static String getTodaysDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        String todaysDate = dateFormat.format(date);
        Log.d(TAG, "" + todaysDate);
        return todaysDate;
    }


    public static String getWeeksFirstDay(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
//        Log.i(tag, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static String getPrevWeeksFirstDay(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        c.add(Calendar.WEEK_OF_MONTH, -1);
//        Log.i(tag, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static String getPrevWeeksLastDay(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.FRIDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
//        c.add(Calendar.WEEK_OF_MONTH, -1);
//        Log.i(tag, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }




    public static String getPrevMonthsLastDay(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.setFirstDayOfWeek(Calendar.SATURDAY);
//        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 0);
        Log.i(TAG, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static boolean isDatesSame(String day1, String day2){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(day1) == dateFormat.parse(day2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


}
