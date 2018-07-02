package com.hossain.ju.bus.utils;

/**
 * Created by mohammod.hossain on 2/11/2016.
 */


public abstract class Day {

    public static final String TODAY        = DateUtils.getToday(DateUtils.FORMAT2);
    public static final String YESTERDAY    = DateUtils.getYesterday(DateUtils.FORMAT2);
    public static final String TOMORROW     = DateUtils.getNextDay(DateUtils.FORMAT2);

}
