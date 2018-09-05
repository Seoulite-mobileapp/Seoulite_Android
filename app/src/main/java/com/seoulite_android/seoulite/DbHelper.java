package com.seoulite_android.seoulite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;

public class DbHelper extends SQLiteOpenHelper{

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE AGENCIES (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, agnc_nm_kr TEXT, agnc_nm_en TEXT, " +
                "own_kr TEXT, own_en TEXT, phone TEXT, fax TEXT, adr_gu_kr TEXT, adr_dt_kr TEXT," +
                "adr_gu_en TEXT, adr_dt_en TEXT, lang_en NUMBER, lang_cn NUMBER, lang_jp NUMBER, " +
                "lang_etc TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String agnc_nm_kr, String agnc_nm_en, String own_kr, String own_en, String phone,
                       String fax, String adr_gu_kr, String adr_dt_kr, String adr_gu_en, String adr_dt_en,
                       int lang_en, int lang_cn, int lang_jp, String lang_etc){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO AGENCIES values(" +
                "null," + agnc_nm_kr + "," + agnc_nm_en + "," + own_kr + "," + own_en + "," +
                phone + "," + fax + "," + adr_gu_kr + "," + adr_dt_kr + "," + adr_gu_en + "," +
                adr_dt_en + "," + lang_en +"," + lang_cn + "," +lang_jp + "," + lang_etc + ");"
        );
    }

    public Collection<AgencyVO> getResults(){
        SQLiteDatabase db = getReadableDatabase();
        Collection list = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM AGENCIES", null);
        AgencyVO agency;
        while (cursor.moveToNext()) {
            agency = new AgencyVO(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getInt(10),
                    cursor.getInt(11), cursor.getInt(12), cursor.getString(13));
            list.add(agency);
        }
        return list;
    }

}
