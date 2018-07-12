package com.hossain.ju.bus.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.hossain.ju.bus.R;
import com.hossain.ju.bus.progressdialog.CubeGrid;


/**
 * Created by mortuza on 3/1/2018.
 */

public class CustomProgressDialog extends android.app.DialogFragment {
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_fragment_layout, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.aa);
        CubeGrid cubeGrid = new CubeGrid();
        cubeGrid.setColor(ContextCompat.getColor(view.getContext(), R.color.progressbarColor));
       // cubeGrid.setColor(getResources().getColor(R.color.progressbarColor,null));
        progressBar.setIndeterminateDrawable(cubeGrid);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

}
