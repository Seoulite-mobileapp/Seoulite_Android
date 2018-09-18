package com.seoulite_android.seoulite;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements NavigationHost,
        DistrictSelectionFragment.OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.app_bar) Toolbar mToolbar;
    private NavigationIconClickListener mNavIconClickListener;

    View mNavIconView;

    // Fragments
    private static DistrictSelectionFragment mDistrictSelectionFragment;
    private static AgencyByDistrictFragment mAgencyByDistrictFragment;
    private static LivingInfoFragment mLivingInfoFragment;
    private static FavoritesFragment mFavoritesFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mNavIconView = getToolbarNavigationIcon(mToolbar);

        setSupportActionBar(mToolbar);
        mNavIconClickListener = new NavigationIconClickListener(this,
                findViewById(R.id.main_container), null,
                getResources().getDrawable(R.drawable.ic_hamburger_menu),
                getResources().getDrawable(R.drawable.ic_x_shape));
        mToolbar.setNavigationOnClickListener(mNavIconClickListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, new HomeFragment())
                    .commit();
        }
        // Set cut corner background for API 23+
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            findViewById(R.id.main_container).setBackground(getDrawable(R.drawable.toolbar_shape));
//        }

        // Initialize Fragments
        mDistrictSelectionFragment = new DistrictSelectionFragment();
        mAgencyByDistrictFragment = new AgencyByDistrictFragment();
        mLivingInfoFragment = new LivingInfoFragment();
        mFavoritesFragment = new FavoritesFragment();

        Log.d(TAG, "onCreate: ");

        //Start DB
        DbHelper dbHelper = new DbHelper(getApplicationContext());
//        //insert into agencies 호출
//        if(dbHelper.isTableReadyForInsert("AGENCIES")) {
//            dbHelper.insertAgenciesTotal();
//        }
//        Log.i("agencies records",dbHelper.countRecords("AGENCIES")+"");
//        if(dbHelper.isTableReadyForInsert("DISTRICTS")) {
//            dbHelper.insertDistrictsTotal();
//        }
//        Log.i("districts records",dbHelper.countRecords("DISTRICTS")+"");
        //End DB
    }

    @Override
    public void onBackPressed() {
        if (mNavIconClickListener.getBackdropShown()) {
            mNavIconClickListener.onClick(mNavIconView);
        } else if (getCurrentFragment() instanceof HomeFragment){
            super.onBackPressed();
        } else {
            replaceFragment(new HomeFragment(), false);
        }
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.main_container);
    }

    private View getToolbarNavigationIcon(Toolbar toolbar) {
        boolean hadContentDescription = TextUtils.isEmpty(toolbar.getNavigationContentDescription());
        String contentDescription = !hadContentDescription ? toolbar.getNavigationContentDescription().toString() : "navigationIcon";
        toolbar.setNavigationContentDescription(contentDescription);
        ArrayList<View> potentialView = new ArrayList<>();
        toolbar.findViewsWithText(potentialView, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        View navIcon = null;
        if (potentialView.size() > 0) {
            navIcon = potentialView.get(0);
        }
        if (hadContentDescription) toolbar.setNavigationContentDescription(null);
        return navIcon;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @OnClick(R.id.btn_backdrop_home)
    public void moveToHomeFragment() {
        mNavIconClickListener.onClick(mNavIconView);
        replaceFragment(new HomeFragment(), false);
    }

    @OnClick(R.id.btn_backdrop_search)
    public void moveToDistrictSelectionFragment() {
        mNavIconClickListener.onClick(mNavIconView);
        replaceFragment(mDistrictSelectionFragment, false);
    }

    @OnClick(R.id.btn_backdrop_living_info)
    public void moveToLivingInfoFragment() {
        mNavIconClickListener.onClick(mNavIconView);
        replaceFragment(mLivingInfoFragment, false);
    }

    @OnClick(R.id.btn_backdrop_favorites)
    public void moveToFavoritesFragemnt() {
        mNavIconClickListener.onClick(mNavIconView);
        replaceFragment(mFavoritesFragment, false);
    }
//
//    TODO: implements onOptionsItemSelected - Warnings: HomeFragment Timer!

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.nav_camera:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MessageFragment()).commit();
//                break;
//            case R.id.nav_gallery:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ChatFragment()).commit();
//                break;
//            case R.id.nav_slideshow:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ProfileFragment()).commit();
//                break;
//            case R.id.nav_manage:
//                Toast.makeText(this, "nav_message clicked", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_share:
//                Toast.makeText(this, "nav_share clicked", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_send:
//                Toast.makeText(this, "nav_send clicked", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    // Pass the district name from DistrictSelectionFragment to AgencyByDistrictFragment
    @Override
    public void onFragmentInteraction(String districtName) {
        if (mAgencyByDistrictFragment != null) {
            Log.d(TAG, "onFragmentInteraction: mAgencyByDistrictFragment is not null");
            Bundle args = new Bundle();
            args.putString("districtName", districtName);
            mAgencyByDistrictFragment.setArguments(args);
            replaceFragment(mAgencyByDistrictFragment, true);

        } else {
            Log.d(TAG, "onFragmentInteraction: mAgencyBydistrictFragment is null");
        }
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment);
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
