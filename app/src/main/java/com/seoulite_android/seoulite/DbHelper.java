package com.seoulite_android.seoulite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*import java.util.ArrayList;
import java.util.Collection;*/

public class DbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "Agencies.db";
    private static final int DB_VERSION = 1;
    private static final int agenciesRecordNo = 261;
    private static final int districtsRecordNo = 100;//TODO: 실제 레코드 수 나오면 맞춰야함

    private static final String SQL_CREATE_AGENCIES = "CREATE TABLE AGENCIES (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, agnc_nm_kr TEXT, agnc_nm_en TEXT, " +
            "own_kr TEXT, own_en TEXT, phone TEXT, fax TEXT, adr_gu_kr TEXT, adr_dt_kr TEXT," +
            "adr_gu_en TEXT, adr_dt_en TEXT, lang_en NUMBER, lang_cn NUMBER, lang_jp NUMBER, " +
            "lang_etc TEXT)";

    private static final String SQL_CREATE_FAVORITES = "CREATE TABLE FAVORITES (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, is_district INTEGER, is_agency INTEGER, memo TEXT)";
    //todo: foreign key??
        /*
            // Set up the location column as a foreign key to location table.
            // https://gerardnico.com/android/sqlite
            "FOREIGN KEY ('is_agency') REFERENCES 'AGENCIES' ('_id')"
        */

    private static final String SQL_CREATE_DISTRICTS = "CREATE TABLE DISTRICTS (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, dist TEXT, dist_pop INTEGER, fn_pop INTEGER, fn_rat INTEGER, avg_rent INTEGER, rank INTEGER, dist_near TEXT, dist_near_en TEXT, feats TEXT, feats_en TEXT)";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_AGENCIES);
        db.execSQL(SQL_CREATE_FAVORITES);
        db.execSQL(SQL_CREATE_DISTRICTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int DB_VERSION, int newVersion) {
        if (newVersion == DB_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS AGENCIES");
            db.execSQL("DROP TABLE IF EXISTS FAVORITES");
            db.execSQL("DROP TABLE IF EXISTS DISTRICTS");
            onCreate(db);
        }
    }

    public boolean isTableReadyForInsert(String table) {
        int actualRecords = countRecords(table);
        int correctRecords = 0;

        if (table.equals("AGENCIES")) correctRecords = agenciesRecordNo;
        else if (table.equals("DISTRICTS")) correctRecords = districtsRecordNo;

        if (actualRecords==correctRecords) return false;

        Log.i(table+" record no.", (actualRecords+""));
        deleteAll(table);
        return true;
    }

    private void insertAgencies(String agnc_nm_kr, String agnc_nm_en, String own_kr, String own_en, String phone,
                       String fax, String adr_gu_kr, String adr_dt_kr, String adr_gu_en, String adr_dt_en,
                       int lang_en, int lang_cn, int lang_jp, String lang_etc){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO AGENCIES VALUES(" +
                "null," + agnc_nm_kr + "," + agnc_nm_en + "," + own_kr + "," + own_en + "," +
                phone + "," + fax + "," + adr_gu_kr + "," + adr_dt_kr + "," + adr_gu_en + "," +
                adr_dt_en + "," + lang_en +"," + lang_cn + "," +lang_jp + "," + lang_etc + ");"
        );
        db.close();
    }

    private void insertDistricts(String dist, double dist_pop, double fn_pop, double fn_rat, int avg_rent, int rank, String dist_near, String dist_near_en, String feats, String feats_en){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DISTRICTS VALUES(" +
                "null,"+dist+","+dist_pop+","+fn_pop+","+fn_rat+","+avg_rent+","+rank+","+dist_near+","+dist_near_en+","+feats+","+feats_en+");"
        );
        db.close();
    }

    /** 정상 입력 확인용*/
    public int countRecords(String table){
        int records=-1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
        cursor.moveToNext();
        records = Integer.parseInt(cursor.getString(0));

        return records;
    }

    /**테스트용 데이터 리셋(전체 레코드 삭제)*/
    private void deleteAll(String table){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+table);
    }

    //현재 미사용
    /*public Collection<AgencyVO> getResults(){
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
    }*/

    /**(영문) 구별 부동산 조회-현재미사용*/
    /*public Collection<AgencyVO> getAgenciesByAdrGuEn(String searchAdrGuEn){
        SQLiteDatabase db = getReadableDatabase();
        Collection list = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM AGENCIES WHERE adr_gu_en='"+searchAdrGuEn+"'", null);
        AgencyVO agency;
        while (cursor.moveToNext()) {
            agency = new AgencyVO(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getInt(10),
                    cursor.getInt(11), cursor.getInt(12), cursor.getString(13));
            list.add(agency);
        }
        return list;
    }*/

    //TODO: insert into districts 예시 - 현재 테이블 생성 안됨
    //임시적 테이블 초기화
