package com.seoulite_android.seoulite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;

public class LivingInfoFragment extends Fragment {
    MainActivity activity;
    String distName;
    //@BindView(R.id.text_living_info_district_name) TextView mLivingInfoDistrictName;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_living_info, container, false);

        //getting agency id from previous fragment.
        if(savedInstanceState ==null) {
            gettingDistName();
        }
        TextView mLivingInfoDistrictName = (TextView) view.findViewById(R.id.text_living_info_district_name);
        mLivingInfoDistrictName.setText(distName);


        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity=null;
    }

    private void gettingDistName(){
        Bundle bundle = getArguments();
        if(bundle != null){
            distName = bundle.getString("distName");
        }else{
            Toast.makeText(activity, "Can't get data. Try again.", Toast.LENGTH_LONG).show();
        }
    }
}
