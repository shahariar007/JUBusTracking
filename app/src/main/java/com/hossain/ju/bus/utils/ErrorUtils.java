package com.hossain.ju.bus.utils;

import com.hossain.ju.bus.networking.APIClient;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;

import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by mohammod.hossain on 9/28/2017.
 */

public class ErrorUtils {

    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
               APIClient.getInstance()
                        .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error = null;

        try {
            if(response.errorBody() != null){
                error = converter.convert(response.errorBody());
            }

        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }

}