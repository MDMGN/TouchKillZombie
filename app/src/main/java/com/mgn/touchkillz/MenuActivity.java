package com.mgn.touchkillz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    Button btnSingOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        btnSingOut=findViewById(R.id.btnSingOut);
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

    @Override
    protected void onStart() {
        isUserLogin();
        super.onStart();
    }
}