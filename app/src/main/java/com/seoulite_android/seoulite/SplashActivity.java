package com.seoulite_android.seoulite;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView mManIcon;
    private ImageView mWomanIcon;
    private TextView mAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mManIcon = findViewById(R.id.man_image);
        mWomanIcon = findViewById(R.id.woman_image);
        mAppName = findViewById(R.id.appname_text);

        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        mManIcon.startAnimation(splashAnim);
        mWomanIcon.startAnimation(splashAnim);
        mAppName.startAnimation(splashAnim);

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        };

        timer.start();
    }
}
