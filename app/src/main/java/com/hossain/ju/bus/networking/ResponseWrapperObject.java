package com.hossain.ju.bus.networking;

/**
 * Created by Tariqul.Islam on 8/1/17.
 */

public class ResponseWrapperObject<T> {


    String status, message;
    T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
