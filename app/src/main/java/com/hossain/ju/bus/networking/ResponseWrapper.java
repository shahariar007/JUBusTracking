package com.hossain.ju.bus.networking;

/**
 * Created by Tariqul.Islam on 8/3/17.
 */

public class ResponseWrapper {
    String status, message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess(){
        return status.equals("OK");
    }
}
