package com.seoulite_android.seoulite;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link FavDistrictFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// */
public class FavDistrictFragment extends Fragment {
//
//    private OnFragmentInteractionListener mListener;

    @BindView(R.id.btn_fav_find_district) Button mFindDistrictButton;
    private ArrayList<FavVO> mMockList = new ArrayList<>();

    public FavDistrictFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_district, container, false);
        ButterKnife.bind(this, view);

        // TODO: This is mock data
        mMockList.add(new FavVO(1, "Nowon-gu", 1, 0, null));
        mMockList.add(new FavVO(2, "Gangnam-gu", 1, 0, null));
        mMockList.add(new FavVO(3, "Shit-gu", 1, 0, "Pretty fucking shit !"));

        // Set up the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.fav_district_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        FavCardRecyclerViewAdapter adapter = new FavCardRecyclerViewAdapter(mMockList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    // TODO: Make this Button Work. This is temporary !
    @OnClick(R.id.btn_fav_find_district)
    public void moveToDistrictSelection() {
        Toast.makeText(getContext(), "Btn Clicked", Toast.LENGTH_SHORT).show();
        ((MainActivity)getActivity()).replaceFragment(new DistrictSelectionFragment(), true);
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