//        if(countRecords("DISTRICTS")>0) deleteAll("DISTRICTS");
//        Log.d("after insert districts", countRecords("DISTRICTS")+"");

    public void insertDistrictsTotal() {
        insertDistricts("'강남구'", 551888, 4936, 0.89, 60, 25, "'서초구, 송파구'","'Seocho-gu, Songpa-gu'","'코엑스, 봉은사, 가로수길, 테헤란로'", "'COEX(Convention & Exhibition Center), Bongeunsa(buddhist temple), Garosu-gil(shopping street), Teheran-ro(office street)'");
        insertDistricts("'강동구'", 437050, 4301, 0.98, 52, 19, "'송파구'","'Songpa-gu'","'코엑스'", "'COEX'");
        insertDistricts("'강북구'", 326063, 3678, 1.13, 36, 2, "'도봉구, 노원구, 성북구'","'Dobong-gu, Nowon-gu, Seongbuk-gu'","'북악산 등등'", "'Northern Seoul dream forest park(ecological park), Bukhansan(famous mountain), April 19th National Cemetery'");
    }

    public void insertAgenciesTotal(){
        insertAgencies("'청솔공인중개사사무소'","'Cheongsol Certified Real Estate Agency'","'이돈권'","'Lee, Donkwon'","'82-2-763-1126'","'82-2-763-1141'","'종로구'","'종로구 종로 365(숭인동)'","'Jongno-gu'","'365, Jong-ro, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'진솔공인중개사사무소'","'Jinsol Certified Real Estate Agency'","'김형기'","'Kim, Hyungkee'","'82-2-738-4984'","'82-2-738-4983'","'종로구'","'종로구 자하문로 33 (통인동)'","'Jongno-gu'","'33, Jahamun-ro, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'한일공인중개사사무소'","'Hanil Certified Real Estate Agency'","'이웅기'","'Lee, Woongki'","'82-2-765-2189'","'82-2-765-2146'","'종로구'","'종로구 종로46길 1, 2층(창신동)'","'Jongno-gu'","'2F, 1, Jong-ro 46-gil, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'에이스공인중개사사무소'","'Ace Certified Real Estate Agency'","'기종익'","'Kee, Jongick'","'82-2-379-2600'","'82-0505-889-6161'","''","'종로구 평창길 8(평창동)'","'Jongno-gu'","'8, Pyeongchang-gil, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'부동산멘토공인중개사사사무소'","'Mentor Real Estate Agency'","'윤영숙'","'Youn, Young-sook'","'82-2-725-1127,82-736-1127'","'-'","'종로구'","'종로구 사직로8길24, 112호(내수동, 경희궁의아침2단지)'","'Jongno-gu'","'King`s GardenⅡ#112, 24 sajik-ro 8-gil, Jongno-gu'",1,0,1,"null");
        insertAgencies("'청운상록수공인중개사사무소'","'Cheongun Sangnoksu Certified Real Estate Agency'","'강희목'","'Kang, Heemok'","'82-2-738-5288'","'82-2-735-4982'","'종로구'","'종로구 자하문로33길 30(청운동)'","'Jongno-gu'","'30, Jahamun-ro 33-gil, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'중앙부동산공인중개사사무소'","'Jungang Certified Real Estate Agency'","'이문웅'","'Lee, Moonwoong'","'82-2-379-0047'","'82-2-379-4080'","'종로구'","'종로구 평창문화로 94(평창동)'","'Jongno-gu'","'94, Pyeongchangmunhwa-ro, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'종로상가부동산중개사무소'","'Jongno Sangga Certified Real Estate Agency'","'최문규'","'Choi, Mungyu'","'82-2-3672-3478'","'82-2-3672-3479'","'종로구'","'종로구 돈화문로11길 26 삼창빌딩 302호(돈의동,삼창빌딩)'","'Jongno-gu'","'#302, 26 Donhwamun-ro 11-gil, Jongno-gu'",1,0,0,"null");
        insertAgencies("'평창부동산중개사무소'","'Pyeongchang Certified Real Estate Agency'","'백태현'","'Paik,Taehyun'","'82-2-396-7744,82-2-396-4004'","'-'","'종로구'","'종로구 평창문화로 43-1'","'Jongno-gu'","'43-1, Pyeongchangmunhwa-ro, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'중앙공인중개사사무소'","'Jungang Certified Real Estate Agency'","'윤혜원'","'Lee, Moonwoong'","'82-2-739-1155'","'82-2-722-2634'","'종로구'","'종로구 송월길 147, 동아아파트 상가 101호'","'Jongno-gu'","'94, Pyeongchangmunhwa-ro, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'국제공인중개사사무소'","'Kukje Certified Real Estate Agency'","'구영주'","'Koo, Youngzoo'","'82-2-2233-2525'","'82-2-2233-2503'","'중구'","'중구 다산로12길 21(신당동)'","'Jung-gu'","'21, Dasan-ro 12-gil, Jung-gu, Seoul'",1,0,0,"null");
        insertAgencies("'동대문 공인중개사사무소'","'DongDaeMoon Certified Real Estate Agency'","'김명수'","'Kim, Myungsoo'","'82-2-2254-4984'","'82-2-798-5453'","'중구'","'중구 마장로 11 신한빌딩 402-1호(신당동)'","'Jung-gu'","'#402, 11, Majang-ro, Jung-gu, Seoul'",1,0,0,"null");
        insertAgencies("'앳서울컨설팅 공인중개사 사무소'","'Atseoul Consulting Certified Real Estate Agency'","'강훈정'","'Kang, Hunjoung'","'82-2-310-9009'","'82-2-310-9779'","'중구'","'중구 세종대로 64 해남빌딩 112호(태평로1가)'","'Jung-gu'","'#112, 64, Sejong-daero, Jung-gu, Seoul'",1,0,0,"null");
        insertAgencies("'온누리 공인중개사사무소'","'Onnuri Certified Real Estate Agency'","'권영필'","'Kwon, Youngpil'","'82-2-796-4955'","'82-2-796-4946'","'중구'","'중구 동호로37길 20(주교동)'","'Jung-gu'","'20, Dongho-ro 37-gil, Jung-gu, Seoul'",1,0,0,"null");
        insertAgencies("'부동산 114 삼성 공인중개사사무소'","'Realty 114 Samsung Certified Real Estate Agency'","'장영'","'Chang, Young'","'82-2-717-3399'","'82-2-714-6806'","'용산구'","'용산구 원효로 51, 108호 (산천동,리버힐삼성아파트 상가)'","'Yongsan-gu'","'#108, Samsung River Hill Apt. Sangga, 51, Wonhyo-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'아세아공인중개사무소'","'Asea Real Estate Agency'","'홍순원'","'Hong, soonwon'","'82-2-714-5873'","'82-2-714-5429'","'용산구'","'용산구 이촌로 248, 31동 101-2호(이촌동 한강맨션)'","'Yongsan-gu'","'#101-2, 31Dong, 248, Ichon-ro, Yongsan-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'에덴 공인중개사사무소'","'Eden Certified Real Estate Agency'","'최영호'","'Choi, Youngho'","'82-2-793-4774'","'82-2-793-4776'","'용산구'","'용산구 신흥로 9-1, 1층(용산동2가 5-1312)'","'Yongsan-gu'","'1F, 9-1, Sinheung-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'지니 공인중개사사무소'","'Jinny Certified Real Estate Agency'","'조진희'","'Cho, Jinhee'","'82-2-749-5959'","'82-2-749-5633'","'용산구'","'용산구 한강대로 145,103호 (한강로2가 256-2)'","'Yongsan-gu'","'#103,145, Hangang-daero, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'동원 공인중개사사무소'","'Dongwon Certified Real Estate Agency'","'강창수'","'Kang, Changsoo'","'82-2-792-1035'","'82-2-792-1036'","'용산구'","'용산구 녹사평대로40나길 48 (이태원동 396-1)'","'Yongsan-gu'","'48, Noksapyeong-daero 40na-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'Peace평화 공인중개사사무소'","'Peace Certified Real Estate Agency'","'박예순'","'Park, Yesoon'","'82-2-792-1006'","'82-2-792-1033'","'용산구'","'용산구 한강대로52길 17-8'","'Yongsan-gu'","'17-8, Hangang-daero 52-gil,Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'연세 공인중개사사무소'","'Yonsei Certified Real Estste Agency'","'반형석'","'Ban, hyeongsuk'","'82-2-798-3223'","'82-2-798-3226'","'용산구'","'용산구 이촌로 264, 삼익상가 106호(이촌1동, 삼익상가)'","'Yongsan-gu'","'#106, Samik Arcade, 264, Ichon-ro, Yongsan-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'알프스 공인중개사사무소'","'Alps Certified Real Estate Agency'","'홍순욱'","'Hong, Soonuk'","'82-2-774-1588'","'82-2-752-1588'","'용산구'","'용산구 후암로 8(후암동 251-16)'","'Yongsan-gu'","'8, Huam-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'비바컨설팅 공인중개사사무소'","'Viva-Consulting Certified Real Estate Agency'","'박은우'","'Park, Eunwoo'","'82-2-792-5500'","'82-2-793-5363'","'용산구'","'용산구 대사관로12길 4-2, 1층 1호(한남동)'","'Yongsan-gu'","'#1, 1F, 4-2, Daesagwan-ro 12-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'엘에이 공인중개사사무소'","'LA Certified Real Estate Agency'","'박경배'","'Park, Kyungbae'","'82-2-790-3555'","'82-2-790-5143'","'용산구'","'용산구 녹사평대로 46가길 11, 1층 101호 (이태원동)'","'Yongsan-gu'","'#101, 1F11, Noksapyeong-daero 46-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'뉴타운 공인중개사사무소'","'New-Town Certified Real Estate Agency'","'정돈희'","'Jung, Donhee'","'82-2-797-8200'","'82-2-797-8208'","'용산구'","'용산구 보광로 120-1, 1층(이태원동 131-24)'","'Yongsan-gu'","'1F, 120-1, Bogwang-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'써미트 공인중개사사무소'","'Summit Certified Real Estate Agency'","'유동숙'","'Yu, Dongsuk'","'82-2-765-6820'","'82-2-765-6821'","'용산구'","'용산구 한강대로 196, 3층(한강로1가)'","'Yongsan-gu'","'3F, 196, Hangang-daero, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'서울부동산net 공인중개사사무소'","'Seoul Realty-Net Certified Real Estate Agency'","'최연식'","'Choi, Younsik'","'82-2-797-9113'","'82-2-798-0894'","'용산구'","'용산구 한강대로 196, 3층(한강로1가)'","'Yongsan-gu'","'3F,196, Hangang-daero, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'한솔 공인중개사사무소'","'Hansol Certified Real Estate Agency'","'이유상'","'Lee, Yusang'","'82-2-798-6262'","'82-2-790-8882'","'용산구'","'용산구 이촌로65가길 66, 105호(이촌동, 대영상가)'","'Yongsan-gu'","'#105, Daeyoung Sangga, 66, Ichon-ro 65ga-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'넥서스 공인중개사사무소'","'Nexus Certified Real Estate Agency'","'유난희'","'Yoo, Nanhee'","'82-2-796-5670'","'82-2-766-1777'","'용산구'","'용산구 회나무로 78 (이태원동 5-2)'","'Yongsan-gu'","'78, Hoenamu-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'뉴타운 공인중개사사무소'","'New-Town Certified Real Estate Agency'","'김인순'","'Jung, Donhee'","'82-2-703-7888'","'82-2-703-7891'","'용산구'","'용산구 효창원로 269-1 (서계동 267-39)'","'Yongsan-gu'","'1F, 120-1, Bogwang-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'씨앤지 공인중개사사무소'","'C&G Certified Real Estate Agency'","'정재헌'","'Chung, Chaehun'","'82-2-795-9043'","'82-2-790-9105'","'용산구'","'용산구 녹사평대로40길 33(이태원동 552)'","'Yongsan-gu'","'33, Noksapyeong-daero 40-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'중앙드림랜드 공인중개사사무소'","'Jungang Dreamland Certified Real Estate Agency'","'강신영'","'Kang, Shinyoung'","'82-2-794-2002'","'82-2-794-9876'","'용산구'","'용산구 이촌로 248, 21동101호(이촌동, 한강맨숀상가)'","'Yongsan-gu'","'21-101, Hangang Mansion Sangga, 248, Ichon-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'림코 어드바이저 부동산중개법인'","'Reamko advisor Real Estate co., Ltd'","'유영화'","'You, Younghwa'","'82-2-749-9959'","'82-2-749-7959'","'용산구'","'용산구 독서당로 86, 2층( 한남동 28-11)'","'Yongsan-gu'","'2F, 86, Dokseodang-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'유엔 공인중개사사무소'","'UN Certified Real Estate Agency'","'장문희'","'Chang, Moonhee'","'82-2-792-2558'","'82-2-793-0741'","'용산구'","'용산구 독서당로 84,1층(한남동 28-12)'","'Yongsan-gu'","'1F, 84, Dokseodang-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'올스타공인중개사사무소'","'All-Star Certified Real Estate Agency'","'이용균'","'Lee, Yongkyun'","'82-2-794-8844'","'82-2-749-8892'","'용산구'","'용산구 우사단로2길 15, 1층 101호(보광동 265-219)'","'Yongsan-gu'","'#101, 1F, 15, Usadan-ro 2-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'그린부동산컨설팅 공인중개사사무소'","'Green Certified Real Estate Agency'","'정승섭'","'Chung, Seungsup'","'82-2-749-8070'","'82-2-749-8060'","'용산구'","'용산구 독서당로 98, 2층 204호 (한남동)'","'Yongsan-gu'","'#204, 2F, 98, Dokseodang-ro, Yongsan-gu,Seoul'",1,0,0,"null");
        insertAgencies("'하이렌트컨설팅 공인중개사사무소'","'Hi-Rent Certified Real Estate Agency'","'안영복'","'Ahn, Youngbok'","'82-2-797-7277'","'82-2-795-7077'","'용산구'","'용산구 이촌로 182(이촌동)'","'Yongsan-gu'","'182, Ichon-ro, Yongsan-gu,Seoul'",1,0,0,"null");
        insertAgencies("'서울부동산공인중개사사무소'","'Seoul Certified Real Estate Agency'","'강필수'","'Kang, Phillsoo'","'82-2-797-0102'","'82-2-797-9794'","'용산구'","'용산구 서빙고로51길 64, 101동 106호(서빙고동,그린파크(아)상가)'","'Yongsan-gu'","'101-106, Green Park APT, 64, Seobinggo-ro 51-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'서울하우징공인중개사사무소'","'Seoul Housing Certified Real Estate Agency'","'김영근'","'Kim, Youngkeun'","'82-2-749-7111'","'82-2-749-9711'","'용산구'","'용산구 회나무로 54, 1층(이태원동 210-8)'","'Yongsan-gu'","'54, Hoenamu-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'렉스공인중개사사무소'","'Rex Certified Real Estate Agency'","'안권찬'","'An, Gwonchan'","'82-2-790-8833'","'82-2-790-8837'","'용산구'","'용산구 신흥로 27 (용산동2가 5-756)'","'Yongsan-gu'","'27, Sinheung-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'114공인중개사사무소'","'114 Certified Real Estate Agency'","'이알렉스덕'","'Lee, Alexduk'","'82-2-711-4088'","'82-2-711-4089'","'용산구'","'용산구 청파로71길 26-3 (청파동1가 31-1)'","'Yongsan-gu'","'26-3, Cheongpa-ro 71-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'신용산 공인중개사사무소'","'Sinyongsan Certified Real Estate Agency'","'김병천'","'Kim, Byongchun'","'82-2-796-8833'","'82-2-796-5673'","'용산구'","'용산구 보광로 58, 1층(보광동 258-1)'","'Yongsan-gu'","'58, Bogwang-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'트라팰리스 공인중개사사무소'","'Trapalace Certified Real Estate Agency'","'하홍수'","'Ha, Hongsu'","'82-2-756-8959'","'82-2-756-4554'","'용산구'","'용산구 소월로2길 37, 1층(후암동 446-43)'","'Yongsan-gu'","'37, Sowol-ro 2-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'ABC 공인중개사사무소'","'ABC Certified Real Estate Agency'","'오미숙'","'Oh, Misook'","'82-2-797-4007'","'82-2-797-3537'","'용산구'","'용산구 장문로49길 5, 1층(한남동 568-232)'","'Yongsan-gu'","'5, Jangmun-ro 49-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'명당 공인중개사사무소'","'DabokCertified Real Estate Agency'","'이응준'","'Oh, Hyosuk'","'82-2-790-7430'","'82-2-6937-1147'","'용산구'","'용산구 한강대로21길 17-3'","'Yongsan-gu'","'#1F ,273, Hyochangwon-ro, Yongsan-gu, Seoul'",0,0,1,"null");
        insertAgencies("'한남유엔 공인중개사사무소'","'Hannam UN Certified Real Estate Agency'","'이세목'","'Lee, Saemok'","'82-2-792-7100'","'82-2-796-0330'","'용산구'","'용산구 대사관로30길 4, 1층(한남동 641-3)'","'Yongsan-gu'","'#1F ,4, Daesagwan-ro 30-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'다복공인중개사사무소'","'DabokCertified Real Estate Agency'","'오효석'","'Oh, Hyosuk'","'82-2-3273-8949'","'82-2-3273-4545'","'용산구'","'용산구 효창원로 273, 1층(서계동)'","'Yongsan-gu'","'#1F ,273, Hyochangwon-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'이지리빙공인중개사사무소'","'Easy Living Certified Real Estate Agency'","'유재영'","'Yu, Jaeyoung'","'82-2-792-3300'","'82-2-775-4425'","'용산구'","'용산구 한강대로 205 용산파크자이 D-2223호'","'Yongsan-gu'","'D-2223, 205, Hangang-daero, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'희망찬공인중개사사무소'","'Heemangchan Certified Real Estate Agency'","'김제성'","'Kim, Jeasung'","'82-2-719-0303'","'82-2-719-0535'","'용산구'","'용산구 원효로 174, 1층(원효로2가)'","'Yongsan-gu'","'1F, 174, Wonhyo-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'한림부동산 공인중개사사무소'","'HallimCertified Real Estate Agency'","'조창기'","'Joe, Changki'","'82-2-790-8077'","'82-2-790-8078'","'용산구'","'용산구 한남대로 20길 21, 1층 101호(한남동)'","'Yongsan-gu'","'#101, 1F, 21, Hannam-daero 20-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'와이에스피엠씨(YSPMC)공인중개사사무소'","'YSPMC Certified Real Estate Agency'","'차연신'","'Cha, Yoonshin'","'82-2-793-2021'","'82-2-793-2620'","'용산구'","'용산구 이태원로 211 한남빌딩 10층 1008호'","'Yongsan-gu'","'#1008, 10F Hannam Bldg, 211, Itaewon-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'필라공인중개사사무소'","'PillarCertified Real Estate Agency'","'이경자'","'Lee, Kyungja'","'82-2-790-3188'","'82-2-790-3177'","'용산구'","'용산구 녹사평대로 40나길 18 지층(이태원동)'","'Yongsan-gu'","'18, Noksapyeong-daero 40na-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'대림공인중개사사무소'","'DaerimCertified Real Estate Agency'","'김종순'","'Kim, Jongsoon'","'82-2-790-0079'","'82-2-796-7191'","'용산구'","'용산구 회나무로 39'","'Yongsan-gu'","'39, Hoenamu-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'유림공인중개사사무소'","'YuorimCertified Real Estate Agency'","'권순용'","'Kwon, Soonyong'","'82-2-794-7633'","'82-2-798-0843'","'용산구'","'용산구 장문로 26(동빙고동)'","'Yongsan-gu'","'26, Jangmun-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'앤디스부동산공인중개사사무소'","'Andy`s Certified Real Estate Agency'","'이상길'","'Lee, Sangkeel'","'82-2-794-0990'","'82-2-792-0877'","'용산구'","'용산구 서빙고로 69, B18호(용산동5가, 파크타워상가)'","'Yongsan-gu'","'#B18, Parktower Arcade, 69, Seobinggo-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'명부동산중개사무소'","'Myung Real Estate Agency'","'이태숙'","'Lee,Taesook'","'82-2-793-9696'","'82-2-790-6860'","'용산구'","'용산구 한남대로 20길 13(한남동)'","'Yongsan-gu'","'1F, 13, Hannam-daero 20-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'탑부동산컨설팅공인중개사사무소'","'Top realtor Real Estate Consulting Agency'","'김준호'","'Kim, Junho'","'82-2-790-7933'","'82-2-790-6860'","'용산구'","'용산구 장무로 147, 지층 1호(한남동)'","'Yongsan-gu'","'#B1, 147, Jangmun-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'줄리스 리얼티 앤 리로케이션 주식회사'","'Jules Realty and relocation Real Estate Brokerage Co., Ltd.'","'백주연'","'Baek, Juyeon'","'87-2-790-2097'","'82-2-790-2098'","'용산구'","'용산구 한강대로 210-2(한강로1가)'","'Yongsan-gu'","'210-2, Hangang-daero, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'퍼스트애비뉴공인중개사사무소'","'Frist Avenue Certified Real Estate Agency'","'강원희'","'Kang, Wonhee'","'82-2-749-0091'","'82-2-749-0092'","'용산구'","'용산구 이태원로 5-1 삼화빌딩 1F(한강로1가 24-1)'","'Yongsan-gu'","'5-1, Itaewon-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'(주)부동산중개법인 글로벌용산시대'","'Global Yongsan-AgeReal Estate Brokerage Co., Ltd.'","'김현정'","'Kim, Hyonjeong'","'82-2-712-9993'","'82-2-6335-8660'","'용산구'","'용산구 한강대로 69, 105호(한강로2가, 용산푸르지오써밋)'","'Yongsan-gu'","'#105, Yongsan-Prugiosummit, 69, Hangang-daero, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'카이로자산운용코리아공인중개사사무소'","'Ciro Asset Management Korea Certified Real Estate Agency'","'최이로'","'Choi, Iero'","'82-2-792-8230'","'82-2-532-7324'","'용산구'","'용산구 새창로 217, 용산토투밸리 1001호(한강로2가 2-37)'","'Yongsan-gu'","'#1001, 217, Saechang-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'이기호공인중개사사무소'","'Lee`s Certified Real Estate Agency'","'이기호'","'Lee, Kiho'","'82-2-797-2244,82-2-797-2254'","'-'","'용산구'","'용산구 대사관로34길 46(한남동 611-2)'","'Yongsan-gu'","'46, Daesagwan-ro 34-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'라꾸라꾸공인중개사사무소'","'RakuRaku Certified Real Estate Agency'","'정윤미'","'Jung, Yunmi'","'82-2-749-3802,82-2-792-6153'","'-'","'용산구'","'용산구 서빙고로 35, 103동 903호(한강로3가)'","'Yongsan-gu'","'103-dong 903-ho, 35 Seobinggo-ro, Yongsan-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'아스테리움서울부동산공인중개사사무소'","'Asterium Seoul Certified Real Estate Agency'","'김미애'","'Kim, MiAe'","'82-2-776-9999'","'82-2-757-6666'","'용산구'","'용산구 한강대로 372. B동106호 (동자동,센트레빌아스테리움서울)'","'Yongsan-gu'","'#106 Tower B, Centreville Asterium Seoul, 372 Hangang-daero,Yongsan -gu, Seoul'",1,0,0,"null");
        insertAgencies("'파크타워공인중개사사무소'","'ParkTower Certified Real Estate Agency'","'조태인'","'Jo, TaeIn'","'82-2-777-7367'","'82-2-777-7363'","'용산구'","'용산구 서빙고로 69, 파크타워 104-106(용산동5가, 파크타워)'","'Yongsan-gu'","'104-106, ParkTower, 69 Seobinggoro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'아스테리움부동산 공인중개사사무소'","'Asterium Certified Real Estate Agency'","'우승원'","'Woo, Seungwon'","'82-2-793-8949'","'82-2-793-8289'","'용산구'","'용산구 한강대로30길25 아스테리움용산 B120'","'Yongsan-gu'","'#120, B1, Asterium Yongsan, Hangangdaero 30gil 25, Yongsan-Gu'",1,0,0,"null");
        insertAgencies("'행복부동산중개연구소'","'Happinees Certified Real Estate Agency'","'장재혁'","'Jang, Jaehyeok'","'82-2-701-4985'","'82-2-701-1414'","'용산구'","'용산구 효창원로 115, 1층(용문동)'","'Yongsan-gu'","'1F, 115, Hyochangwon-ro Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'디원공인중개사사무소'","'D1 Certified Real Estate Agency'","'이민철'","'Lee, MinChul'","'82-2-797-7607'","'82-2-797-7607'","'용산구'","'용산구 이태원로 211,8층'","'Yongsan-gu'","'8F, 211, Itaewon-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'베스트원공인중개사사무소'","'Best One Certified Real Estate Agency'","'지영훈'","'Ji, YoungHoon'","'82-2-779-1119,82-2-757-1118'","'-'","'용산구'","'용산구 한강대로 372, B동 105호(센트레빌아스테리움서울, 동자동)'","'Yongsan-gu'","'#B-105, AsteriumSeoul, Hangang-daero 372, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'센트레빌공인중개사사무소'","'Centreville Certified Real Estate Agency'","'이정희'","'Lee, Junghee'","'82-2-563-6700,82-2-563-6700'","'-'","'용산구'","'용산구 후암로 35길 51'","'Yongsan-gu'","'51. Huam-ro 35-gil.Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'포춘공인중개사사무소'","'Fortune Certified Real Estate Agency'","'유병강'","'Yoo, Byoung Kang'","'82-2-749-5886'","'82-2-749-5887'","'용산구'","'용산구 이태원로 211, 813호(한남동,한남빌딩)'","'Yongsan-gu'","'#813, 211, Itaewon-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'경제공인중개사사무소'","'Kyungje Certified Real Estate Agency'","'정인철'","'Jung, Inchul'","'82-2-595-4235'","'82-2-595-4239'","'용산구'","'용산구 한강대로 161, 1층'","'Yongsan-gu'","'1st Floor,161, Hangang-daero Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'ERA한국부동산 공인중개사사무소'","'ERA Korea Certified Real Estate Agency'","'권용주'","'Kwon, Yongjoo'","'82-2-797-5589'","'82-2-796-5589'","'용산구'","'용산구 회나무로13길 9, 301호(이태원동, 남산대림아파트상가동)'","'Yongsan-gu'","'#301, 9, Hoenamu-ro 13-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'용산아스테리움신디스 공인중개사사무소'","'Yongsan Asterium Cindys Certified Real Estate Agency'","'박경훈'","'Park, Kyunghun'","'82-2-792-3222'","'82-2-792-3200'","'용산구'","'용산구 한강대로30길 25, B113호(한강로2가,아스테리움용산)'","'Yongsan-gu'","'#B113, Yongsan Asterium, 25, Hangang-daero 30-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'에이앤에이 공인중개사사무소'","'A&A Certified Real Estate Agency'","'전호재'","'Jeon, Hojae'","'82-2-794-1133'","'82-2-798-0201'","'용산구'","'용산구 이태원로 211, 1009호(한남동)'","'Yongsan-gu'","'#1009, 211, Itaewon-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'마이에이전트서울부동산중개법인 주식회사'","'MyAgent Seoul Real Estate Agency (ソウル不動産仲介法人(株))'","'이응주'","'Lee, Eungjoo'","'82-2-508-3668'","'82-2-508-3611'","'용산구'","'용산구 서빙고로67, 파크타워 105동 상가 16호(용산동5가)'","'Yongsan-gu'","'#16, Park Tower 105-dong, Seobinggo-ro 67-gil, Yongsan-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'강산 공인중개사사무소'","'Gangsan Certified Real Estate Agency'","'이수원'","'Lee, Soowon'","'82-2-2292-8799'","'82-2-2292-8799'","'성동구'","'성동구 왕십리로21길 5(행당동)'","'Seongdong-gu'","'5, Wangsimni-ro 21-gil, Seongdong-gu, Seoul'",1,0,0,"null");
        insertAgencies("'참빛부동산리서치 공인중개사사무소'","'Real-light Real Estate Reserch Agency'","'송창현'","'Song, Changhyeon'","'82-2-498-9669'","'82-2-498-9766'","'성동구'","'성동구 성수이로 137 , 현대아이파크상가 101호(성수동2가)'","'Seongdong-gu'","'#101, Hyundai I-park Apt., Sangga, 137, Seongsui-ro, Seongdong-gu, Seoul'",1,0,0,"null");
        insertAgencies("'한솔 공인중개사사무소'","'Hansol Certified Real Estate Agency'","'김수성'","'Kim, SooSung'","'82-2-2296-4848'","'82-2-2296-4832'","'성동구'","'성동구 독서당로 270 , 대우아파트상가 415호(금호동4가)'","'Seongdong-gu'","'Daewoo Apartment Plaza #415, 270 Dokseodang-ro, Seongdong-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'새싹공인중개사사무소'","'Sessak Certified Real Estate Agency'","'김성혜'","'Kim, Sunghye'","'82-2-463-1212'","'82-2-463-4477'","'성동구'","'성동구 왕십리로 80-1, 1층 2호(성수동 1가)'","'Seongdong-gu'","'1F #2, 80-1, Wangsimni-ro, Seongdong-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'예스공인중개사사무소'","'Yes Certified Real Estate Agency'","'김만재'","'Kim, Manjae'","'82-2-2299-0064'","'82-2-2299-0048'","'성동구'","'성동구 왕십리로 390, 107호 (상왕십리동,노블리안오피스텔)'","'Seongdong-gu'","'#107, 390 Wangsimni-ro, Seongdong-Gu, Seoul'",1,0,0,"null");
        insertAgencies("'비전공인중개사사무소'","'Vision Certified Real Estate Agency'","'주응중'","'Joo, Eungjoong'","'82-2-2292-3040'","'82-2-6081-0633'","'성동구'","'성동구 마장로 186, 101호 (홍익동)'","'Seongdong-gu'","'#101,186, majang-ro, Seongdong-Gu, Seoul'",1,0,0,"null");
        insertAgencies("'금호미소공인중개사사무소'","'Geumho Real Estate Agency(金湖微笑公認仲介士事務所)'","'이기숙'","'Lee, Kisook'","'82-2-2237-7500'","'82-2-2235-8715'","'성동구'","'성동구 금호로 105, 상가 B403'","'Seongdong-gu'","'Plaza B403, 105, Geumho-ro, Seongdong-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'강산부동산공인중개사사무소'","'Kangsan Certified Real Estate Agency'","'유태식'","'Yoo, Taishik'","'82-2-446-0092'","'82-2-453-3646'","'광진구'","'광진구 광나루로 606(구의동 200-1)'","'Gwangjin-gu'","'606, Gwangnaru-ro, Gwangjin-gu, Seoul'",1,0,0,"null");
        insertAgencies("'소백공인중개사사무소'","'Sobaek Certified Real Estate Agency'","'최교환'","'Choi, Kyohwan'","'82-2-432-8949'","'82-2-469-3300'","'광진구'","'광진구 긴고랑로 8(중곡동 248-1)'","'Gwangjin-gu'","'8, Gingorang-ro, Gwangjin-gu, Seoul'",1,0,0,"null");
        insertAgencies("'광나루공인중개사사무소'","'Gwangnaru Certified Real Estate Agency'","'서홍석'","'Seo, Hongsuk'","'82-2-444-4949'","'82-2-444-4995'","'광진구'","'광진구 아차산로 76길 27, 105호(광장동)'","'Gwangjin-gu'","'31, Achasan-ro 76-gil, Gwangjin-gu,Seoul'",1,0,0,"null");
        insertAgencies("'천지인공인중개사사무소'","'Cheonjiin Certified Real Estate Agency'","'신윤욱'","'Shin, Younwook'","'82-2-458-5151'","'82-2-876-8242'","'광진구'","'광진구 아차산로 530 현대프라자 101호(광장동 565)'","'Gwangjin-gu'","'530, Achasan-ro, Gwangjin-gu, Seoul'",1,0,0,"null");
        insertAgencies("'장원공인중개사사무소'","'Jangwon Certified Real Estate Agency'","'장무현'","'Jang, Muhyun'","'82-2-444-8066'","'82-2-444-8069'","'광진구'","'광진구 아차산로51길 36, 1층(구의동 257-76)'","'Gwangjin-gu'","'36, Achasan-ro 51-gil, Gwangjin-gu, Seoul'",1,0,1,"null");
        insertAgencies("'두산부동산중개인사무소'","'Doosan Real Estate Agency'","'장해문'","'Jang, Haemun'","'82-2-467-9103'","'82-2-463-7227'","'광진구'","'광진구 긴고랑로13길 10(중곡동 164-3)'","'Gwangjin-gu'","'10, Gingorang-ro 13-gil(Junggok-dong 164-3), Gwangjin-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'뉴월드부동산공인중개사사무소'","'New World Certified Real Estate Agency'","'김선옥'","'Kim, Sunok'","'82-2-468-8080,82-2-467-1666'","'-'","'광진구'","'광진구 자양동 848-1'","'Gwangjin-gu'","'848-1, Jayang-dong, Gwangjin-gu, Seoul, Republic of Korea'",0,1,0,"null");
        insertAgencies("'건대스타시티공인중개사사무소'","'Gundae Starcity Certified Real Estate Agency'","'장지형'","'Chang, Jihyung'","'82-2-3437-4005'","'82-2-3437-0002'","'광진구'","'광진구 아차산로36길 39, 상가107호(자양동)'","'Gwangjin-gu'","'#107, 39, Achasan-ro 36-gil, Gwangjin-gu, Seoul'",1,0,0,"null");
        insertAgencies("'아이비공인중개사사무소'","'Ivy Certified Real Estate Agency'","'권수한'","'Kwon, Soohan'","'82-2-923-2202'","'82-2-923-2202'","'동대문구'","'동대문구 안암로 134(제기동)'","'Dongdaemun-gu'","'134, Anam-ro, Dongdaemun-gu, Seoul'",1,0,0,"null");
        insertAgencies("'경희공인중개사사무소'","'kyunghee Certified Real Estate Agency'","'김하나'","'kim, Hana'","'82-2-969-4501'","'82-2-966-4501'","'동대문구'","'동대문구 회기로 138'","'Dongdaemun-gu'","'138, hoegi-ro dongdaemun-gu'",1,0,0,"null");
        insertAgencies("'삼성공인중개사사무소'","'Samsung Certified Real Estate Agency'","'조경현'","'Cho, Kyunghyun'","'82-2-965-0088'","'82-2-965-0086'","'동대문구'","'동대문구 회기로 172'","'Dongdaemun-gu'","'172, Hoegi-ro, Dongdaemun-gu, Seoul, Republic of Korea'",0,1,0,"null");
        insertAgencies("'우리 공인중개사사무소'","'Woori Certified Real Estate Agency'","'김정자'","'Kim, Jungja'","'82-2-433-8338'","'82-2-433-0821'","'중랑구'","'중랑구 상봉중앙로 57 (상봉1동 221-7)'","'Jungnang-gu'","'57, Sangbongjungang-ro (Sangbong1-dong 221-7), Jungnang-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'금강 공인중개사사무소'","'Kumgang Certified Real Eatate Agency'","'염흥섭'","'Yom, Heungseob'","'82-2-977-5775'","'82-2-972-5776'","'중랑구'","'중랑구 동일로157길 7(묵2동 244-143)'","'Jungnang-gu'","'7, Dongil-ro 157-gil, Jungnang-gu, Seoul'",1,0,0,"null");
        insertAgencies("'성현공인중개사사무소'","'Seonghyeon Certified Real Estate Agency'","'정문규'","'Jeong, Moongyoo'","'82-2-496-0500'","'82-2-496-0502'","'중랑구'","'중랑구 동일로140길 7(중화동)'","'Jungnang-gu'","'7, Donil-ro 140-gil, Jungnang-gu, Seoul'",1,0,0,"null");
        insertAgencies("'열린공인중개사사무소'","'Yeollin Certified Real Eatate Agency'","'유인자'","'Yoo, Inja'","'82-02-943-7111,82-02-943-3222'","'-'","'성북구'","'성북구 아리랑로18길 29(정릉동)'","'Seongbuk-gu'","'29, Arirang-ro 18-gil, Seongbuk-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'오리엔트공인중개사사무소'","'Orient Certified Real Estate Agency'","'김유숙'","'Kim, Yoosook'","'82-2-3672-4400'","'82-2-3672-1279'","'성북구'","'성북구 대사관로 98(성북동)'","'Seongbuk-gu'","'98 Daesagwan-ro, Seongbuk-gu, Seoul'",1,0,0,"null");
        insertAgencies("'로얄공인중개사사무소'","'Royal Real Estate Agency'","'강현수'","'Kang, Hyeonsoo'","'82-2-953-7477'","'82-2-953-7478'","'성북구'","'성북구 보문로 13길22,1층(보문동7가)'","'Seongbuk-gu'","'22,Bomun-ro 13-gil, Seongbuk-gu, Seoul'",1,0,0,"null");
        insertAgencies("'한일유엔아이 공인중개사사무소'","'hanil UNI Certified Real Estate Agency'","'김동주'","'Kim, Dongju'","'82-2-987-8945'","'82-2-987-8946'","'강북구'","'강북구 월계로21가길41 111동 상가 101호(미아동)'","'Gangbuk-gu'","'#101, 111dong Sangga, 41, Wolgye-ro 21ga-gil, Gangbuk-gu, Seoul'",1,0,0,"null");
        insertAgencies("'에이스부동산공인중개사사무소'","'Ace Certified Real Estate Agency'","'곽동선'","'Kee, Jongick'","'82-2-995-6060'","'82-2-995-6131'","'강북구'","'강북구 인수봉로78길 31 (인수동)'","'Gangbuk-gu'","'8, Pyeongchang-gil, Jongno-gu, Seoul'",1,0,0,"null");
        insertAgencies("'미래앤공인중개사사무소'","'Miraen Certified Real Estate Agency'","'권영창'","'Kwon, Youngchang'","'82-2-989-1114'","'82-2-989-2229'","'강북구'","'강북구 도봉로53길 7 (미아동)'","'Gangbuk-gu'","'7, Dobong-ro 53-gil, Gangbuk-gu, Seoul'",1,0,0,"null");
        insertAgencies("'강북부동산컨설팅공인중개사사무소'","'Gangbuk Real Estate Consulting Certified Agency'","'백칠현'","'Baek, Chilhyun'","'82-2-996-5577'","'-'","'강북구'","'강북구 한천로 1055(수유동 190-3)'","'Gangbuk-gu'","'1055, Hancheon-ro(Suyu-dong 190-3), Gangbuk-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'조은공인중개사사무소'","'Joeun Certified Real Estate Agency'","'이경숙'","'Lee, Kyungsook'","'82-2-990-0708'","'82-2-990-0711'","'도봉구'","'도봉구 노해로63가길 37, 102호(창5동)'","'Dobong-gu'","'#102, 37, Nohae-ro 63ga-gil, Dobong-gu'",1,0,0,"null");
        insertAgencies("'김병건공인중개사사무소'","'Kim Byunggun Certified Real Estate Agency'","'김병건'","'Kim, Byung-gun'","'82-2-930-8400'","'82-2-930-9722'","'노원구'","'노원구 동일로237길 70(상계1동)'","'Nowon-gu'","'70, Dongil-ro 237-gil, Nowon-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'신웅 공인중개사사무소'","'Shinwung Certified Real Estate Agency'","'서신웅'","'Suh, Shinwung'","'82-2-936-1589'","'82-2-933-9970'","'노원구'","'노원구 한글비석로15길 48, 현대종합상가 1층 130호(중계동)'","'Nowon-gu'","'#130, 1F, Hyundai Shopping Complx, 48, Hangeulbiseok-ro 15-gil, Nowon-gu, Seoul'",1,0,0,"null");
        insertAgencies("'가가굿랜드비젼공인중개사사무소'","'GaGa Good Landvision Certified Real Estate Agency'","'이명식'","'Lee, Myeong-shik'","'82-2-935-0005'","'82-2-935-0651'","'노원구'","'노원구 상계로 89 (상계동)'","'Nowon-gu'","'89, Sanggye-ro, Nowon-gu, Seoul'",1,0,0,"null");
        insertAgencies("'현대 공인중개사사무소'","'Hyundai Certified Real Estate Agency'","'양영동'","'Yang, Youngdong'","'82-2-354-5533'","'82-2-354-5575'","'은평구'","'은평구 진흥로93(역촌동)'","'Eunpyeong-gu'","'93, Jinheung-ro, Eunpyeong-gu, Seoul'",0,0,1,"null");
        insertAgencies("'예성 공인중개사사무소'","'Yesung Certified Real Estate Agency'","'이종배'","'Lee, Jongbae'","'82-2-383-2459'","'82-2-383-2480'","'은평구'","'은평구 연서로 99 (구산동)'","'Eunpyeong-gu'","'99, Yeonseo-ro, Eunpyeong-gu, Seoul'",1,0,0,"null");
        insertAgencies("'길부동산공인중개사사무소'","'Kil Certified Real Estate Agency'","'김윤화'","'Kim, Yoonhwa'","'82-2-356-8985'","'82-2-357-4974'","'은평구'","'은평구 연서로3나길 1 (역촌동)'","'Eunpyeong-gu'","'1, Yeonseo-ro 3na-gil, Eunpyeong-gu, Seoul'",0,0,1,"null");
        insertAgencies("'오렌지 공인중개사사무소(현재명칭.서린공인중개사사무소)'","'Orange Certified Real Estate Agency'","'김종일'","'Kim, Jongil'","'82-2-357-0008'","'82-2-355-3629'","'은평구'","'은평구 응암로32길 7,101호(응암동)'","'Eunpyeong-gu'","'#101, 7, Eungam-ro 32-gil, Eunpyeong-gu, Seoul'",0,0,1,"null");
        insertAgencies("'좋은자리공인중개사사무소'","'Good Place Certified Real Estate Agency'","'이갑호'","'Lee, Kapho'","'82-2-307-8779'","'82-2-303-4770'","'은평구'","'은평구 응암로14길 23, 1층(응암동)'","'Eunpyeong-gu'","'1F, 23 Eungam-ro 14-gil, Eunpyeong-gu,Seoul'",1,0,0,"null");
        insertAgencies("'DMC(수)공인중개사사무소'","'DMC(Su) Certified Real Estate Agency'","'천선기'","'Chun, Sungee'","'82-2-307-5300'","'82-2-384-7997'","'은평구'","'은평구 증산로15길 20, 102호'","'Eunpyeong-gu'","'#102, 20, Jeungsan-ro 15-gil, Eunpyeong-gu, Seoul'",1,0,0,"null");
        insertAgencies("'LBA문찬호부동산 공인중개사사무소'","'Moonchanho Realty Certified Real Estate Agency'","'문찬호'","'Moon, Chanho'","'82-2-392-4040'","'82-2-393-0804'","'서대문구'","'서대문구 이화여대3길 45 (대현동)'","'Seodaemun-gu'","'45, Ewhayeodae 3-gil, Seodaemun-gu, Seoul'",1,0,0,"null");
        insertAgencies("'가이아부동산공인중개사사무소'","'Gaia Certified Real Estate Agency'","'이순우'","'Lee, SoonWoo'","'82-2-363-4200'","'82-2-313-3212'","'서대문구'","'서대문구 신촌역로 16, 1층 102-1호(대현동, 신촌가이아)'","'Seodaemun-gu'","'#102-1, 1F, Gaia shinchon, 16, Sinchonnyeok-ro, Seodaemoon-Gu, Seoul'",1,0,0,"null");
        insertAgencies("'신성공인중개사사무소'","'Sinsung Certified Real Estate Agency'","'전용우'","'Jun,Yongwoo'","'82-2-372-0202'","'82-2-372-0212'","'서대문구'","'서대문구 서대문구 가재울로 45, 상가103호(남가좌동, 남가좌동현대아파트서문상가)'","'Seodaemun-gu'","'#103, 45, Gajaeul­ro, Seodaemun­gu, Seoul'",1,0,0,"null");
        insertAgencies("'연희동태양부동산공인중개사사무소'","'Yeonhuidong Sun Certified Real Estate Agency'","'오규연'","'Oh,Gyuyoun'","'82-2-322-8945'","'82-2-323-8945'","'서대문구'","'서대문구 증가로 48(연희동)'","'Seodaemun-gu'","'48, Jeungga-ro, Seodaemun-gu, Seoul'",1,0,0,"null");
        insertAgencies("'김동영공인중개사사무소'","'Kim Dongyoung Certified Real Estate Agency'","'김동영'","'Kim, Dongyoung'","'82-02-365-0080'","'82-02-365-0089'","'서대문구'","'서대문구 이화여대8길 36, 1층(대현동, 공간휴)'","'Seodaemun-gu'","'#1F, 36, Ewhayeodae 8-gil, Seodaemun-gu, Seoul'",1,0,1,"null");
        insertAgencies("'JK공인중개사사무소'","'JK Certified Real Estate Agency'","'임진기'","'Lim, Jinki'","'82-02-393-8489'","'82-02-393-8486'","'서대문구'","'서대문구 신촌로 109, B220호(창천동, 신촌르메이에르5차)'","'Seodaemun-gu'","'#B220, 109 Sinchon-ro, Seodaemun-gu, Seou'",1,0,0,"null");
        insertAgencies("'신촌자이엘라부동산공인중개사사무소'","'Shinchon xiella Certified Real Estate Agency'","'신혜선'","'Shin, hyesun'","'82-02-364-4080'","'82-02-364-4980'","'서대문구'","'서대문구 신촌로 149, B101호(대현동, 신촌자이엘라)'","'Seodaemun-gu'","'#B101, 149, Sinchon-ro, Seodaemun-gu, Seoul'",1,0,0,"null");
        insertAgencies("'신성공인중개사사무소'","'Best Certified Real Estate Agency'","'공영숙'","'Jeong, Siyoung'","'82-02-364-1919'","'82-02-363-0557'","'서대문구'","'서대문구 이화여대7길 23, 1층(대현동)'","'Seodaemun-gu'","'1F, 1, Dongnimmun-ro, Seodaemun-gu, Seoul'",0,0,1,"null");
        insertAgencies("'베스트공인중개사사무소'","'Best Certified Real Estate Agency'","'정시영'","'Jeong, Siyoung'","'82-2-312-8500'","'82-2-312-8500'","'서대문구'","'서대문구 독립문로 1, 1층'","'Seodaemun-gu'","'1F, 1, Dongnimmun-ro, Seodaemun-gu, Seoul'",1,0,0,"null");
        insertAgencies("'연희공인중개사사무소'","'Yeonhui Certified Real Estate Agency'","'이은희'","'Lee, Eunhee'","'82-2-333-0302'","'82-2-333-0302'","'서대문구'","'서대문구 연희로12길 10-4'","'Seodaemun-gu'","'10-4, Yeonhui-ro 12-gil, Seodaemun-gu,Seoul'",1,0,0,"null");
        insertAgencies("'GS공인중개사사무소'","'GS Certified Real Estate Agency'","'박만순'","'Park, Mahn Soon'","'82-2-3275-2092'","'82-2-3275-2093'","'마포구'","'마포구 마포대로 44, 10층 6호(도화동, 진도빌딩)'","'Mapo-gu'","'#1006, Jin-Do Building, 44, Mapo-daero, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'예일 공인중개사사무소'","'Yale Certified Real Estate Agency'","'장득걸'","'Jang, Deukkul'","'82-2-3141-1233'","'82-504-033-3030'","'마포구'","'마포구 양화로 157,파라다이스텔 B-03 호(동교동,파라다이스텔)'","'Mapo-gu'","'B-3, Paradisetel, 157, Yanghwa-ro, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'마포자이2차 공인중개사사무소'","'Mapo Xi 2nd Certified Real Estate Agency'","'임경채'","'Lim, Kyungchae'","'82-2-716-5100'","'82-2-713-5456'","'마포구'","'마포구 백범로 83-1, 1층(대흥동)'","'Mapo-gu'","'83-1, Baekbeom-ro, Mapo-gu, Seoul'",0,0,1,"null");
        insertAgencies("'㈜글로벌서울부동산법인'","'Global Seoul Real Estate Co., Ltd'","'윤선화'","'Youn, Seonhwa'","'82-2-717-0049'","'82-2-702-2480'","'마포구'","'마포구 큰우물로 52, 102호(용강동,세일상가)'","'Mapo-gu'","'#102, Seil Complex Store, 52, Keunumul-ro, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'신대림공인중개사사무소'","'Sindaerim Certified Real Estate Agency'","'조항준'","'Cho, Hangjoon'","'82-2-375-4489'","'82-2-490-9114'","'마포구'","'마포구 월드컵북로 188 (성산동)'","'Mapo-gu'","'188, World Cup buk-ro, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'현대 공인중개사사무소'","'Hyundai Certified Real Estate Agency'","'이미숙'","'Lee, Misook'","'82-2-376-7700'","'82-2-376-7720'","'마포구'","'마포구 상암산로1길 57, 상가 101호(상암동,월드컵파크6단지)'","'Mapo-gu'","'#101, Sangga, 57, Sangamsan-ro 1-gil, Mapo-gu, Seoul'",1,0,1,"null");
        insertAgencies("'공덕신영부동산공인중개사사무소'","'Gongdeok Shinyoung Certified Real Estate Agency'","'이기봉'","'Lee, Kibong'","'82-2-717-7778'","'82-2-711-6500'","'마포구'","'마포구 백범로 178, 1층 104호(공덕동,마포신영지웰)'","'Mapo-gu'","'178, Baekbeom-ro, Mapo-gu, Seoul'",0,0,1,"null");
        insertAgencies("'이안공인중개사사무소'","'Iaan Certified Real Estate Agency'","'최은진'","'Choi, Eunjin'","'82-2-376-0089'","'82-2-6393-6377'","'마포구'","'마포구 월드컵북로 361, 112호 (상암동,이안오피스텔2단지)'","'Mapo-gu'","'#112, 361, World Cup buk-ro, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'미소공인중개사사무소'","'Smile Certified Real Estate Agency'","'노상규'","'Noh, SangKyu'","'82-2-713-7756~7'","'82-2-713-7758'","'마포구'","'마포구 신촌로 170, 상가106호(대흥동, 이대푸르지오시티)'","'Mapo-gu'","'#B106, 170, Sinchon-ro, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'포럼부동산중개사무소'","'Forum Certified Real Estate Agency'","'박 순'","'Park, Soon'","'82-2-338-8988'","'82-707-7753-7019'","'마포구'","'마포구 양화로 133, 11층1110호 (서교동, 서교타워)'","'Mapo-gu'","'Rm, 1110, Seokyo Tower, 133 Yanghwa-ro, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'샘터공인중개사사무소'","'Samteo Certified Real Estate Agency'","'최장규'","'Choi, JangKyu'","'82-2-338-9989'","'82-2-338-9987'","'마포구'","'마포구 독막로19길 45 ,1층(상수동)'","'Mapo-gu'","'45, Dongmak-ro 19-gil, Mapo-gu, Seoul'",0,1,0,"null");
        insertAgencies("'타워공인중개사사무소'","'Tower Certified Real Estate Agency'","'이영태'","'Lee, Yeongtae'","'82-2-393-3500'","'82-2-393-3502'","'마포구'","'마포구 마포대로 195, 상가 4동 113호(아현동, 마포래미안푸르지오)'","'Mapo-gu'","'Arcade 4-113(Ahyun-dong, Mapo Ramian Prugio) #195, Mapo-daero Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'연세공인중개사사무소'","'Yonsei Certified Real Estste Agency'","'이준규'","'Lee, Junkyu'","'82-2-706-1472'","'82-2-706-0046'","'마포구'","'마포구 토정로 35길 11, B103 (용강동, 인우빌딩)'","'Mapo-gu'","'#B103, 11, Tojung-ro 35-gil, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'넥스트원코리아공인중개사사무소'","'Next One Korea Certified Real Estate Agency'","'최보경'","'Choi, BoKyung'","'82-2-794-1004'","'82-2-797-5431'","'마포구'","'마포구 백범로 199, 515호, 516호(신공덕동, 메트로디오빌)'","'Mapo-gu'","'#515 - 516, 199, Baekbeom-ro, Mapo-gu, Seoul'",0,0,1,"null");
        insertAgencies("'한우공인중개사사무소'","'Hanwoo Certified Real Estate Agency'","'서준호'","'Seo, Junho'","'82-2-701-9885'","'82-2-6010-8887'","'마포구'","'마포구 마포대로 52, 801호'","'Mapo-gu'","'#801, 52, Mapo-daero, Mapo-gu, Seoul'",0,1,0,"null");
        insertAgencies("'시민공인중개사사무소'","'Simin Certified Real Estate Agency'","'양영조'","'Yang, Yongjo'","'82-2-2647-8949'","'82-2-2647-1025'","'양천구'","'양천구 신목로 39, 1층(신정동 114-5)'","'Yangcheon-gu'","'39, Sinmok-ro, Yangcheon-gu, Seoul'",0,0,1,"null");
        insertAgencies("'성실공인중개사사무소'","'Seongsil Certified Real Estate Agency'","'신승범'","'Shin, Seungbum'","'82-2-2647-3100'","'82-2-2648-6970'","'양천구'","'양천구 목동서로 38, 상가B동 101-3호(목동,목동아파트1단지)'","'Yangcheon-gu'","'#101-3, B, 38, Mokdongseo-ro, Yangcheon-gu, Seoul'",0,0,1,"null");
        insertAgencies("'LG공인중개사사무소'","'LG Certified Real Estate Agency'","'임춘길'","'Lim, Choongil'","'82-2-2065-3322'","'82-2-2065-8228'","'양천구'","'양천구 곰달래로5길 51, 1층(신월동 137-18)'","'Yangcheon-gu'","'51, Gomdallae-ro 5-gil, Yangcheon-gu, Seoul'",0,0,1,"null");
        insertAgencies("'하이페리온공인중개사사무소'","'Hyperion Certified Real Estate Agency'","'김명준'","'Kim, Myungjun'","'82-2-2648-5300'","'82-2-2643-0050'","'양천구'","'양천구 신목로 119 (목동 406-316)'","'Yangcheon-gu'","'119, Sinmok-ro, Yangcheon-gu, Seoul'",1,0,0,"null");
        insertAgencies("'드림타워공인중개사사무소'","'Dream Tower Certified Real Estate Agency'","'전소영'","'Jeon, Soyoung'","'82-2-2646-5333'","'82-2-2116-2223'","'양천구'","'양천구 목동동로 233-1,현대드림타워 911호(목동 923-14)'","'Yangcheon-gu'","'#911, 233-1, Mokdongdong-ro, Yangcheon-gu, Seoul'",0,0,1,"null");
        insertAgencies("'예스공인중개사사무소'","'Yes Certified Real Estate Agency'","'권해구'","'Kwon, Haekoo'","'82-2-2651-2289'","'82-2-6737-4110'","'양천구'","'양천구 목동동로8길 15, 101호 (신정동,정진빌딩)'","'Yangcheon-gu'","'#101, 15, Mokdongdong-ro 8-gil, Yangcheon-gu, Seoul'",0,1,0,"null");
        insertAgencies("'목동명지공인중개사사무소'","'Mokdong Myeong-ji certified Real Estate Agency'","'황성원'","'Hwang, Sungwon'","'82-2-2654-4988'","'82-2-2654-3611'","'양천구'","'양천구 목동로 224, 101동 상가 104-1호(목동, 대원칸타빌1차아파트)'","'Yangcheon-gu'","'#104-1, 101-dong, 224, Mokdong-ro, Yangcheon-gu, Seoul'",1,0,0,"null");
        insertAgencies("'현대아이 공인중개사사무소'","'Hyundai-I Certified Real Estate Agency'","'김상현'","'Kim, Sanghyun'","'82-2-2695-4998'","'82-2-2696-7611'","'강서구'","'강서구 화곡로31길 82 (화곡동)'","'Gangseo-gu'","'82, Hwagok-ro 31-gil, Gangseo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'LBA서울 공인중개사사무소'","'LBA Seoul Certified Real Estate Agency'","'이성무'","'Lee, Seongmu'","'82-2-2691-1700'","'82-2-2606-2282'","'강서구'","'강서구 강서로45길 9 (화곡동)'","'Gangseo-gu'","'9, Gangseo-ro 45-gil, Gangseo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'손낙술공인중개사사무소'","'Sohn Nelson Certified Real Estate Agency'","'손낙술'","'Sohn, Nelson'","'82-2-3662-5555'","'82-2-3664-0304'","'강서구'","'강서구 강서로 348, 104-12호(내발산동, 우장'","'Gangseo-gu'","'#104-12, Ujangsan Hillstate Mall, 348, Gangseo-ro, Gangseo-gu,Seoul'",1,0,1,"null");
        insertAgencies("'마곡에스비 공인중개사사무소'","'Magok SB Certified Real Estate Agency'","'오영옥'","'Oh, Youngok'","'82-2-2654-5959'","'82-2-2654-5952'","'강서구'","'강서구 마곡중앙5로 81, 109호(마곡동,에스비타운)'","'Gangseo-gu'","'#109, 81, Magokjungang 5-ro, Gangseo-gu, Seoul'",0,0,1,"null");
        insertAgencies("'롯데공인중개사사무소'","'Lotte Certified Real Estate Agency'","'김재윤'","'Kim, Jae Yoon'","'82-2-854-8534'","'82-2-854-8582'","'구로구'","'구로구 도림로 41(구로동)'","'Guro-gu'","'41, Dorim-ro, Guro-gu, Seoul'",1,0,0,"null");
        insertAgencies("'글로벌에이스공인중개사사무소'","'Global Ace Certified Real Estate Agency'","'김선남'","'Kim, Seon Nam'","'82-2-733-6777'","'82-2-733-6786'","'구로구'","'구로구 경인로 661, 102동 2311호(신도림동, 신도림1차 푸르지오)'","'Guro-gu'","'#102-2311, 661, Gyeongin-ro, Guro-gu, Seoul'",0,0,1,"null");
        insertAgencies("'청호 공인중개사사무소'","'Cheongho Certified Real Estate Agency'","'박내열'","'Park, Naeyeol'","'82-2-808-8920'","'82-2-808-8970'","'금천구'","'금천구 금하로24길 39(시흥동)'","'Geumcheon-gu'","'39, Geumha-ro 24-gil, Geumcheon-gu, Seoul'",1,0,0,"null");
        insertAgencies("'sk공인중개사사무소'","'SK Certified Real Estate Agency'","'문혁래'","'Moon, Hyouklae'","'82-2-6292-2400'","'82-2-6292-2402'","'금천구'","'금천구 가산디지털1로 119, 비동 103호(가산동, sk트윈테크타워)'","'Geumcheon-gu'","'#103, B-dong, 119, Gasan digital 1-ro, Geumcheon-gu, Seoul'",1,0,0,"null");
        insertAgencies("'스타팍스부동산중개㈜'","'Star Parks Real Estate Co.,Ltd.'","'우윤성'","'Woo, Younsung'","'82-2-6337-1000'","'82-2-783-0086'","'영등포구'","'영등포구 국제금융로8길 19, 1012호(여의도동)'","'Yeongdeungpo-gu'","'#1012, Center Bldg., 19, Gukjegeumyung-ro 8-gil, Yeongdeungpo-gu, Seoul'",1,1,1,"'러시아어'");
        insertAgencies("'문래파라곤공인중개사사무소'","'Munrae Parogon Real Estate Agency'","'이승진'","'Lee, SeungJin'","'82-2-2635-1900'","'82-2-2635-1551'","'영등포구'","'영등포구 선유로11길 12, 101동 102호(문래동6가)'","'Yeongdeungpo-gu'","'101-102, Munrae Paragon, 12, Seonyu-ro 11-gil,Yeongdeungpo-gu, Seoul'",1,0,1,"null");
        insertAgencies("'효성입점부동산중개사무소'","'Hyosungibjeom Certified Real Estate Agency'","'정주억'","' Jeong, Jueok'","'82-2-2631-8000'","'82-2-2631-0000'","'영등포구'","'영등포구 당산로205, 103B호(당산동5가,당산역해링턴타워)'","'Yeongdeungpo-gu'","'#103B 205, Dangsan-ro, Yeongdeungpo-gu, Seoul'",0,0,1,"null");
        insertAgencies("'아크로정문공인중개사사무소'","'Akeurojeongmun Real Estate Agency'","'김알빈만식'","'Kim, Arvinmansik'","'82-2-773-0055'","'-'","'영등포구'","'영등포구 양산로 206, 1층(영등포동5가)'","'Yeongdeungpo-gu'","'1F, 206, Yangsan-ro, Yeongdeungpo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'삼성 공인중개사사무소'","'Samsung Certified Real Estate Agency'","'강지영'","'Gang, Jiyoung'","'82-2-814-5848'","'82-2-824-5114'","'동작구'","'동작구 노량진동 263-11'","'Yeongdeungpo-gu'","'39, Noryangjin-ro 8-gil, Dongjak-gu, Seoul'",1,0,0,"null");
        insertAgencies("'신현대공인중개사사무소'","'Shin-Hyundai Certified Real Estate Agency'","'김영식'","'Kim, Youngsik'","'82-2-817-8008'","'82-2-817-8532'","'동작구'","'동작구 등용문로 127, 103동116호(대방동,현대아파트상가)'","'Dongjak-gu'","'#116, 103Dong Hyundai Sangga, 127, Deungyong-ro, Dongjak-gu, Seoul'",1,0,0,"null");
        insertAgencies("'알파공인중개사사무소'","'Alpha Certified Real Estate Agency'","'하창수'","'Ha, Changsoo'","'82-2-844-6000'","'82-2-844-6007'","'동작구'","'동작구 여의대방로 10길 13, 한성아파트상가 101호'","'Dongjak-gu'","'#101, Hansung Apartmant Store, 13,Yeouidaebang-ro 10-gil, Dongjak-gu, Seoul'",1,0,0,"null");
        insertAgencies("'하나부동산 공인중개사사무소'","'Hana Certified Real Estate Agency'","'이진옥'","'Lee, Jinok'","'82-2-889-7003'","'82-2-888-6714'","'관악구'","'관악구 청림6길 3, 상가동105호(봉천동,관악푸르지오)'","'Dongjak-gu'","'#105, Daewoo Apt. Sangga,27, Gwanak-ro 30-gil, Gwanak-gu,Seoul'",1,0,0,"null");
        insertAgencies("'동화공인중개사사무소'","'DongHwa Certified Real Estate Agency'","'안종인'","'An, Jongin'","'82-2-884-8579'","'82-2-878-8565'","'관악구'","'관악구 남부순환로 1847'","'Gwanak-gu'","'1847, Nambusunhwan-ro, Gwanak-gu, Seoul'",1,0,0,"null");
        insertAgencies("'신미래공인중개사사무소'","'Sinmirae Certified Real Estate Agency'","'김성은'","'Kim, Seongeun'","'82-2-877-0062'","'82-2-877-0063'","'관악구'","'관악구 봉천로 261, 1층'","'Gwanak-gu'","'1F, 261, Bongchun-ro, Gwanak-gu, Seoul'",1,0,0,"null");
        insertAgencies("'반포114 공인중개사사무소'","'Banpo114 Certified Real Estate Agency'","'이동하'","'Lee, Dongha'","'82-2-595-5982'","'82-2-595-5909'","'서초구'","'서초구 신반포로 49 엠동 제14호(반포동,구반포상가)'","'Gwanak-gu'","'M-14, 49, Sinbanpo-ro, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'남북 공인중개사사무소'","'Nambuk Certified Real Estate Agency'","'이오성'","'Lee, Ohsung'","'82-2-577-9498'","'82-2-577-9407'","'서초구'","'서초구 강남대로12길 49, 1층(양재동, 대덕빌딩)'","'Seocho-gu'","'49, Gangnam-daero 12-gil,Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'대성공인중개사사무소'","'Daeseong Certified Real Estate Agency'","'박진'","'Park, Jin'","'82-2-537-1044'","'82-2-536-2369'","'서초구'","'서초구 서래로10길 9, 101호(반포동, 서래빌딩)'","'Seocho-gu'","'#101, Seorae Bldg., 9, Seorae-ro 10-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'센추리케이에스부동산중개주식회사'","'Century KS Realty Co.,Ltd.'","'김승'","'Kim, Seung'","'82-2-534-8252'","'82-2-534-8292'","'서초구'","'서초구 동광로46길 4, 지2층 101호(반포동)'","'Seocho-gu'","'#B101, 4, Donggwang-ro 46-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'황금 공인중개사사무소'","'Hwangkum Certified Real Estate Agency'","'송한근'","'Song, Hangun'","'82-2-525-0077'","'82-2-525-0880'","'서초구'","'서초구 효령로34길 76, 1층 1호(방배동,동림빌딩)'","'Seocho-gu'","'#1,1F, 76, Hyoryeong-ro 34-gil, Seocho-gu, Seoul'",1,0,0,"'스페인어'");
        insertAgencies("'으뜸 공인중개사사무소'","'Top Certified Real Estate Agency'","'이광수'","'Lee, Kwangsoo'","'82-2-522-5355'","'82-2-522-4288'","'서초구'","'서초구 서초대로64길 50 , 103호(서초동,아남상가)'","'Seocho-gu'","'#103, Anam Sangga, 50, Seocho-daero 64-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'한양부동산중개사무소'","'Hanyang Certified Real Estate Agency'","'박찬엽'","'Park, Chanyeop'","'82-2-582-3939'","'82-2-525-7728'","'서초구'","'서초구 서초대로 46길 3, 1층(서초동)'","'Seocho-gu'","'1F, 3, Seocho-daero 46-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'지우 공인중개사사무소'","'Giwoo Certified Real Estate Agency'","'김삼복'","'Kim, Sambok'","'82-2-581-3333'","'82-2-3471-1070'","'서초구'","'서초구 방배천로4안길 24, 1층(방배동)'","'Seocho-gu'","'1F, 24, Bangbaecheon-ro 4an-gil, Seocho-gu, Seoul'",0,0,1,"null");
        insertAgencies("'우일공인중개사사무소'","'Wooil Certified Real Estate Agency'","'이춘호'","'Lee, Chunho'","'82-2-514-8959'","'82-2-515-4980'","'서초구'","'서초구 강남대로89길 7 ,1층(반포동, 올릭스빌딩)'","'Seocho-gu'","'1F, 7, Gangnam-daero 89-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'삼익공인중개사사무소'","'Wooil Certified Real Estate Agency'","'현창환'","'Lee, Chunho'","'82-2-533-4001'","'82-2-593-4934'","'서초구'","'서초구 서초중앙로24길 55, 지105호(서초동,중앙서초프라자)'","'Seocho-gu'","'1F ,7, Gangnam-daero 89-gil, Seocho-Gu, Seoul'",1,0,0,"null");
        insertAgencies("'금동산 공인중개사사무소'","'Samik Certified Real Estate Agency'","'김서정'","'Hyun, Changhwan'","'82-2-597-7171'","'82-2-586-8830'","'서초구'","'서초구 서초중앙로 72(서초동, 영빌딩)'","'Seocho-gu'","'#105 ,55, Seochojungang-Plaza, Seochojungang-ro 24-gil, Seocho-gu, Seoul'",1,0,1,"null");
        insertAgencies("'오피스그룹공인중개사사무소'","'kumdongsan Certified Real Estate Agency'","'박명진'","'kim, Seojeoung'","'82-2-557-8444'","'82-2-6280-9175'","'서초구'","'서초구 강남대로 481, 8층 808호(서초동,두산베어스텔)'","'Seocho-gu'","'72, Seochojungang-ro, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'강남효성공인중개사사무소'","'Office Certified Real Estate Agency'","'정유리'","'Park, Myungjin'","'82-2-588-2110'","'82-2-582-2117'","'서초구'","'서초구 강남대로51길 10, 1층102호(서초동, 강남효성해링턴타워)'","'Seocho-gu'","'#808, 481, Gangnam­daero, Seocho-gu, Seoul'",1,1,0,"null");
        insertAgencies("'한국 공인중개사사무소'","'Gangnam HyosungCertified Real Estate Agency'","'이희선'","'Jeong Yuri'","'82-2-578-0508'","'82-2-578-0589'","'서초구'","'서초구 반포대로 14길 54, 2층 205호(서초동)'","'Seocho-gu'","'102, 1337-6, Seocho-dong, Seocho-gu, Seoul'",1,1,0,"null");
        insertAgencies("'신동아공인중개사사무소'","'Shin Dong A Certified Real Estate Agency'","'김학성'","'Kim, Haksung'","'82-2-599-5000'","'82-2-594-6608'","'서초구'","'서초구 신반포로 219, 1층 15호(잠원동, 반포쇼핑타운8동)'","'Seocho-gu'","'#15, Banpo Shopping Town 8-dong, 219 Sinbanpo-ro, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'에이스렌트 공인중개사사무소'","'Ace Rent Certified Real Estate Agency'","'김재우'","'Kim, Jaewoo'","'82-2-797-0330'","'82-2-794-2181'","'서초구'","'서초구 서래로 32 1층3호'","'Seocho-gu'","'#3, 1F, 32, Seorae-ro, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'리버파크단지내공인중개사사무소'","'River Park Danjinae Certified Real Estate Agency'","'김은'","'Kim, Eun'","'82-2-532-5100'","'82-2-535-7070'","'서초구'","'서초구 신반포로15길 29, 1층132호(반포동,신반포상가)'","'Seocho-gu'","'#132, 1F, 29, Sinbanpo-ro 15-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'LBA한신부동산중개사무소'","'LBA HanShin Certified Real Estate Agency'","'전영득'","'Chun, Youngdeuk'","'82-2-2042-1800'","'82-2-8706-6110'","'서초구'","'서초구 사평대로55길 45(반포동)'","'Seocho-gu'","'45, Sapyeong-daero 55-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'지앤비롯데공인중개사사무소'","'G&B Lotte Certified Real Estate Agency'","'박순서'","'Park, Soonseo'","'82-2-548-2002'","'82-2-540-3953'","'강남구'","'강남구 학동로 431, 101호(청담동)'","'Seocho-gu'","'#101, 431, Hakdong-ro, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'뉴욕부동산 공인중개사사무소'","'NewYork Certified Real Estate Agency'","'박인순'","'Park, Insoon'","'82-2-566-8666'","'82-2-529-8207'","'강남구'","'강남구 남부순환로 2804, A동 104-2호(도곡동,아카데미스위트)'","'Gangnam-gu'","'#104-2, Academysweet A-Dong, 2804, Nambusunhwan-ro, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'렉슬탑공인중개사사무소'","'Rexle-Top Certified Real Estate Agency'","'문종규'","'Moon, Jonggyo'","'82-2-576-5765'","'82-2-576-5796'","'강남구'","'강남구 선릉로 225 , 상가 137호(도곡동,도곡렉슬)'","'Gangnam-gu'","'#137, Dogok Rexle Sangga, 225, Seolleung-ro, Gangnam-gu,Seoul'",1,0,0,"null");
        insertAgencies("'아남공인중개사사무소'","'Anam Certified Real Estate Agency'","'이원기'","'Lee, Wonki'","'82-2-2009-4000'","'82-2-565-0505'","'강남구'","'강남구 테헤란로 311, 지층 124호(역삼동)'","'Gangnam-gu'","'#124, 1st Basement, 311, Teheran-ro, Gangnam-gu, Seoul'",1,0,1,"null");
        insertAgencies("'특집 공인중개사사무소'","'Teukjip Real Estate Agency'","'배종주'","'Bae Jongju'","'82-2-543-9036'","'82-2-543-9038'","'강남구'","'강남구 선릉로130길 57, 1층 12호,13호(삼성동)'","'Gangnam-gu'","'#13, 1F, 57, Seolleung-ro 130-gil, Gangnam-gu, Seoul'",0,0,1,"null");
        insertAgencies("'상원부동산중개주식회사'","'Sangwon Realty Co.,Ltd.'","'박상원'","'Park, Sangwon'","'82-2-541-8866'","'82-2-541-4012'","'강남구'","'강남구 영동대로128길 5, 107호(삼성동)'","'Gangnam-gu'","'#107, 5, Yeongdong-daero 128-gil, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'전영석 공인중개사사무소'","'Jun, Youngsuk Certified Real Estate Agency'","'전영석'","'Jun, Youngsuk'","'82-2-581-3353'","'82-2-581-3319'","'강남구'","'강남구 테헤란로4길 46, 쌍용플래티넘밸류상가 126호(역삼동)'","'Gangnam-gu'","'#126, 46, Teheran-ro 4-gil, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'뉴서울 공인중개사사무소'","'Newseoul Certified Real Estate Agency'","'박일권'","'Park, Ilkwon'","'82-2-555-2882'","'82-2-555-7682'","'강남구'","'강남구 남부순환로 2927, 1층 105호(대치동)'","'Gangnam-gu'","'#35, 2921, Nambusunhwan-ro, Gangnam-gu,'",1,0,0,"null");
        insertAgencies("'한솔공인중개사사무소'","'Hansol Certified Real Estate Agency'","'서무석'","'Lee, Yusang'","'82-2-517-7558'","'82-2-545-9139'","'강남구'","'강남구 선릉로130길 57, 1층 (삼성동)'","'Gangnam-gu'","'#105, Daeyoung Sangga, 66, Ichon-ro 65ga-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'정상호공인중개사사무소'","'Chung`s Certified Real Estate Agency'","'정상호'","'Chung, Sangho'","'82-2-555-1002'","'82-2-555-3250'","'강남구'","'강남구 삼성로64길 5, 101호(대치동,현대아파트상가)'","'Gangnam-gu'","'#101, Hyundai Apartment Arcade, 5, Samseong-ro 64-gil, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'대신공인중개사사무소'","'Daeshin Certified Real Estate Agency'","'강일'","'Kang, El'","'82-2-564-0111'","'82-2-554-6400'","'강남구'","'강남구 선릉로 420, 1층1호(대치동)'","'Gangnam-gu'","'#1, 1F, 420, Seolleung­ro, Gangnam­gu, Seoul'",1,0,0,"null");
        insertAgencies("'탑 공인중개사사무소'","'Top Certified Real Estate Agency'","'심덕보'","'Lee, Kwangsoo'","'82-2-554-4151'","'82-2-2179-9354'","'강남구'","'강남구 역삼로67길 8, 101호(대치동)'","'Gangnam-gu'","'#103, Anam Sangga, 50, Seocho-daero 64-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'글로벌리공인중개사사무소'","'global Lee Certified Real Estate Agency'","'이영성'","'Lee, Youngsung'","'82-2-508-4749'","'82-2-508-4749'","'강남구'","'강남구 선릉로 420, 1층 1호(대치동)'","'Gangnam-gu'","'1F, 420, Seolleung-ro, Gangnam-gu, Seoul, Kprea'",1,0,0,"null");
        insertAgencies("'벡셀공인중개사사무소'","'bexel Certified Real Estate Agency'","'김윤희'","'Kim, Yoonhee'","'82-2-555-0885'","'82-2-2051-0885'","'강남구'","'강남구 봉은사로114길 42, 1층(삼성동)'","'Gangnam-gu'","'1F, 42, Bongeunsa-ro 114-gil, Gangnam-gu, Seoul, Korea'",1,0,0,"null");
        insertAgencies("'새방공인중개사사무소'","'Saebang Real Estate Agency'","'전영준'","'Jeon, Yeongjun'","'82-2-574-5858'","'82-2-574-5855'","'강남구'","'서울시 강남구 선릉로 52, 1층 103호(개포동)'","'Gangnam-gu'","'#103, 1F, 52, Seolleung-ro, Gangnam-gu, Seoul'",0,0,1,"null");
        insertAgencies("'대림공인중개사사무소'","'Daelim Certified Real Estate Agency'","'문창준'","'Changjoon Moon'","'82-2-566-4488'","'82-2-6455-4489'","'강남구'","'서울시 강남구 학동로 323, 1층(논현동)'","'Gangnam-gu'","'1F, 323, Hakdong-ro, Gangnam-gu, Seoul, Korea'",1,0,0,"null");
        insertAgencies("'미래공인중개사사무소'","'Mirae Certified Real Estate Agency'","'신영지'","'Shin, Youngji'","'82-2-3463-0111'","'82-2-571-5377'","'강남구'","'서울시 강남구 언주로 118, 우성캐릭터 199상가 103호(도곡동)'","'Gangnam-gu'","'#103, 118, Woosung-Character-Sangga, Eouju-ro, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'타팰공인중개사사무소'","'Topal Certified Real Estate Agency'","'김은정'","'Kim, Eunjung'","'82-2-571-9000'","'82-2-571-9923'","'강남구'","'강남구 언주로30길 21, 비동 103호(도곡동, 아카데미스위트상가)'","'Gangnam-gu'","'#103, Academy Sweet Building B Dong, 21 Eonju-ro 30-gil, Gangnam-gu,Seoul'",1,0,0,"null");
        insertAgencies("'대치래미안공인중개사사무소'","'Daechi Raemian Certified Real Estate Agency'","'박은숙'","'Park Eunsuk'","'82-2-558-0123'","'82-2-558-0112'","'강남구'","'강남구 도곡로78길 22, 102호(대치동)'","'Gangnam-gu'","'#102, 22 Dogok-ro 78-gil, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'렉슬공인중개사사무소'","'Rexle Certified Real Estate Agency'","'조장수'","'Cho Jangsoo'","'82-2-579-3335'","'82-2-562-3330'","'강남구'","'강남구 선릉로 225, 135호(도곡동, 도곡렉슬상가)'","'Gangnam-gu'","'#135, Dogok Rexle Arcade, 225 Seolleung-ro, Gangnam-gu, Seoul'",1,1,0,"null");
        insertAgencies("'강남역쉐르빌공인중개사사무소'","'Gangnamyeok Cherevil Real Estate Agency'","'홍선하'","'Hong, Seonha'","'82-2-538-7800'","'82-2-538-7810'","'강남구'","'강남구 강남대로 328, 1층 110호(역삼동, 강남역쉐르빌)'","'Gangnam-gu'","'#110, 1F, 328, Gangnam-daero, Gangnam-gu, Seoul'",0,0,1,"null");
        insertAgencies("'MVP빌딩부동산중개주식회사'","'MVP Building Real Estate Brokerage Co., Ltd.'","'박형방'","'Park, Hyungbang'","'82-2-594-5555'","'82-2-538-5001'","'강남구'","'강남구 역삼로 310, 1층 95호(역삼동)'","'Gangnam-gu'","'#95, 1F, 310, Yeoksam-ro, Gangnam-gu, Seoul, Korea'",1,0,0,"null");
        insertAgencies("'주식회사부동산중개법인투데이(투데이부동산중개주식회사)'","'Today Certified Real Estate Agency'","'한 훈'","'Han, Hun'","'82-2-549-8249'","'82-2-543-8289'","'강남구'","'강남구 도산대로 519, 201호'","'Gangnam-gu'","'#201, 519, Dosan-daero, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'서울프라퍼티공인중개사사무소'","'Seoul Property Certified Real Estate Agency'","'이승용'","'Lee, Seungyong'","'82-2-566-4088'","'82-2-6951-4088'","'강남구'","'강남구 테헤란로 423, 지202호'","'Gangnam-gu'","'#B202, 423, Teheran-ro, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'고시원창업공인중개사사무소'","'Ko`s Certified Real Estate Agency'","'고지훈'","'Ko, Jihoon'","'82-2-567-7942'","'-'","'강남구'","'강남구 도곡로 405, B102호(대치동, 삼환아르누보2)'","'Gangnam-gu'","'Unit B102, Samhwan Art Nouveau Building, 405, Dogok-ro, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'글로벌부동산컨설팅부동산중개법인(주)'","'Global Realty Consulting Real Estate Corporation'","'김응진'","'Kim, Eungjin'","'82-2-515-0073'","'82-2-515-0377'","'강남구'","'강남구 도산대로25길 43, 1층(신사동)'","'Gangnam-gu'","'1F, 43, Dosan-daero 25-gil, Gangnam-gu, Seoul'",1,0,1,"null");
        insertAgencies("'부일공인중개사사무소'","'Buil Real Estate Agency'","'이현주'","'Lee, Hyeonju'","'82-2-576-4411'","'82-2-577-4413'","'강남구'","'강남구 개포로 310, A동 130호(개포동)'","'Gangnam-gu'","'A-dong 130-ho, 310 Gaepo-ro, Gaepo 1-dongGangnam-gu, Seoul'",0,0,1,"null");
        insertAgencies("'알라딘에셋부동산중개'","'AladdinEset Real Estate Agency'","'서재형'","'Seo, Jaehyeong '","'82-2-556-8989'","'82-2-554-8959'","'강남구'","'강남구 논현로167길 13, 1층(신사동)'","'Gangnam-gu'","'570-4 Sinsa-dong, Gangnam-gu, Seoul'",0,0,1,"null");
        insertAgencies("'거여 공인중개사사무소'","'Geoyeo Real Estate Agency'","'이정훈'","'Lee , Jeonghun '","'82-2-404-9990'","'82-1515-430-9922'","'송파구'","'송파구 오금로54길 10, 111호(거여동,현대아파트상가 )'","'Gangnam-gu'","'#110, Hyundai APT Arcade, 10, Ogeum-ro 54-gil, Songpa-gu, Seoul, Republic of Korea'",0,0,1,"null");
        insertAgencies("'푸른 공인중개사사무소'","'Pureun Certified Real Estate Agency'","'배홍문'","'Bae, Hongmoon'","'82-2-408-3003'","'82-2-408-4833'","'송파구'","'서울시 송파구 송파대로32길 15, 상가동 124호'","'Songpa-gu'","'#124, Da-dong Sangga, 359, Songpa-daero 32-gil, Songpa-gu, Seoul'",1,0,0,"null");
        insertAgencies("'꼬레아 공인중개사사무소'","'Corea Certified Real Estate Agency'","'송원용'","'Song, Wonyong'","'82-2-400-4300'","'82-2-400-3830'","'송파구'","'송파구 송파구 문정로 11, 1층(문정동)'","'Songpa-gu'","'11, Munjeong-ro, Songpa-gu,Seoul'",1,0,0,"null");
        insertAgencies("'청담공인중개사사무소'","'Cheongdam Certified Real Estate Agency'","'구자동'","'Koo, Jadong'","'82-2-413-4488'","'82-2-3431-8944'","'송파구'","'송파구 올림픽로 203,130호(잠실동, 중앙상가)'","'Songpa-gu'","'#130, Jungang Arcade, 203, Olympic-ro, Songpa-gu, Seoul'",1,0,0,"null");
        insertAgencies("'희망공인중개사사무소'","'Hope Certified Real Estate Agency'","'한상준'","'Han, Sangjun'","'82-2-413-1114'","'82-2-416-4436'","'송파구'","'서울시 송파구 올림픽로 145, 상가동 1층1A1호'","'Songpa-gu'","'#1F,1A1ho, Ricenz Commercial Bldg., 145, Olympic-ro, Songpa-gu,Seoul'",1,0,0,"null");
        insertAgencies("'(주)한마루부동산중개법인'","'Hanmaru Realty Co.,Ltd.'","'김상헌'","'Kim, Sanghun'","'82-2-415-7244'","'82-2-415-0044'","'송파구'","'송파구 석촌호수로 268, 107호(송파동,경남레이크파크)'","'Songpa-gu'","'#107, Kyungnam Lake Park, 268, Seokchonhosu-ro, Songpa-gu, Seoul'",1,0,0,"null");
        insertAgencies("'뉴금성 공인중개사사무소'","'New goldstar Certified Real Estate Agency'","'구재우'","'Koo, Jaewoo'","'82-2-416-8888'","'82-2-424-4404'","'송파구'","'송파구 올림픽로 435, 148-1호(신천동, 파크리오)'","'Songpa-gu'","'#148-1, Parkrio, 435, Olympic-ro, Songpa-gu, Seoul'",1,0,0,"null");
        insertAgencies("'청운공인중개사사무소'","'Chungwoon Certified Real Estate Agency'","'김병수'","'Kim, byoungsoo'","'82-2-425-7222'","'82-2-423-4556'","'송파구'","'송파구 올림픽로 119, 1층 1B11호(잠실동,잠실파인애플상가)'","'Songpa-gu'","'119, Olympic-ro, Songpa-gu,Seoul,Korea, 1F1B11(Jamsil-dong,Fineapple Arcade)'",1,0,0,"null");
        insertAgencies("'한양공인중개사사무소'","'Hanyang Certified Real Estate Agency'","'이상일'","'Lee, sangil'","'82-2-408-1100'","'82-2-408-1140'","'송파구'","'송파구 위례성대로 158,113호(오금동,대림@)'","'Songpa-gu'","'158 ,Wiryeseong-daero Songpa-gu Seoul, Korea,113(Ogeum-dong, Daelim)'",1,0,0,"null");
        insertAgencies("'글로벌공인중개사사무소'","'Global Real Estate Agency'","'구자원'","'Koo, Jawon'","'82-2-400-5562'","'82-2-400-5567'","'송파구'","'송파구 새말로 62, 상가149호 (문정동, 송파푸르지오시티)'","'Songpa-gu'","'#149, 62 Saemal-ro, Songpa-gu, Seoul'",1,0,0,"null");
        insertAgencies("'로즈힐공인중개사사무소'","'Rosehill Certified Real Estate Agency'","'김정희'","'Kim, Junghee'","'82-2-477-0083'","'82-2-477-0983'","'송파구'","'송파구 토성로15길 6, 1층 (풍납동)'","'Songpa-gu'","'1F, 6, Toseong-ro 15-gil, Songpa-gu, Seoul'",1,0,0,"null");
        insertAgencies("'신성공인중개사사무소'","'Sinsung Certified Real Estate Agency'","'김 강'","'Jun,Yongwoo'","'82-2-424-7400'","'82-2-415-8524'","'송파구'","'송파구 백제고분로 426, 104호 (송파동)'","'Songpa-gu'","'#103, 45, Gajaeul­ro, Seodaemun­gu, Seoul'",1,0,0,"null");
        insertAgencies("'국내외부동산공인중개사사무소'","'Domestic and foreignCertified Real Estate Agency'","'홍강지'","'Hong, Kanggi'","'82-2-400-5679'","'82-2-400-5679'","'송파구'","'송파구 마천로7길 6, 상가106호 (오금동, 대림아파트)'","'Songpa-gu'","'#106, 6 Macheon-ro 7-gil, Songpa-gu, Seoul'",1,0,0,"null");
        insertAgencies("'이직선 공인중개사사무소'","'Lee Jik Sun Certified Real Estate Agency'","'이직선'","'Lee, Jiksun'","'82-2-470-0044'","'82-2-472-4455'","'강동구'","'강동구 명일로 108 (둔촌동)'","'Gangdong-gu'","'108, Myeongil-ro, Gangdong-gu, Seoul'",1,0,0,"null");
        insertAgencies("'삼성부동산 공인중개사사무소'","'Samsung Real Estate Agency'","'정호갑'","'Jung, Hogap'","'82-2-485-5263'","'82-2-483-0021'","'강동구'","'강동구 성내로9길 23 지층(성내동)'","'Gangdong-gu'","'23, Seongnae-ro 9-gil, Gangdong-gu, Seoul'",0,0,1,"null");
        insertAgencies("'한양공인중개사사무소'","'HanyangCertified Real Estate Agency'","'이현주'","'Lee, Hyonjoo'","'82-2-441-0776'","'82-2-441-8994'","'강동구'","'강동구 천호대로 219길 100(상일동)'","'Gangdong-gu'","'100, Cheonho-daero 219-gil, Gangdong-gu, Seoul'",1,0,0,"null");
        insertAgencies("'사직공인중개사사무소'","'Sajik Certified Real Esate Agency'","'정정희'","'Junghee Jung'","'82-2-738-8898'","'82-2-738-8898'","'종로구'","'종로구 우정국로2길 43, 6층(관철동)'","'Jongno-gu'","'6F, 43, Ujeongguk-ro 2-gil, Jongno-gu, Seoul'",1,1,1,"null");
        insertAgencies("'조인공인중개사사무소'","'Join Certified Real Estate Agency'","'안진연'","'Ahn Jinyeon'","'82-2-2234-0078'","'82-2-2256-7899'","'중구'","'중구 청계천로 318, 1층 나29(신당동)'","'Jung-gu'","'#Na29, 1F, 318, Cheonggyecheon-ro, Jung-gu, Seoul'",1,0,0,"null");
        insertAgencies("'제이리공인중개사사무소'","'JayLee Cerfified Real Estate Agency'","'장석규'","'Seokkyu Jang'","'82-2-909-2878'","'82-50-4427-7327'","'중구'","'중구 장충단로 263(을지로6가) 8층 49호'","'Jung-gu'","'#49, 8F, 263, Jangchungdan-ro, Jung-gu, Seoul'",1,0,0,"null");
        insertAgencies("'지구공인중개사사무소'","'Jeegoo Certified Real Estate Agency'","'이승영'","'Lee, Seung Young'","'82-2-792-2555'","'82-2-749-2555'","'용산구'","'용산구 회나무로 29(이태원동)'","'Yongsan-gu'","'29, Hoenamu-ro, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'위더스공인중개사사무소'","'With Us Certified Real Estate Agency'","'권영광'","'Kwon, Young Kwang'","'82-2-797-2000'","'82-2-797-8200'","'용산구'","'용산구 회나무로13길 12-1(이태원동) 3층'","'Yongsan-gu'","'3F, 12-1, Hoenamu-ro 13-gil, Yongsan-gu, Seoul'",1,0,0,"null");
        insertAgencies("'마이에이젼트서울부동산중개법인'","'MyAgent Seoul Real Estate Co., Ltd'","'조주연'","'Julian JY Joh'","'82-2-508-3668'","'82-2-508-3611'","'용산구'","'용산구 서빙고로67, 파크타워105동 상가 16호'","'Yongsan-gu'","'#16, Sangga, 105Dong, Yongsan Park Tower, 67, Seobinggo-ro, Yongsan-gu,Seoul'",1,1,1,"null");
        insertAgencies("'뉴드림공인중개사사무소'","'New Dream Real Estate Agency'","'임성아'","'Lim, Seonga'","'82-2-779-8282'","'82-2-779-8383'","'용산구'","'용산구 두텁바위로1길 49(후암동) 1층'","'Yongsan-gu'","'1F, 49, Duteopbawi-ro 1-gil, Yongsan-gu, Seoul'",0,0,0,"null");
        insertAgencies("'한강수부동산공인중개사사무소'","'Han-gangsu Real Estate Agency'","'정길식'","'Jung, Gilsik'","'82-2-792-9977'","'82-2-792-0224'","'용산구'","'용산구 이촌로 75길 22, 105호'","'Yongsan-gu'","'22, Ichon-ro 75-gil, Yongsan-gu, Seoul'",0,0,0,"null");
        insertAgencies("'중앙공인중개사사무소'","'Joong-Ang Certified Real Estate Agency'","'민선기'","'Min, SunGi'","'82-2-451-5002'","'82-2-457-6008'","'광진구'","'광진구 뚝섬로 676(자양동) 1층'","'Gwangjin-gu'","'1F, 676, Ttukseom-ro, Gwangjin-gu, seoul'",1,0,0,"null");
        insertAgencies("'부동산흐뭇공인중개사사무소'","'HMOOT Certified Real Estate Agency'","'황준석'","'Hwang Junsuk'","'82-2-2244-1100'","'82-2-2244-0033'","'동대문구'","'동대문구 장한로6길 8(장안동) 1층'","'Dongdaemun-gu'","'1F, 8, Janghan-ro 6-gil, Dongdaemun-gu, Seoul'",1,0,0,"null");
        insertAgencies("'열린공인중개사사무소'","'Yeollin Real Estate Agency'","'신대교'","'Shin, Daegyo'","'82-2-491-4545'","'82-2-491-4535'","'중랑구'","'중랑구 면목로 286, 1층(면목동)'","'Jungnang-gu'","'1F,  286 Myeonmok-ro, Jungnang-gu, Seoul '",0,1,0,"null");
        insertAgencies("'부동산다나와공인중개사사무소'","'Budongsandanawa Real Estate Agency'","'김효진'","'Kim, Hyojin'","'82-2-998-8945'","'82-2-999-8545'","'강북구'","'강북구 한천로 143길 11, 102호'","'Gangbuk-gu'","'#102, 11 Hancheon-ro 143-gil, Gangbuk-gu, Seoul'",0,0,0,"null");
        insertAgencies("'다원공인중개사사무소'","'Dawon Real Estate Agency'","'김광식'","'Kim, Kwangsik'","'82-2-354-8244'","'82-2-355-8244'","'은평구'","'은평구 응암로25길 3(응암동) 1층'","'Eunpyeong-gu'","'3 Eungam-ro 25-gil, Eunpyeong-gu, Seoul'",0,0,0,"null");
        insertAgencies("'김경희공인중개사사무소'","'Kim Gyeonghui Real Estate Agency'","'김경희'","'Kim, Gyeonghui'","'82-2-356-8863'","'82-2-356-8863'","'은평구'","'은평구 연서로35길 3(불광동) 1층'","'Eunpyeong-gu'","'3 Yeonseo-ro 35-gil, Eunpyeong-gu, Seoul'",0,0,0,"null");
        insertAgencies("'윤공인중개사사무소'","'Yun Real Estate Agency'","'이경숙'","'Lee, Gyeongsuk'","'82-2-393-5576'","'82-2-363-5576'","'서대문구'","'서대문구 신촌로 285(북아현동)'","'Seodaemun-gu'","'285 Sinchon-ro, Bukahyeon-dong, Seodaemun-gu, Seoul'",0,0,0,"null");
        insertAgencies("'믿음공인중개사사무소'","'Believe-In Certified Real Estate Agency'","'양동일'","'Yang,Dongil'","'82-2-365-2100'","'82-2-365-3003'","'서대문구'","'서대문구 신촌로 33길 16, 1층'","'Seodaemun-gu'","'1F, 16, Sinchon-ro 33-gil, Seodaemun-gu, Seoul'",1,0,0,"null");
        insertAgencies("'이화공인중개사사무소'","'Ewha Certified Real Estate Agency'","'최인철'","'Choi, Inchul'","'82-2-718-0088'","'82-2-718-0884'","'마포구'","'마포구 숭문길 224(염리동) 1층'","'Mapo-gu'","'1F, 224, Sungmun-gil, Mapo-gu, Seoul'",1,0,0,"null");
        insertAgencies("'신촌날개공인중개사사무소'","'Sinchonnalgae Real Estate Agency'","'이인환'","'Lee, Inhwan'","'82-2-717-2246'","'82-2-717-2247'","'마포구'","'마포구 신촌로20길 34(노고산동) 1층'","'Mapo-gu'","'34 Sinchon-ro 20-gil, Mapo-gu, Seoul'",0,0,0,"null");
        insertAgencies("'대화공인중개사사무소'","'Daehwa Certified Real Estate Agency'","'이재용'","'Lee, Jai Yong'","'82-2-2643-8844'","'82-2-2643-0094'","'양천구'","'양천구 신목로 37(신정동)'","'Yangcheon-gu'","'37, Sinmok-ro, Yangcheon-gu, Seoul'",1,0,0,"null");
        insertAgencies("'신서공인중개사사무소'","'Sinseo Real Estate Agency'","'장영걸'","'Jang, Yeonggeol'","'82-2-2699-4984'","'-'","'양천구'","'양천구 은행정로 55(신정동)'","'Yangcheon-gu'","'55 Eunhaengjeong-ro, Yangcheon-gu, Seoul'",0,0,0,"null");
        insertAgencies("'대방3층진공인중개사사무소'","'Daebang 3F JIN Certified Real Estate Agency'","'정항진'","'JUNG, Hang JIN'","'82-2-2664-4489'","'-'","'강서구'","'강서구 양천로 344, A동 318호(마곡동, 대방디엠시티오피스텔)'","'Gangseo-gu'","'A-318, 344, Yangcheon-ro, Gangseo-gu'",1,0,0,"null");
        insertAgencies("'두드림공인중개사사무소'","'DoDream Certified Real Estate Agency'","'서지유'","'Seo, Jiyou'","'82-2-865-1116'","'-'","'관악구'","'관악구 조원로 119, 1층'","'Gwanak-gu'","'1F, 119, Jowon-ro, Gwanak-gu, Seoul'",1,0,0,"null");
        insertAgencies("'리더스공인중개사사무소'","'Leader`s Certified Real Estate Agency'","'김현성'","'Kim, Hyun Sung'","'82-2-3461-4963'","'82-2-3461-4965'","'서초구'","'서초구 효령로 55길 15, 101호(서초동, 벨타워오피스텔)'","'Seocho-gu'","'#101, 15, Hyoryeong-ro 55-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'청구공인중개사사무소'","'Chung-Gu Certified Real Estate Agency'","'정해솔'","'Jung, Haesol'","'82-2-599-8800'","'82-2-599-8118'","'서초구'","'서초구 방배로 269, 101호(삼호상가)'","'Seocho-gu'","'#101, 269, Bangbae-ro, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'더행복한공인중개사사무소'","'The Blessed Certified Real Estate Agency'","'박찬수'","'Peter Park'","'82-2-532-5577'","'82-2-532-5578'","'서초구'","'서초구 신반포로15길 29, 155호'","'Seocho-gu'","'#155, 29, Sinbanpo-ro 15-gil, Seocho-gu, Seoul'",1,0,0,"'포르투갈어'");
        insertAgencies("'태양공인중개사사무소'","'Taeyang Real Estate Agency'","'장석우'","'Jang, Seokwoo'","'82-2-533-3100'","'82-2-3481-6856'","'서초구'","'서초구 서운로 207(서초동) 103호'","'Seocho-gu'","'#103, 207 Seoun-ro, Seocho-gu, Seoul'",0,0,0,"null");
        insertAgencies("'통통통공인중개사사무소'","'Tong Tong Tong Certified Real Estate Agency'","'이중구'","'Lee, Jung-Ku'","'82-2-522-0018'","'82-2-588-8946'","'서초구'","'서초구 남부순환로 315길 88-3, 1층'","'Seocho-gu'","'1F, 88-3, Nambusunhwan-ro 315gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'드림부동산중개경매'","'DREAM REAL ESTATE CONSULTING'","'이영욱'","'Lee Young Wook'","'82-2-597-0031'","'82-2-597-0038'","'서초구'","'서초구 효령로27길 28(방배동) 1층'","'Seocho-gu'","'1F, 28, Hyoryeong-ro 27-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'브로운스톤공인중개사사무소'","'Brownstone Certified Real Estate Agency'","'윤효상'","'Yoon, Hyo Sang'","'82-2-587-2885'","'82-2-587-5880'","'서초구'","'서초구 서초대로 334, 브라운스톤 314호'","'Seocho-gu'","'#314, Brownstone, 334, Seocho-daero, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'현대렉시온OK공인중개사사무소'","'Hyundai Rexion OK Certified Real Estate Agency'","'양성근'","'Yang, sung geun'","'82-2-2182-5088'","'82-2-2182-5089'","'서초구'","'서초구 강남대로 305, B110호'","'Seocho-gu'","'B110, Hyundai Rexion Building, 305, Gangnam-daero, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'로제공인중개사사무소'","'Rosee Certified Real Estate Agency'","'김창선'","'Kim, Changsun'","'82-2-599-3090'","'82-2-599-3092'","'서초구'","'서초구 서초대로27길 30(방배동) 1층'","'Seocho-gu'","'1F, 30, Seocho-daero 27-gil, Seocho-gu, Seoul'",1,0,0,"null");
        insertAgencies("'선릉역공인중개사사무소'","'Seolleung-station Certified Real Estate Agency'","'임정일'","'Lim, Jungil'","'82-2-557-0047'","'82-2-070-8848-0392'","'강남구'","'강남구 선릉로 86길 18(대치동)'","'Gangnam-gu'","'18, Seolleung-ro 86-gil, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'한미공인중개사사무소'","'Korus Certified Real Estate Agency'","'이채길'","'Lee, Chaegil'","'82-2-2183-5758'","'82-2-2183-5544'","'강남구'","'강남구 역삼로 121, 유성빌딩 3층'","'Gangnam-gu'","'3F, Yoosung bldg, 121, Yeoksam-ro, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'케이렉스공인중개사사무소'","'K.REX Certified Real Estate Office'","'손성구'","'Sohn, Seongkoo'","'82-2-540-6540'","'82-2-540-7540'","'강남구'","'강남구 학동로6길 10, 1층 102호'","'Gangnam-gu'","'#101, 1F, 10, Hakdong-ro, Gangnam-gu, Seoul'",1,0,0,"null");
        insertAgencies("'강일이음공인중개사사무소'","'kangil IEUM Certified Real Estate Office'","'이윤희'","'Lee, Yun-Hee'","'82-2-429-1400'","'-'","'강동구'","'강동구 아리수로 419, 104호(강일동)'","'Gangdong-gu'","'#104, 419, arisu-ro, gangdong-gu, Seoul'",1,0,0,"null");
    }

}
