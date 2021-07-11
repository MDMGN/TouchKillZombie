package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnLogin,btnSingin;
    ImageView handzombie;
    Typeface tf;
    public static Activity fa;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fa = this;
        btnLogin=findViewById(R.id.btninLogin);
        btnSingin=findViewById(R.id.btnSingin);
        tf = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/edosz.ttf");
        this.btnLogin.setTypeface(tf);
        this.btnSingin.setTypeface(tf);

        handzombie=findViewById(R.id.handzImagen);
        count=0;
        animationHandZombie(handzombie);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        btnSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        handzombie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count==7){
                    Toast.makeText(MainActivity.this, "Touch Kill - Zombie", Toast.LENGTH_SHORT).show();
                    count=0;
                }
            }
        });
    }
    private void animationHandZombie(ImageView imagen){
        Handler handler=new Handler();
        Runnable run=new Runnable() {
            @Override
            public void run() {
                AlphaAnimation animation1 = new AlphaAnimation(0.5f, 1.0f); //here is a bit of animation for ya ;)
                animation1.setDuration(500);
                animation1.setStartOffset(200); //time for that color effect
                animation1.setFillAfter(true);
                imagen.startAnimation(animation1);
                imagen.setImageResource(R.drawable.hand_zombie);
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(run,1000);
    }
}