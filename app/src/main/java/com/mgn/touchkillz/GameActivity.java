package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    String name,points;
    TextView pointsUser,nameUser,tVtime,tVheight,tVwidth;
    ImageView pjzombie;
    int widthScreen;
    int heightScreen;
    int anchoPantalla;
    int altoPantalla;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tVheight=findViewById(R.id.heightscreen);
        tVwidth=findViewById(R.id.widthscreen);
        pointsUser=findViewById(R.id.tVpoint);
        nameUser=findViewById(R.id.tVname);
        tVtime=findViewById(R.id.tVtime);
        pjzombie=findViewById(R.id.pjzombie);
        Bundle intent=getIntent().getExtras();
        name=intent.getString("name");
        points=intent.getString("recordpoint");
        pointsUser.setText(points);
       nameUser.setText(name);
       screen();
       pjzombie.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               count++;
               pointsUser.setText(String.valueOf(count));
               pjzombie.setImageResource(R.drawable.zombieaplastado);
               AlphaAnimation animation1 = new AlphaAnimation(0.5f, 1.0f); //here is a bit of animation for ya ;)
               animation1.setDuration(500);
               animation1.setStartOffset(300); //time for that color effect
               pjzombie.startAnimation(animation1);
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       pjzombie.setImageResource(R.drawable.zombie);
                   }
               },500);
           }
       });
    }
    private void screen(){
        Display display=getWindowManager().getDefaultDisplay();
        Point poitn=new Point();
        display.getSize(poitn);
        widthScreen=poitn.x;
        heightScreen=poitn.y;
        tVwidth.setText(String.valueOf(widthScreen));
        tVheight.setText(String.valueOf(heightScreen));
    }
    private void movimiento(){
        int min=0;
        int maxX=anchoPantalla-pjzombie.getWidth();
        int maxY=altoPantalla-pjzombie.getHeight();
    }
}