package com.seoulite_android.seoulite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AgencyInfoFragment extends Fragment implements OnMarkerClickListener, OnMapReadyCallback {

    MainActivity activity;
    private GoogleMap mMap;
    private Marker mAgency;
    //TODO: 부동산 주소에 맞게 바꾸기 ( geocoding? )
    private LatLng AGENCY = new LatLng(37, 128);
    //todo: favorite check
    private boolean favorite_check = true;

    @BindView(R.id.agencyinfo_favorite_star) ImageView favorite_star;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_agency_info, container, false);
        ButterKnife.bind(this, rootView);

        matching_favorite_star(favorite_check);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.agencyinfo_map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @OnClick(R.id.agencyinfo_favorite_layout)
    void favoriteInteraction(){
        if(favorite_check){ // 삭제
            favorite_check = false;
            //todo: delete data from favorite table
            matching_favorite_star(favorite_check);
        } else { // 추가
            favorite_check = true;
            //todo: insert data from favorite table
            matching_favorite_star(favorite_check);
            showAddFavoriteAlert();
        }
    }

    private void matching_favorite_star(boolean check){
        if(check) favorite_star.setImageResource(R.drawable.agencyinfo_star_on);
        else favorite_star.setImageResource(R.drawable.agencyinfo_star_off);
    }

    private void showAddFavoriteAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Notification");
        builder.setMessage("Agency is successfully added to your favorites!"+
            ""+
            "Would you like to check your favorite?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                (activity).replaceFragment(new FavAgencyFragment(), false);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        addMarkerToMap();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(AGENCY));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.setOnMarkerClickListener(this);
    }

    private void addMarkerToMap() {
        mAgency = mMap.addMarker(new MarkerOptions()
                .position(AGENCY)
                .title("Eden Agency")
                .snippet("Eng, Jap"));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(marker.equals(mAgency)){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+ AGENCY ));
            startActivity(intent);
        }
        return false;
    }

}


    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AgencyInfoFragment() {
        // Required empty public constructor
    }
*/
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgencyInfoFragment.
     */
     /*
    // TODO: Rename and change types and number of parameters
    public static AgencyInfoFragment newInstance(String param1, String param2) {
        AgencyInfoFragment fragment = new AgencyInfoFragment();
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
        return inflater.inflate(R.layout.fragment_agency_info, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

