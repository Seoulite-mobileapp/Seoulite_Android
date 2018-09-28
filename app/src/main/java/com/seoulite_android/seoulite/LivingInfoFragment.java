package com.seoulite_android.seoulite;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LivingInfoFragment extends Fragment {
    ViewGroup view;
    MainActivity activity;

    //Declaration for getLanguage
    private Locale systemLocale;
    private String currentLangauge;

    DbHelper dbHelper;
    DistrictVO district;

    String distName;

    /*ImageView mapPopupImage;*/
    @BindView(R.id.living_info_map_image) ImageView living_info_map_image;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_living_info, container, false);
        //getLanguage
        systemLocale = activity.getResources().getConfiguration().locale;
        currentLangauge = systemLocale.getLanguage();

        ButterKnife.bind(this, view);
        //getting agency id from previous fragment.
        if(savedInstanceState ==null) {
            gettingDistName();
        }

        getDistrictInfo(distName); // db에서 전 화면에서 받아온 id를 이용해 sql select
        setTextViews(currentLangauge); //textview들을 setting

        changeImage(distName);

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
            distName = bundle.getString("districtName");
        }else{
            Toast.makeText(activity, "Can't get data. Try again.", Toast.LENGTH_LONG).show();
        }
    }

    private void getDistrictInfo(String distName){// db에서 전 화면에서 받아온 id를 이용해 select 후 agencyVO에 저장
        dbHelper = new DbHelper(activity);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlDistrict = "SELECT * FROM DISTRICTS where dist_en = '"+ distName+"'";
        Cursor cursor = db.rawQuery(sqlDistrict, null);
        while (cursor.moveToNext()) {
            district = new DistrictVO(cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4), cursor.getFloat(5),
                    cursor.getInt(6), cursor.getInt(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10), cursor.getString(11));
        } //cursor.getString(0) : return id
        cursor.close();
        db.close();
    }

    private void setTextViews(String currentLangauge){
        TextView textviewDistrictName = view.findViewById(R.id.text_living_info_district_name);
        TextView textviewNearby = view.findViewById(R.id.text_living_info_nearby);
        TextView textviewFeatures = view.findViewById(R.id.text_living_info_features);

        if(currentLangauge.equals("en")){
            textviewDistrictName.setText(district.getDistrictEn());
            textviewNearby.setText(district.getDistNearEn());
            textviewFeatures.setText(district.getFeatsEn());
        }else if(currentLangauge.equals("ko")){
            textviewDistrictName.setText(district.getDistrictKr());
            textviewNearby.setText(district.getDistNearKr());
            textviewFeatures.setText(district.getFeatsKr());
        }

        TextView textviewPopulation = view.findViewById(R.id.text_living_info_population);
        textviewPopulation.setText(String.valueOf(district.getForeignPop()));
        TextView textviewForeignerRatio = view.findViewById(R.id.text_living_info_foreigner_ratio);
        textviewForeignerRatio.setText(String.valueOf(district.getForeignRate()));
        TextView textviewAverageFee = view.findViewById(R.id.text_living_info_average_fee);
        textviewAverageFee.setText(String.valueOf(district.getAvgRent()));
        TextView textviewRank = view.findViewById(R.id.text_living_info_rank);
        textviewRank.setText(String.valueOf(district.getRentRank()));


    }

    private void changeImage (String distName) {
        switch (distName) {
            case "Gangnam-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.gangnamgu));
                break;
            case "Gangdong-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.gangdonggu));
                break;
            case "Gangbuk-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.gangbukgu));
                break;
            case "Gangseo-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.gangseogu));
                break;
            case "Gwangjin-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.gwangjingu));
                break;
            case "Guro-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.gurogu));
                break;
            case "Geumcheon-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.geumcheongu));
                break;
            case "Nowon-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.nowongu));
                break;
            case "Dobong-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.dobonggu));
                break;
            case "Dongdaemun-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.dongdaemungu));
                break;
            case "Dongjak-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.dongjakgu));
                break;
            case "Seodaemun-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.seodaemungu));
                break;
            case "Seocho-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.seochogu));
                break;
            case "Sungdong-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.seongdonggu));
                break;
            case "Sungbuk-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.seongbukgu));
                break;
            case "Songpa-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.songpagu));
                break;
            case "Yangcheon-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.yangcheongu));
                break;
            case "Yeongdeungpo-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.yeongdeungpogu));
                break;
            case "Yongsan-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.yongsangu));
                break;
            case "Eunpyeong-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.eunpyeonggu));
                break;
            case "Jongno-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.jongnogu));
                break;
            case "Jung-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.junggu));
                break;
            case "Jungnang-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.jungnanggu));
                break;
            case "Gwanak-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.gwanakgu));
                break;
            case "Mapo-gu":
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.mapogu));
                break;
            default:
                living_info_map_image.setImageDrawable(getResources().getDrawable(R.drawable.agencyinfo_fax));
                break;
        }
    }

}
