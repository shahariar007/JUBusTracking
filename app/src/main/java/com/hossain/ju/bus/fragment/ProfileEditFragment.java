package com.hossain.ju.bus.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hossain.ju.bus.R;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.model.user.User;
import com.hossain.ju.bus.networking.APIClient;
import com.hossain.ju.bus.networking.APIServices;
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
    TextView userName, userEmail, userPhone, userDepartment, userAddress, userHall, userEmergencyContact;
    ImageView imageProfile;

    LinearLayout userShowLayout, userEditLayout;
    private Animation animationFadeOut, animationFadeIn;
    Button saveButton, cancelButton;
    Menu menu;
    boolean callStatus = true;

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
        setHasOptionsMenu(true);
        animationFadeOut = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        animationFadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        init(view);
        apiServices = APIClient.getInstance().create(APIServices.class);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserList();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userShowLayout.setVisibility(View.VISIBLE);
                userShowLayout.startAnimation(animationFadeIn);

                userEditLayout.startAnimation(animationFadeOut);
                userEditLayout.setVisibility(View.GONE);
                menu.getItem(0).setIcon(R.drawable.ic_edit_black_24dp);
            }
        });
    }

    private void setUserList() {
        final CustomProgressDialog progressDialog = UI.show(mContext);
        String token = Utils.BEARER + SharedPreferencesHelper.getToken(mContext);
        Log.e("Token::", token);
        Call<User> response = apiServices.getUserInfo(token);

        response.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismissAllowingStateLoss();
                Log.e(TAG, response.toString());
                Log.e("FFFF:FF", response.body().getName());
                try {
                    setProfileData(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // there is more than just a failing request (like: no internet connection)
                progressDialog.dismissAllowingStateLoss();
                Log.e(TAG, t.getMessage() + "");
                Utils.toast(mContext, "data Failed!");
            }
        });

    }

    public void setProfileData(User profileData) throws Exception {
        userName.setText(profileData.getName());
        userEmail.setText(profileData.getEmail());
        userPhone.setText(profileData.getUserInfo().getPhone());
        userDepartment.setText(profileData.getUserInfo().getDepartment().getName());
        userAddress.setText(profileData.getUserInfo().getAddress());
        userHall.setText(profileData.getUserInfo().getEmergencyContact());
        userEmergencyContact.setText(profileData.getName());
        // imageProfile
    }

    public void init(View view) {

        userName = (TextView) view.findViewById(R.id.userName);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        userPhone = (TextView) view.findViewById(R.id.userPhone);
        userDepartment = (TextView) view.findViewById(R.id.userDepartment);
        userAddress = (TextView) view.findViewById(R.id.userAddress);
        userHall = (TextView) view.findViewById(R.id.userHall);
        userEmergencyContact = (TextView) view.findViewById(R.id.userEmergencyContact);
        imageProfile = (ImageView) view.findViewById(R.id.imageProfile);
        userShowLayout = (LinearLayout) view.findViewById(R.id.userShowLayout);
        userEditLayout = (LinearLayout) view.findViewById(R.id.userEditLayout);
        saveButton = (Button) view.findViewById(R.id.save);
        cancelButton = (Button) view.findViewById(R.id.cancel);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menu, menu);
        this.menu = menu;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.edit_profile_menu: {
                if (callStatus) {
                    callStatus = false;
                    userShowLayout.startAnimation(animationFadeOut);
                    userShowLayout.setVisibility(View.GONE);
                    userEditLayout.setVisibility(View.VISIBLE);
                    userEditLayout.startAnimation(animationFadeIn);
                    menu.getItem(0).setIcon(R.drawable.ic_clear_black_24dp);
                } else {
                    callStatus = true;
                    userShowLayout.setVisibility(View.VISIBLE);
                    userShowLayout.startAnimation(animationFadeIn);

                    userEditLayout.startAnimation(animationFadeOut);
                    userEditLayout.setVisibility(View.GONE);
                    menu.getItem(0).setIcon(R.drawable.ic_edit_black_24dp);
                }

            }
        }
        return false;
    }
}
