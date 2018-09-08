package com.seoulite_android.seoulite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavAgencyFragment extends Fragment {

    private ArrayList<FavVO> mMockList = new ArrayList<>();

    public FavAgencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_district, container, false);

        // Push mock data
        mMockList.add(new FavVO(1, "Fucking Good Agency", 0, 1, null));
        mMockList.add(new FavVO(2, "Certified Real Estate Agency", 0, 1, null));
        mMockList.add(new FavVO(3, "Shit shat shut Agency", 0, 1, "Pretty fucking shit !"));

        // Set up the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.fav_district_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        FavCardRecyclerViewAdapter adapter = new FavCardRecyclerViewAdapter(mMockList);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
