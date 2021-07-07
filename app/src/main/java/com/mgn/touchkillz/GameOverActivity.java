package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class GameOverActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY =
            "com.mgn.touchkill.extra.REPLY";
    String path="fonts/edosz.ttf",name;
    Typeface tf;
    TextView endGameTv,tVkillZombiesText,tVkillZombiesScore,tVPlayerUserName;
    Button btnPlayAgain,btnShowScore,btnGoMenu;
    Bundle bundle;
    LinearLayoutCompat lnLayout;
    int count;
    int widthScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        lnLayout=findViewById(R.id.ln_Layout);
        widhtScreen();
        this.setFinishOnTouchOutside(false);
        tf= Typeface.createFromAsset(GameOverActivity.this.getAssets(),path);
        endGameTv=findViewById(R.id.endGameText);
        tVkillZombiesText=findViewById(R.id.killZombiesText);
        tVkillZombiesScore=findViewById(R.id.killZombies);
        tVPlayerUserName=findViewById(R.id.tVplayerUsername);
        btnPlayAgain=findViewById(R.id.playAgain);
        btnShowScore=findViewById(R.id.showScore);
        btnGoMenu=findViewById(R.id.goMenu);

        bundle=getIntent().getExtras();
        name=bundle.getString("name");
        tVPlayerUserName.setText(name);
        tVPlayerUserName.setTypeface(tf);

        count=bundle.getInt("killzombie");
        String zombies=String.valueOf(count);
        tVkillZombiesScore.setText(zombies+" zombies.");
        tVkillZombiesScore.setTypeface(tf);

        isLosePlayer();
        endGameTv.setTypeface(tf);

        tVkillZombiesText.setTypeface(tf);
        btnPlayAgain.setTypeface(tf);
        btnShowScore.setTypeface(tf);
        btnGoMenu.setTypeface(tf);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = "restart";
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                intent.putExtra(EXTRA_REPLY, answer);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btnGoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.ga.finish();
                finish();
            }
        });
    }
    public void isLosePlayer(){
        String lose=bundle.getString("lose");
        if(lose != null){
            endGameTv.setText(R.string.lose_game);
        }
    }
    public void widhtScreen(){
        Display display=getWindowManager().getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        widthScreen=point.x;
        lnLayout.setMinimumWidth((widthScreen*8/10));
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}