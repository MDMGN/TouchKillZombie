package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin=findViewById(R.id.btnLogin);
        btnSingin=findViewById(R.id.btnSingin);
        handzombie=findViewById(R.id.handzImagen);
        animationHandZombie(handzombie);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Inicia sesión",Toast.LENGTH_LONG).show();
            }
        });
        btnSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
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