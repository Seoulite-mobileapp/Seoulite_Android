package com.seoulite_android.seoulite;


import android.content.res.Configuration;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements NavigationHost,
        DistrictSelectionFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    public static FragmentManager fragmentManager;

    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.app_bar) Toolbar mToolbar;

    ActionBarDrawerToggle mToggle;


    // Fragments
    private HomeFragment mHomeFragment;
    private DistrictSelectionFragment mDistrictSelectionFragment;
    private AgencyByDistrictFragment mAgencyByDistrictFragment;
    private LivingInfoFragment mLivingInfoFragment;
    private FavoritesFragment mFavoritesFragment;

    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.main_container, new HomeFragment())
                    .commit();
        }
        setNavigationViewListener();

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

    }



    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.main_container);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem search = menu.findItem(R.id.app_bar_search);
        mSearchView = (android.support.v7.widget.SearchView)search.getActionView();
        mSearchView.setQueryHint("Search");

        // TODO: Activate Search Action


        return true;
    }



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
                fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, fragment);
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                replaceFragment(new HomeFragment(), false);
                break;
            case R.id.nav_agency:
                replaceFragment(new DistrictSelectionFragment(), true);
                break;
            case R.id.nav_district_info:
                replaceFragment(new DistrictSelectionLivingInfoFragment(), true);
                break;
            case R.id.nav_favorites:
                replaceFragment(new FavoritesFragment(), true);
                break;
            case R.id.nav_languages:
                replaceFragment(new LanguageSettingFragment(), true);
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
