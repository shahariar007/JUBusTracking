package com.hossain.ju.bus.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hossain.ju.bus.R;
import com.hossain.ju.bus.helper.SharedPreferencesHelper;
import com.hossain.ju.bus.networking.APIClient;
import com.hossain.ju.bus.networking.APIServices;
import com.hossain.ju.bus.utils.APIError;
import com.hossain.ju.bus.utils.CircleTransform;
import com.hossain.ju.bus.utils.CustomProgressDialog;
import com.hossain.ju.bus.utils.ErrorUtils;
import com.hossain.ju.bus.utils.Utils;
import com.hossain.ju.bus.views.UI;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePictureChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePictureChangeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView imageButton, profilePicture;
    private APIServices apiServices;


    public ProfilePictureChangeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilePictureChangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilePictureChangeFragment newInstance(String param1, String param2) {
        ProfilePictureChangeFragment fragment = new ProfilePictureChangeFragment();
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
        apiServices = APIClient.getInstance().create(APIServices.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_picture_change, container, false);
        imageButton = (ImageView) v.findViewById(R.id.imageButton);
        profilePicture = (ImageView) v.findViewById(R.id.imageProfile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), REQUEST_CODE);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri selectedImage = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            Picasso.get().load(selectedImage).transform(new CircleTransform()).into(profilePicture);
                            setProfilePicture(imageToString(bitmap));
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e("ERROR", "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("ERROR", "Exception in onActivityResult : " + e.getMessage());
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] b = stream.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    private void setProfilePicture(String data) {

        final CustomProgressDialog progressDialog = UI.show(getActivity());
        String token = Utils.BEARER + SharedPreferencesHelper.getToken(getActivity());
        // Log.e("Token::",token);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("userId", "");
        stringStringHashMap.put("imageData", data);

        Call<ResponseBody> response = apiServices.imageUpload(token, stringStringHashMap);

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismissAllowingStateLoss();
                try {
                    if (response != null && response.isSuccessful()) {

                    } else {
                        // parse the response body …
                        try {
                            APIError error = ErrorUtils.parseError(response);
                            if (error != null)
                                Utils.toast(getActivity(), error.message());
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
                progressDialog.dismissAllowingStateLoss();

                Utils.toast(getActivity(), "data Failed!");
            }
        });

    }

}
