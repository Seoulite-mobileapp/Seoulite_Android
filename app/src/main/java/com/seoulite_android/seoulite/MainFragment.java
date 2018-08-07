package com.seoulite_android.seoulite;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment {

    @BindView(R.id.image_viewpager) ViewPager mViewPager;
    @BindView(R.id.indicator) TabLayout mIndicator;

    List<Drawable> mImages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        mImages = new ArrayList<>();
        // TODO: Temp Code, Needs Refactoring
        mImages.add(getResources().getDrawable(R.drawable.sample_1));
        mImages.add(getResources().getDrawable(R.drawable.sample_2));
        mImages.add(getResources().getDrawable(R.drawable.sample_3));

        mViewPager.setAdapter(new SliderAdapter(getContext(), mImages));
        mIndicator.setupWithViewPager(mViewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        return view;
    }


    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            ((MainActivity) getActivity()).runOnUiThread(new Runnable() {
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
