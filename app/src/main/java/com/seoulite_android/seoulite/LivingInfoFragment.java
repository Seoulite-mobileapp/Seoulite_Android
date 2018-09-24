package com.seoulite_android.seoulite;

import android.app.Dialog;
import android.content.Context;
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

import butterknife.BindView;

public class LivingInfoFragment extends Fragment {
    MainActivity activity;
    String distName;
/*
    ImageView mapPopupImage;
*/
    @BindView(R.id.living_info_map_image) ImageView living_info_map_image;


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

            /*changeImage(distName);*/

            /*living_info_map_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUp(distName);

                }
            }*/

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

    /*private void changeImage(String distName) {
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
    }*/

    /*public void showPopUp(String distName){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_map_image);
        mapPopupImage = dialog.findViewById(R.id.map_popup_image);
        switch (distName) {
            case "Gangnam-gu":
                mapPopupImage.setImageDrawable(getResources().getDrawable(R.drawable.gangnamgu));
                dialog.show();
                break;
            default:
                mapPopupImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_camera));
                dialog.show();
                break;
        }
    }*/
}
