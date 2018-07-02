package com.hossain.ju.bus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.networking.APIClient;
import com.hossain.ju.bus.networking.APIServices;
import com.hossain.ju.bus.networking.ResponseWrapperObject;
import com.hossain.ju.bus.utils.APIError;
import com.hossain.ju.bus.utils.CustomProgressDialog;
import com.hossain.ju.bus.utils.ErrorUtils;
import com.hossain.ju.bus.utils.Utils;
import com.hossain.ju.bus.views.UI;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final int REQUEST_SIGNUP = 0;
    private Context mContext;
    private EditText edtUserId,edtPassword;
    private Button btnLogin;
    private CheckBox rememberMe;
    private TextView link_to_register,versionInfo;
    private ProgressDialog pDialog;
    private APIServices apiServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        apiServices = APIClient.getInstance().create(APIServices.class);
        setContentView(R.layout.login);
        initialization();

        final RelativeLayout llLogin = (RelativeLayout)findViewById(R.id.loginLayout);
        llLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                Utils.hideSoftKeyboard(LoginActivity.this);
                return false;
            }
        });

        edtUserId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    String userId = edtUserId.getText().toString();
                    if (userId.length() > 0) {
                        edtPassword.requestFocus();
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
           // onLoginFailed();
            return;
        }else{
            if(!edtUserId.getText().toString().equals("") && !edtPassword.getText().toString().equals("")){
                if (Utils.isConnected(mContext)) {
                    checkLogin();
                } else {
                    Utils.toast(mContext, "Internet is not available");
                }
                //onLoginSuccess();
            }else{
                onLoginFailed();
            }
        }
    }


    private void checkLogin() {

        HashMap<String, Object> inputs = new HashMap<>();
        inputs.put("username", edtUserId.getText().toString());
        inputs.put("password", edtPassword.getText().toString());

        final CustomProgressDialog progressDialog = UI.show(LoginActivity.this);
        Call<ResponseBody> response = apiServices.login(inputs);

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismissAllowingStateLoss();;
                //  Log.e(TAG,response.toString());
                try {
                    if (response != null && response.isSuccessful() ) {
                     //  Log.e(TAG, response.body().string());
                        JSONObject obj = new JSONObject(response.body().string());

                       // Log.d("My App", obj.toString());

                        if(obj != null){
                            if(obj.has("token")){
                                Log.e("token::" , obj.get("token").toString());

                                SharedPreferencesHelper.setToken(mContext,obj.get("token").toString());
                                SharedPreferencesHelper.setLastUserID(mContext,edtUserId.getText().toString());
                                SharedPreferencesHelper.setPass(mContext,edtPassword.getText().toString());
                                onLoginSuccess();
                            }
                        }


                    } else {
                        // parse the response body …
                        try {
                            APIError error = ErrorUtils.parseError(response);
                            if (error != null)
                                Log.e("Login error", "Code: " + response.code() + " Message: " + response.message());
                            //Utils.toast(mContext,error.message());
                            Utils.toast(mContext, "Login Failed!");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // there is more than just a failing request (like: no internet connection)
                progressDialog.dismissAllowingStateLoss();;
                t.printStackTrace();
                Utils.toast(mContext, "Login Failed!");
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialization() {

        edtUserId = (EditText) findViewById(R.id.edtUserId);
        edtPassword =  (EditText) findViewById(R.id.edtPassword);
        btnLogin =  (Button) findViewById(R.id.btnLogin);
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);
        versionInfo = (TextView) findViewById(R.id.versionInfo);
        SharedPreferencesHelper.setISLogin(mContext,"0");

        if(!SharedPreferencesHelper.getLastUserId(this).equals("")) {
            rememberMe.setChecked(true);
            edtUserId.setText(SharedPreferencesHelper.getLastUserId(this));
        }

        if(Utils.getAppVersion(LoginActivity.this) != null){
            versionInfo.setText(Utils.getAppVersion(LoginActivity.this)+" "+ "\u00a9"+ " JU BUS Tracking Apps. 2018");
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public void onLoginSuccess() {
        Log.e("TAG:","Called.");
        Intent intent = new Intent(mContext, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void saveLoginInfo(String loginId, String password) {
        SharedPreferencesHelper.setUser(mContext, loginId);
        SharedPreferencesHelper.setISLogin(mContext, "1");
        if(rememberMe.isChecked()){
            SharedPreferencesHelper.setLastUserID(mContext,SharedPreferencesHelper.getUser(mContext));
        }else {
            SharedPreferencesHelper.setLastUserID(mContext,"");
        }
    }

    public void onLoginFailed() {
        Utils.toast(getBaseContext(),getString(R.string.login_error));
    }

    public boolean validate() {
        boolean valid = true;

        String userId   = edtUserId.getText().toString();
        String password = edtPassword.getText().toString();
        int color = Color.WHITE;// whatever color you want
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(color);
        if (userId.isEmpty() ) {
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(getString(R.string.userid_validation_error));
            ssbuilder.setSpan(fgcspan, 0, getString(R.string.userid_validation_error).length(), 0);
            edtUserId.setError(ssbuilder);
            valid = false;
        } else {
            edtUserId.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 20) {
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(getString(R.string.password_min_max_length));
            ssbuilder.setSpan(fgcspan, 0, getString(R.string.password_min_max_length).length(), 0);
            edtPassword.setError(ssbuilder);
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }

}

