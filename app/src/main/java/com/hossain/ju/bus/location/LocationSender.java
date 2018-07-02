package com.hossain.ju.bus.location;

import android.content.Context;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by mohammod.hossain on 6/20/2016.
 */
public class LocationSender {

    private static final String TAG = "LocationSender";

    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 0;
    private String tag_send_location_obj = "send_location_req";

    private Context mContext;

    LocationSender(Context context){
        this.mContext = context;
    }

    public void locationSender(final String msg) {
        //new AsyncCallWS().execute(msg);
    }

    public void locationSender(final String msg, final String id) {
       // new AsyncCallWS2().execute(msg,id);
    }





    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';

    public static String createQueryStringForParameters(Map<String, String> parameters) {
        StringBuilder parametersAsQueryString = new StringBuilder();
        if (parameters != null) {
            boolean firstParameter = true;
            for (String parameterName : parameters.keySet()) {
                if (!firstParameter) {
                    parametersAsQueryString.append(PARAMETER_DELIMITER);
                }
                parametersAsQueryString.append(parameterName)
                        .append(PARAMETER_EQUALS_CHAR)
                        .append(parameters.get(parameterName));

                firstParameter = false;
            }
        }
        return parametersAsQueryString.toString();
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }

        return sb.toString();
    }

//    public void locationSender(final String msg) {
//        StringRequest putRequest = new StringRequest(Request.Method.POST, Constants.getInstance().SEND_SR_LOCATION,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response != null && response.length() > 0) {
//                            String result = response;
//                            Log.e("result::",result);
//                        } else if (response != null && response.equals("01422")) {
//                            AlertMessage.showMessage(mContext, "Location  Send", "Location sending failed for Database  exception ");
//                        } else {
//                            AlertMessage.showMessage(mContext, "Location  Send", " Location sending Failed");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (error instanceof ServerError) {
//                            Log.d("ServerError>>>>>>>>>", "ServerError.......");
//                        } else if (error instanceof NetworkError) {
//                            Log.d("NetworkError>>>>>>>>>", "NetworkError.......");
//                        } else if (error instanceof TimeoutError) {
//                            Log.d("TimeoutError>>>>>>>>>", "TimeoutError.......");
//                        } else {
//                            ErrorMessage.checkErrorMessage(error, mContext);
//                        }
//                    }
//                }
//        ) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("regno", SharedPreferencesHelper.getEmpReg(mContext));
//                params.put("locMsg", msg);
//                return params;
//            }
//        };
//
//
//        putRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1,0));
//        putRequest.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(putRequest, tag_send_location_obj);
//        Log.e("putRequest::", "" + putRequest.getRetryPolicy().getCurrentRetryCount());
//    }
}
