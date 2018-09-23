package com.seoulite_android.seoulite;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AgencyByDistrictFragment extends Fragment {
    MainActivity activity;
    private static final String TAG = "AgencyByDistrictFrag";


    @BindView(R.id.text_agencylist_district_name)
    TextView mDistrictNameTextView;
    @BindView(R.id.list_agency_by_district)
    ListView mAgencyListView;
    @BindView(R.id.btn_living_info)
    Button btn_living_info;
    @BindView(R.id.btn_language_filter)
    Button btn_language_filter;
    @BindView(R.id.map_image)
    ImageView map_image;
    LinearLayout mlayout;
    Bundle bundle;
    List<AgencyEntry> mAgencyList;
    String districtName;


    DbHelper mDbHelper;
    SQLiteDatabase mDb;
   // private GestureDetector gestureDetector;
    ImageView mapPopupImage;
    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        mDbHelper = new DbHelper(getActivity()); //context 보통 mainActivity를 넣음.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agency_by_district, container, false);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView: ");
        Button btnSelectLan = view.findViewById(R.id.btn_select_language);
        final LinearLayout layout = (LinearLayout)view.findViewById(R.id.language_filter_page);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            districtName = args.getString("districtName");
            if (districtName == null) {
                Log.d(TAG, "setDistrirctName: districtName is null.");
            }
            mDistrictNameTextView.setText(districtName);
            final String selected = mDistrictNameTextView.getText().toString();


            changeImage(districtName);


            mDb = mDbHelper.getReadableDatabase();
            Cursor cursor;
            cursor = mDb.rawQuery("SELECT * FROM AGENCIES WHERE adr_gu_en='" + selected + "'", null);
            final AgencyListCursorAdapter mcursorAdapter = new AgencyListCursorAdapter(getActivity(), cursor);
            mcursorAdapter.notifyDataSetChanged();
            mAgencyListView.setAdapter(mcursorAdapter);


            btn_living_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LivingInfoFragment fragment = new LivingInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("disName", selected);
                    fragment.setArguments(bundle);

                    //getFragmentManager().beginTransaction().add(R.id.main_container, fragment).commit();
                    ((MainActivity)getActivity()).replaceFragment(fragment,false);

                }
            });

            map_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUp(districtName);

                }
            });

            mAgencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView id = view.findViewById(R.id.agency_id);
                    String selectedAgcId = id.getText().toString();

                    Log.d("선택된 에이전시 이름", selectedAgcId);

                    AgencyInfoFragment agencyInfoFragment = new AgencyInfoFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("agcId", selectedAgcId);
                    agencyInfoFragment.setArguments(bundle2);

                    getFragmentManager().beginTransaction().replace(R.id.main_container, agencyInfoFragment)
                            .addToBackStack(null)
                            .commit();
                    //((MainActivity)getActivity()).replaceFragment(new AgencyInfoFragment(), false);

                }
            });

            btn_language_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layout.setVisibility(View.VISIBLE);
                    layout.bringToFront();
                }
            });

        }
        return view;
    }


    //구에 따라 이미지 다르게 보여줌.
    private void changeImage(String districtName) {
        switch (districtName) {
            case "Gangnam-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.gangnamgu));
                break;
            case "Gangdong-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.gangdonggu));
                break;
            case "Gangbuk-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.gangbukgu));
                break;
            case "Gangseo-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.gangseogu));
                break;
            case "Gwangjin-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.gwangjingu));
                break;
            case "Guro-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.gurogu));
                break;
            case "Geumcheon-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.geumcheongu));
                break;
            case "Nowon-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.nowongu));
                break;
            case "Dobong-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.dobonggu));
                break;
            case "Dongdaemun-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.dongdaemungu));
                break;
            case "Dongjak-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.dongjakgu));
                break;
            case "Seodaemun-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.seodaemungu));
                break;
            case "Seocho-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.seochogu));
                break;
            case "Sungdong-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.seongdonggu));
                break;
            case "Sungbuk-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.seongbukgu));
                break;
            case "Songpa-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.songpagu));
                break;
            case "Yangcheon-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.yangcheongu));
                break;
            case "Yeongdeungpo-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.yeongdeungpogu));
                break;
            case "Yongsan-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.yongsangu));
                break;
            case "Eunpyeong-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.eunpyeonggu));
                break;
            case "Jongno-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.jongnogu));
                break;
            case "Jung-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.junggu));
                break;
            case "Jungnang-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.jungnanggu));
                break;
            case "Gwanak-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.gwanakgu));
                break;
            case "Mapo-gu":
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.mapogu));
                break;
            default:
                map_image.setImageDrawable(getResources().getDrawable(R.drawable.agencyinfo_fax));
                break;
        }
    }

    //구별...로 다시 만들자ㅏㅏㅏㅏㅏ
    public void showPopUp(String districtName){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_map_image);
        mapPopupImage = dialog.findViewById(R.id.map_popup_image);
        switch (districtName) {
            case "Gangnam-gu":
                mapPopupImage.setImageDrawable(getResources().getDrawable(R.drawable.gangnamgu));
                dialog.show();
                break;
            default:
                mapPopupImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_camera));
                dialog.show();
                break;
        }
    }



}
