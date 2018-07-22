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
import com.hossain.ju.bus.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
                            imageToString(bitmap);
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
}
