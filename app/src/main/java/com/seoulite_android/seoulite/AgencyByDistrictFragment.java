package com.seoulite_android.seoulite;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AgencyByDistrictFragment extends Fragment {

    private static final String TAG = "AgencyByDistrictFrag";

    @BindView(R.id.text_agencylist_district_name) TextView mDistrictNameTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agency_by_district, container, false);

        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView: ");

        Bundle args = getArguments();
        String districtName = args.getString("districtName");
        setDistrirctName(districtName);

        return view;
    }

    public void setDistrirctName(String districtName) {
        if (districtName == null) {
            Log.d(TAG, "setDistrirctName: districtName is null.");
            return;
        }
        mDistrictNameTextView.setText(districtName);
    }


}
