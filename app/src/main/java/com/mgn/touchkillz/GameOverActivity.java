package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {
    String path="fonts/edosz.ttf",name;
    Typeface tf;
    TextView endGameTv,tVkillZombiesText,tVkillZombiesScore,tVPlayerUserName;
    Button btnPlayAgain,btnShowScore,btnGoMenu;
    Bundle bundle;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
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
                GameActivity.ga.finish();
                Intent intent=new Intent(GameOverActivity.this,GameActivity.class);
                startActivity(intent);
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
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}