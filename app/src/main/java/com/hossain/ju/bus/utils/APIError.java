package com.hossain.ju.bus.utils;

/**
 * Created by mohammod.hossain on 9/28/2017.
 */

public class APIError {
    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
