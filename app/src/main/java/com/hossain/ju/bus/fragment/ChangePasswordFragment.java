package com.hossain.ju.bus.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hossain.ju.bus.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText oldPassword, newPassword, repeatPassword;
    Button cancelButton, changeButton;
    private boolean A = false;
    private boolean B = false;
    private static final String TAG = ChangePasswordFragment.class.getSimpleName();

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        oldPassword = (EditText) v.findViewById(R.id.oldPassword);
        newPassword = (EditText) v.findViewById(R.id.newPassword);
        repeatPassword = (EditText) v.findViewById(R.id.repeatPassword);
        changeButton = (Button) v.findViewById(R.id.change_password);
        cancelButton = (Button) v.findViewById(R.id.cancel);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (repeatPassword.getText() == null || repeatPassword.getText().toString().trim().isEmpty()) {
                    Log.d(TAG, "afterTextChanged:FAILED");
                    B = false;
                    A = true;
                    return;
                } else {
                    String repeatPass = repeatPassword.getText().toString().trim();
                    String newPass = newPassword.getText().toString().trim();
                    A = (repeatPass.contains(s));
                    B = (newPass.contains(repeatPass));
                }
                Log.d(TAG, "afterTextChanged: " + s + "A==" + A + "B==" + B);
            }
        });
        repeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (newPassword.getText() == null || newPassword.getText().toString().trim().isEmpty()) {
                    A = false;
                    B = true;
                    Log.d(TAG, "afterTextChanged:REP FAILED");
                    return;
                } else {
                    String newPass = newPassword.getText().toString().trim();
                    String repeatPass = repeatPassword.getText().toString().trim();
                    A = (repeatPass.contains(s));
                    B = (newPass.contains(repeatPass));
                }
                Log.d(TAG, "afterTextChanged:repeat " + s + "A==" + A + "B==" + B);

            }
        });
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (A && B) {
                    Toast.makeText(getActivity(), "True", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "A==" + A + "B==" + B, Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
