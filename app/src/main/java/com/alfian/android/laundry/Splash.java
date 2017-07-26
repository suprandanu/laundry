package com.alfian.android.laundry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.alfian.android.laundry.volley.SessionManager;

public class Splash extends AppCompatActivity {
    SessionManager sm;
    private static int splashInterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub
                sm = new SessionManager(Splash.this);
                if (sm.getStatus().equals("loged")){
                    startActivity(new Intent(Splash.this, Beranda.class));
                    this.finish();
                }else {
                    startActivity(new Intent(Splash.this, Login.class));
                    this.finish();
                }
                Splash.this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);
    }
}
