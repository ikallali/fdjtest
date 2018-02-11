package fr.ikallali.fdjtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import fr.ikallali.fdjtest.news.NewsActivity;

import static fr.ikallali.fdjtest.Settings.SPLASH_TIME_OUT;


public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashScreenActivity.this, NewsActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }




}
