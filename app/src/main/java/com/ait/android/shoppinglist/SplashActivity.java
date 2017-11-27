package com.ait.android.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private Timer myTimer;
    private boolean enabled;

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myTimer = new Timer();
        myTimer.schedule(new MyTimerTask(), 3000, 3000);

        final LinearLayout layoutContent = (LinearLayout) findViewById(R.id.layoutContent);

        Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.show_anim);
        layoutContent.startAnimation(anim);

    }

    @Override
    protected void onStop() {
        super.onStop();
        myTimer.cancel();
    }
}
