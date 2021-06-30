package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    Button btnSingOut,btnPlay,btnRecordall,btnInfo;
    TextView nameUser,emailUser,recordUser,uidUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        nameUser=findViewById(R.id.nameUser);
        emailUser=findViewById(R.id.emailUser);
        recordUser=findViewById(R.id.recorUser);
        uidUser=findViewById(R.id.uidUser);
        btnPlay=findViewById(R.id.btnPlay);
        btnRecordall=findViewById(R.id.btnRecordall);
        btnInfo=findViewById(R.id.btnInfo);
        btnSingOut=findViewById(R.id.btnSingOut);
       btnPlay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(MenuActivity.this, "Jugar", Toast.LENGTH_SHORT).show();
           }
       });
        btnSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singOutUser();
            }
        });
    }
    public void isUserLogin(){
        if(user != null){
            Toast.makeText(MenuActivity.this, "Jugador en línea", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(MenuActivity.this,MainActivity.class));
            finish();
        }
    }
    public void singOutUser(){
        auth.signOut();
        startActivity(new Intent(MenuActivity.this,MainActivity.class));
        Toast.makeText(MenuActivity.this, "Cerrastes sessión.", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void setDataUser(){
    }
    @Override
    protected void onStart() {
        isUserLogin();
        super.onStart();
    }
}