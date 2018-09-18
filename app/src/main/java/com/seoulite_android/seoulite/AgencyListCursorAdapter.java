package com.seoulite_android.seoulite;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static java.security.AccessController.getContext;



@SuppressWarnings("deprecation")
public class AgencyListCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    Drawable selectedImage;
    String selectedName;
//    가장 많이 사용되는 커서 어뎁터 사용..
    public AgencyListCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_agencylist,parent,false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
       TextView agency_num= view.findViewById(R.id.agencylist_number);
        TextView agency_name = view.findViewById(R.id.agencylist_name);
        //ImageView agency_language = view.findViewById(R.id.agencylist_language);
        ImageView engImage = view.findViewById(R.id.imageview_agencyinfo_eng);
        ImageView chImage = view.findViewById(R.id.imageview_agencyinfo_chinese);
        ImageView jpImage = view.findViewById(R.id.imageview_agencyinfo_japanese);
        ImageView portImage = view.findViewById(R.id.imageview_agencyinfo_portuguese);
        ImageView russianImage = view.findViewById(R.id.imageview_agencyinfo_russian);
        ImageView spainImage = view.findViewById(R.id.imageview_agencyinfo_spainish);
        TextView agencyId = view.findViewById(R.id.agency_id);


        int cnt = cursor.getPosition();
        final String AgencyName = cursor.getString(2);
        int EngExist = cursor.getInt(11);
        int ChExist = cursor.getInt(12);
        int JpExist = cursor.getInt(13);
        String EtcExist = cursor.getString(14);

        int AgencyId = cursor.getInt(0);

        Log.d("리스트 결과값 확인", AgencyId+AgencyName +","+ EngExist);

        agencyId.setText(String.valueOf(AgencyId));
        agency_num.setText(String.valueOf(cnt +1)+".");
        agency_name.setText(AgencyName);
//        String etc = agency.getLangEtc();
        if(EngExist != 1){
            engImage.setVisibility(View.GONE);
        }
        if(ChExist != 1){
            chImage.setVisibility(View.GONE);
        }
        if(JpExist != 1){
            jpImage.setVisibility(View.GONE);
        }
        if(EtcExist == "Russian"){
            russianImage.setVisibility(View.VISIBLE);
        } else if(EtcExist == "Portuguese"){
            portImage.setVisibility(View.VISIBLE);
        } else if(EtcExist == "Spanish"){
            spainImage.setVisibility(View.VISIBLE);
        }

    }
}
