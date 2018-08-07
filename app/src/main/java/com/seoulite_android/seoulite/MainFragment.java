package com.seoulite_android.seoulite;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainFragment extends Fragment {

    // ViewPager widgets
    @BindView(R.id.image_viewpager) ViewPager mViewPager;
    @BindView(R.id.indicator) TabLayout mIndicator;

    Timer mTimer;

    List<Drawable> mImages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        // Create images array
        mImages = new ArrayList<>();
        // TODO: Temp Code, Needs Refactoring
        mImages.add(getResources().getDrawable(R.drawable.sample_1));
        mImages.add(getResources().getDrawable(R.drawable.sample_2));
        mImages.add(getResources().getDrawable(R.drawable.sample_3));

        mViewPager.setAdapter(new SliderAdapter(getContext(), mImages));
        mIndicator.setupWithViewPager(mViewPager, true);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Auto-slide timer
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

    }

    @OnClick(R.id.btn_main_search)
    void moveToDistrictSelectionFragment() {
        ((MainActivity)getActivity()).replaceFragment(new DistrictSelectionFragment(), false);
        mTimer.cancel();
    }

    @OnClick(R.id.btn_main_living_info)
    void moveToLivingInfoFragment() {
        ((MainActivity)getActivity()).replaceFragment(new LivingInfoFragment(), false);
        mTimer.cancel();
    }

    @OnClick(R.id.btn_main_favorites)
    void moveToFavoritesFragment() {
        ((MainActivity)getActivity()).replaceFragment(new FavoritesFragment(), false);
        mTimer.cancel();
    }


    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mViewPager.getCurrentItem() < mImages.size() - 1) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    } else {
                        mViewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }


}
