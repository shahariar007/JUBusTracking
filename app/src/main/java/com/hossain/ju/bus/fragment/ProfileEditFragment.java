package com.hossain.ju.bus.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hossain.ju.bus.R;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.model.user.User;
import com.hossain.ju.bus.networking.APIClient;
import com.hossain.ju.bus.networking.APIServices;
import com.hossain.ju.bus.networking.ResponseWrapperObject;
import com.hossain.ju.bus.utils.CustomProgressDialog;
import com.hossain.ju.bus.utils.Utils;
import com.hossain.ju.bus.views.UI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hossain.ju.bus.utils.Utils.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context mContext;
    APIServices apiServices;


    public ProfileEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = getActivity();
        apiServices = APIClient.getInstance().create(APIServices.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserList();
    }

    private void setUserList() {
        final CustomProgressDialog progressDialog = UI.show(mContext);
        String token = Utils.BEARER + SharedPreferencesHelper.getToken(mContext);
        Log.e("Token::", token);
        Call<ResponseWrapperObject<User>> response = apiServices.getUserInfo(token);

        response.enqueue(new Callback<ResponseWrapperObject<User>>() {
            @Override
            public void onResponse(Call<ResponseWrapperObject<User>> call, Response<ResponseWrapperObject<User>> response) {
                progressDialog.dismissAllowingStateLoss();
                Log.e(TAG, response.toString());

            }


            @Override
            public void onFailure(Call<ResponseWrapperObject<User>> call, Throwable t) {
                // there is more than just a failing request (like: no internet connection)
                progressDialog.dismissAllowingStateLoss();
                Log.e(TAG, t.getMessage() + "");
                Utils.toast(mContext, "data Failed!");
            }
        });

    }

    public void setProfileData(User profileData) {

    }

    public void init(View view) {

    }
}
