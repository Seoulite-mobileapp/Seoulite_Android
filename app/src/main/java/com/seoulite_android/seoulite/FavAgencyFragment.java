package com.seoulite_android.seoulite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavAgencyFragment extends Fragment {

    @BindView(R.id.btn_fav_find_agency) Button mFindAgencyButton;

    private ArrayList<FavVO> mMockList = new ArrayList<>();

    public FavAgencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_agency, container, false);
        ButterKnife.bind(this, view);

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

    // TODO: Make this Btn Work. This is temporary !
    @OnClick(R.id.btn_fav_find_agency)
    public void moveToAgencySelection() {
        Toast.makeText(getContext(), "Btn Clicked", Toast.LENGTH_SHORT).show();
    }
}
