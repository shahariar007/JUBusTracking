package com.hossain.ju.bus.networking;

import java.util.List;

/**
 * Created by Tariqul.Islam on 8/1/17.
 */

public class ResponseWrapperArray<T> {


    String status, message;
    List<T> data;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
