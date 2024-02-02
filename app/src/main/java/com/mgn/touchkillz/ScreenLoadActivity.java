package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ScreenLoadActivity extends AppCompatActivity {
    private String userName;
    private String dificult;
    private DrawView vista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vista=new DrawView(this);
        setContentView(vista);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        Bundle intent=getIntent().getExtras();
        dificult=intent.getString("dificult");
        userName=intent.getString("name");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(ScreenLoadActivity.this,GameActivity.class);
                intent.putExtra("name",userName);
                intent.putExtra("dificult",dificult);
                startActivity(intent);
                finish();
            }
        },3200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        vista.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vista.pause();
    }
}