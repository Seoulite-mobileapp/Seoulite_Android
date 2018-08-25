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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AgencyByDistrictFragment extends Fragment {

    private static final String TAG = "AgencyByDistrictFrag";

    @BindView(R.id.text_agencylist_district_name) TextView mDistrictNameTextView;
    @BindView(R.id.list_agency_by_district) ListView mAgencyListView;

    List<AgencyEntry> mAgencyList;

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

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            String districtName = args.getString("districtName");
            setDistrirctName(districtName);
            initAgencyList();

            AgencyListAdapter adapter = new AgencyListAdapter(mAgencyList);
            mAgencyListView.setAdapter(adapter);

        }



        return view;
    }

    public void setDistrirctName(String districtName) {
        if (districtName == null) {
            Log.d(TAG, "setDistrirctName: districtName is null.");
            return;
        }
        mDistrictNameTextView.setText(districtName);
    }

    private void initAgencyList() {
        mAgencyList = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJson());
            JSONArray arr = obj.getJSONArray("district_list_en");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject entry = arr.getJSONObject(i);
                if (mDistrictNameTextView.getText().toString().equals(entry.getString("address_district"))) {
                    String name = entry.getString("name");
                    String representative = entry.getString("representative");
                    String tel = entry.getString("tel");
                    String fax = entry.getString("fax");
                    String fullAddress = entry.getString("address_full");
                    String district = entry.getString("address_district");
                    String language = entry.getString("language");

                    AgencyEntry agency = new AgencyEntry(name, representative, tel, fax, fullAddress,
                            district, language);
                    mAgencyList.add(agency);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJson() {
        String jsonStr = null;
        try {
            InputStream is = getContext().getAssets().open("district_list_en.json");
            if (is != null) {
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(reader);
                String line = "";
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                jsonStr = sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "loadJson: " + "\n" + jsonStr);
        return jsonStr;
    }

}
