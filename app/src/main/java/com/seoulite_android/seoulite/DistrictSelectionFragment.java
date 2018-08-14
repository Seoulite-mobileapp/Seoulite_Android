package com.seoulite_android.seoulite;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DistrictSelectionFragment extends Fragment {
    @BindView(R.id.list_district) ListView mDistrictListView;
    @BindView(R.id.btn_district_selection_search) Button mSearchButton;

    private int mSelectedDistrictPos;
    private String mSelectedDistrictName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_district_selection, container, false);

        ButterKnife.bind(this, view);
        final DistrictListAdapter adapter = new DistrictListAdapter(getContext(), DistrictEntry.getDistrictList());
        mDistrictListView.setAdapter(adapter);
        mDistrictListView.setSelector(R.drawable.list_selector);

        mDistrictListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Background Color Change ... WTF selector...
                if (mSelectedDistrictPos != position) {
                    TextView prevTextView = (adapter.getView(mSelectedDistrictPos, null, parent)).findViewById(R.id.district_name);
                    //prevTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    prevTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                    ((TextView)view.findViewById(R.id.district_name)).setBackgroundColor(getResources().getColor(R.color.toolbarIconColor));
                    mSelectedDistrictPos = position;
                    mSelectedDistrictName = (String)parent.getItemAtPosition(position);
                }

            }
        });

        return view;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            boolean showSeparator = false;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.district_list_item_layout, null);
            } else {
                view = convertView;
            }

            // Set text
            TextView districtNameTextView = view.findViewById(R.id.district_name);
            String districtName = mDistrictList.get(position);
            districtNameTextView.setText(districtName);

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

            return view;
        }

    }
}
