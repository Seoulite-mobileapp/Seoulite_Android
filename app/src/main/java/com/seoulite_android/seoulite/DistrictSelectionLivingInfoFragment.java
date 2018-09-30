package com.seoulite_android.seoulite;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DistrictSelectionLivingInfoFragment extends Fragment {
    @BindView(R.id.list_district_living_info) ListView mDistrictListView;
    @BindView(R.id.btn_district_selection_search_living_info) Button mSearchButton;

    private int mSelectedDistrictPos = -1;
    private String mSelectedDistrictName;

    //private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_district_selection_living_info, container, false);

        ButterKnife.bind(this, view);
        DistrictListAdapter adapter = new DistrictListAdapter(getContext(), DistrictEntry.getDistrictList());
        mDistrictListView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.btn_district_selection_search_living_info)
    public void moveToLivingInfoByDistrictFragment() {
        if (mSelectedDistrictName == null) {
            Toast.makeText(getContext(), "Please select a district !", Toast.LENGTH_SHORT).show();
            return;
        }

        LivingInfoFragment livingInfoFragment = new LivingInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("districtName", mSelectedDistrictName);
        livingInfoFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container, livingInfoFragment)
                .addToBackStack(null).commit();

        //mListener.onFragmentInteraction(mSelectedDistrictName);
    }

    // Containing Activity must implement this interface
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String districtName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }




    class DistrictListAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mDistrictList;

        // State of the item that needs to show separator
        private static final int SECTIONED_STATE = 1;

        // State of the item that needs not show separator
        private static final int REGULAR_STATE = 2;

        // Cache item states based on positions
        private int[] mItemsStates;



        DistrictListAdapter(Context context, List<String> list) {
            mContext = context;
            mDistrictList = list;
            mItemsStates = new int[getCount()];
        }

        @Override
        public int getCount() {
            return mDistrictList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            boolean showSeparator = false;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.district_list_item_layout, null);
            } else {
                view = convertView;
            }

            // Set text
            final TextView districtNameTextView = view.findViewById(R.id.district_name);
            String districtName = mDistrictList.get(position);
            districtNameTextView.setText(districtName);
            // Set Selector color
            if (mSelectedDistrictPos == position) {
                districtNameTextView.setBackgroundColor(Color.parseColor("#FFCF44"));
            } else {
                districtNameTextView.setBackgroundColor(Color.parseColor("#e0e0e0"));
            }

            // Show separator when needed
            switch(mItemsStates[position]) {
                case SECTIONED_STATE:
                    showSeparator = true;
                    break;
                case REGULAR_STATE:
                    showSeparator = false;
                    break;
                default:
                    if (position == 0) showSeparator = true;
                    else {
                        String prevName = mDistrictList.get(position - 1);
                        char[] prevNameCharArr = prevName.toCharArray();
                        char[] curNameCharArr = districtName.toCharArray();

                        if (curNameCharArr[0] != prevNameCharArr[0]) showSeparator = true;
                    }

                    // Caching
                    mItemsStates[position] = showSeparator ? SECTIONED_STATE : REGULAR_STATE;
                    break;
            }

            TextView separatorView = view.findViewById(R.id.district_list_separator);
            if (showSeparator) {
                separatorView.setText(districtName.toCharArray(), 0, 1);
                separatorView.setVisibility(View.VISIBLE);
            } else {
                separatorView.setVisibility(View.GONE);
            }

            districtNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedDistrictName = districtNameTextView.getText().toString();
                    mSelectedDistrictPos = position;
                    notifyDataSetChanged();
                    Toast.makeText(mContext, mSelectedDistrictName + " clicked.", Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }


    }
}
