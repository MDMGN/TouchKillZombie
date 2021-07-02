package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    String name,points;
    TextView pointsUser,nameUser,tVtime,tVheight,tVwidth;
    ImageView pjzombie;
    int widthScreen;
    int heightScreen;
    int count;
    Random random;
    boolean gameOver=false;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tVheight=findViewById(R.id.heightscreen);
        tVwidth=findViewById(R.id.widthscreen);
        pjzombie=findViewById(R.id.pjzombie);
        dialog=new Dialog(GameActivity.this);
        pointsUser=findViewById(R.id.tVpoint);
        nameUser=findViewById(R.id.tVname);
        tVtime=findViewById(R.id.tVtime);
        Bundle intent=getIntent().getExtras();
        name=intent.getString("name");
        points=intent.getString("recordpoint");
        pointsUser.setText(points);
       nameUser.setText(name);
       screen();
       countBack();
       pjzombie.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(!gameOver) {
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
                               moverZombie();
                           }
                       }, 100);
                   }
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
        random=new Random();
    }
    private void moverZombie(){
        int min=0;
        int maxX=widthScreen-pjzombie.getWidth();
        int maxY=heightScreen-pjzombie.getHeight();
        int randomX=random.nextInt(((maxX-min)+1)+min);
        int randomY=random.nextInt(((maxY-min)+1)+min);
        pjzombie.setTranslationX(randomX);
        pjzombie.setY(randomY);
    }
    private void countBack(){
        new CountDownTimer(10000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsBack=millisUntilFinished/1000;
                tVtime.setText(secondsBack+" S");
            }

            @Override
            public void onFinish() {
                tVtime.setText(R.string.time_game);
                gameOver=true;
                mensaggeGameOver();
            }
        }.start();
    }

    private void mensaggeGameOver() {
        String path="fonts/edosz.ttf";
        Typeface tf=Typeface.createFromAsset(GameActivity.this.getAssets(),path);
        TextView endGameTv,tVkillZombiesText,tVkillZombiesScore,tVPlayerUserName;
        Button btnPlayAgain,btnShowScore,btnGoMenu;

        dialog.setContentView(R.layout.game_over);

        endGameTv=dialog.findViewById(R.id.endGameText);
        tVkillZombiesText=dialog.findViewById(R.id.killZombiesText);
        tVkillZombiesScore=dialog.findViewById(R.id.killZombies);
        tVPlayerUserName=dialog.findViewById(R.id.tVplayerUsername);
        btnPlayAgain=dialog.findViewById(R.id.playAgain);
        btnShowScore=dialog.findViewById(R.id.showScore);
        btnGoMenu=dialog.findViewById(R.id.goMenu);
        tVPlayerUserName.setText(name);
        tVPlayerUserName.setTypeface(tf);
        String zombies=String.valueOf(count);
        tVkillZombiesScore.setText(zombies+" zombies.");
        tVkillZombiesScore.setTypeface(tf);
        endGameTv.setTypeface(tf);
        tVkillZombiesText.setTypeface(tf);
        btnPlayAgain.setTypeface(tf);
        btnShowScore.setTypeface(tf);
        btnGoMenu.setTypeface(tf);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setCancelable(false);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent=getIntent();
                finish();
                startActivity(intent);
            }
        });
        btnGoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this,MenuActivity.class));
                finish();
            }
        });
    }
    public void randomPJ(){
        String bgImages[] = {"pjZombie", "pjHuman"};
        Handler handler=new Handler();
        Runnable run=new Runnable() {
            @Override
            public void run() {
                int nrandom = (int) Math.floor(Math.random() * bgImages.length);
                switch (bgImages[nrandom]){
                    case "pjZombie":
                        pjzombie=findViewById(R.id.pjzombie);
                        break;
                    case "pjHuman":
                        pjzombie=findViewById(R.id.pjhuman);
                }
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(run,1000);
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}