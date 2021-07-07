package com.mgn.touchkillz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    public static final int TEXTO_RESPUESTA =1;
    String name,points;
    TextView pointsUser,nameUser,tVtime,tVheight,tVwidth;
    ImageView pj;
    LinearLayoutCompat lnLayout;
    int lnLayoutHeight;
    int widthScreen;
    int heightScreen;
    int count;
    int timeMili=700;
    boolean pclick=false,click;
    Random random;
    boolean gameOver;
    Intent intent;
    public static Activity ga;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_actionbar_zombie);
        /*SpannableString actionbarTitle = new SpannableString("¡Cerebros!");
        actionbarTitle.setSpan(new TypefaceSpan(this,"fonts/edosz.ttf"), 0, actionbarTitle.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(actionbarTitle);*/
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"red\">" +getString("¡Cerebros!") + "</font>"));

        ga=this;
        tVheight=findViewById(R.id.heightscreen);
        tVwidth=findViewById(R.id.widthscreen);
        lnLayout=findViewById(R.id.lnLayout);
        pj=findViewById(R.id.pj);
        pointsUser=findViewById(R.id.tVpoint);
        nameUser=findViewById(R.id.tVname);
        tVtime=findViewById(R.id.tVtime);
        Bundle intent=getIntent().getExtras();
        mPreferences = getSharedPreferences(MenuActivity.sharedPrefFile, MODE_PRIVATE);
        name=mPreferences.getString("name",null);
        points="0";
        if(isOnline() ) {
            name=intent.getString("name");
            points=intent.getString("recordpoint");
        }
        pointsUser.setText(points);
       nameUser.setText(name);
        lnLayout.post(new Runnable(){
            public void run(){
                lnLayoutHeight = lnLayout.getHeight();
            }
        });
        startGame();
      pj.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(!gameOver) {
                   if(!pclick) {
                        actionPJ("pjzombie");
                       pclick=true;
                   }else{
                       click=true;
                       String pjimagen=v.getTag().toString();
                       actionPJ(pjimagen);
                   }
               }
           }
       });
    }
    private void startGame(){
        gameOver=false;
        click=false;
        count=0;
        screen();
        countBack();
        countDownTimer.start();
        tVtime.setText(R.string.time_game);
        fastMoveZombie();
    }
    private void screen(){
        Display display=getWindowManager().getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        widthScreen=point.x;
        heightScreen=point.y;
        tVwidth.setText(String.valueOf(widthScreen));
        tVheight.setText(String.valueOf(heightScreen));
        random=new Random();
    }
    private void moverZombie(){
        int min=0;
        int maxX=widthScreen-pj.getWidth();
        int maxY=(heightScreen-lnLayoutHeight)-pj.getHeight();
        int randomX=random.nextInt(((maxX-min)+1)+min);
        int randomY=random.nextInt(((maxY-min)+1)+min);
        Log.d("randomX",randomX+"");
        Log.d("randomY",randomY+"");
        pj.setX(randomX);
        pj.setY(randomY);
    }
    private void countBack() {
            countDownTimer=new CountDownTimer(15000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(!gameOver) {
                        long secondsBack = millisUntilFinished / 1000;
                        Log.d("time",secondsBack+"");
                        tVtime.setText(secondsBack + " S");
                    }
                }

                @Override
                public void onFinish() {
                    if(!gameOver) {
                        tVtime.setText(R.string.time_game);
                        GameOver(null);
                    }
                }
            };

    }

    public String randomPJ(){
        String pjImages[] = {"pjzombie","pjzombie","pjzombiecoco","pjhuman", "pjzombie"};
        int nrandom = (int) Math.floor(Math.random() * pjImages.length);
        return pjImages[nrandom];
    }
    private void actionPJ(String pjImage){
        int idImageKill = GameActivity.this.getResources().getIdentifier(pjImage+"_kill", "drawable",GameActivity.this.getPackageName());
        pj.setImageResource(idImageKill);
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f); //here is a bit of animation for ya ;)
        animation.setDuration(500);
        animation.setStartOffset(300); //time for that color effect
        animation.setFillAfter(true);
        pj.startAnimation(animation);
        switch(pjImage){
            case "pjhuman":
                count=0;
                tVtime.setText(R.string.time_game);
                GameOver("yes");
                break;
            default:
                count++;
                click=false;
                break;
        }
        pointsUser.setText(String.valueOf(count));
    }
    private void GameOver(String lose){
        intent=new Intent(GameActivity.this, GameOverActivity.class);
        countDownTimer.cancel();
        gameOver=true;
        intent.putExtra("name",name);
        intent.putExtra("killzombie",count);
        intent.putExtra("lose",lose);
        startActivityForResult(intent,1);
    }
    private void fastMoveZombie() {
        if(!gameOver) {
            Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                        if(!gameOver) {
                                moverZombie();
                            if(!click) {
                                String pjimagen=randomPJ();
                                   int idImage = GameActivity.this.getResources().getIdentifier(pjimagen, "drawable", GameActivity.this.getPackageName());
                                    pj.setImageResource(idImage);
                                    pj.setTag(pjimagen);
                                    handler.postDelayed(this, timeMili);
                            }
                        }
                    }
            };
            handler.postDelayed(run, timeMili);
            click=false;
        }
    }
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(GameActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt("count", count);
        preferencesEditor.putString("name", name);
        preferencesEditor.apply();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TEXTO_RESPUESTA){
            if(resultCode == RESULT_OK){
                String answer = data.getStringExtra(GameOverActivity.EXTRA_REPLY);
                Log.d("answer",answer);
                if(answer!="restart"){
                    startGame();
                }
            }
        }
    }
}