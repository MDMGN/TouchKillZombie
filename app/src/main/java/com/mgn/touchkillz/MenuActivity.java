package com.mgn.touchkillz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    Button btnSingOut,btnPlay,btnRecordall,btnInfo;
    TextView nameUser,recordUser,recorText;
    ImageView imgpf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase=firebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference("Data players");
        nameUser=findViewById(R.id.nameUser);
        recordUser=findViewById(R.id.recorUser);
        //font zombie.TTF.
        recorText=findViewById(R.id.recordText);
        String path="fonts/edosz.ttf";
        Typeface tf=Typeface.createFromAsset(MenuActivity.this.getAssets(),path);
        imgpf=findViewById(R.id.imgpf);
        btnPlay=findViewById(R.id.btnPlay);
        btnRecordall=findViewById(R.id.btnRecordall);
        btnInfo=findViewById(R.id.btnInfo);
        btnSingOut=findViewById(R.id.btnSingOut);
        recorText.setTypeface(tf);
        nameUser.setTypeface(tf);
        recordUser.setTypeface(tf);
        btnPlay.setTypeface(tf);
        btnInfo.setTypeface(tf);
        btnRecordall.setTypeface(tf);
        btnSingOut.setTypeface(tf);
       btnPlay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MenuActivity.this,GameActivity.class);
               String name=nameUser.getText().toString();
               String recordpoint=recordUser.getText().toString();
               intent.putExtra("name",name);
               intent.putExtra("recordpoint",recordpoint);
               startActivity(intent);
               finish();
           }
       });
        btnRecordall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Puntuaciones", Toast.LENGTH_SHORT).show();
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Acerca de", Toast.LENGTH_SHORT).show();
            }
        });
        btnSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickShowAlert(v);
            }
        });
        changeImagen();
    }
    public void isUserLogin(){
        if(user != null){
            consultUsers();
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
    public void onClickShowAlert(View view){
        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(MenuActivity.this);
        myAlertBuilder.setTitle(R.string.tittle_alert);
        myAlertBuilder.setMessage(R.string.descript_alert);
        myAlertBuilder.setPositiveButton(R.string.confirm_alert, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button.
                       singOutUser();
                    }
                });
        myAlertBuilder.setNegativeButton(R.string.cancel_alert, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the dialog.
                    }
                });
        myAlertBuilder.show();
    }

    private void changeImagen(){
        String bgImages[] = {"imgpf", "imgpf2","imgpf3"};
        Handler handler=new Handler();
        Runnable run=new Runnable() {
            @Override
            public void run() {
                int nrandom = (int) Math.floor(Math.random() * bgImages.length);
                int idImage = MenuActivity.this.getResources().getIdentifier(bgImages[nrandom], "drawable", MenuActivity.this.getPackageName());
                imgpf.setImageResource(idImage);
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(run,1000);
    }
    @Override
    protected void onStart() {
        isUserLogin();
        super.onStart();
    }
    private void consultUsers(){
        //Consulta
        Query query=reference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String zombies=ds.child("zombies").getValue().toString();
                    String name=ds.child("name").getValue().toString();
                    nameUser.setText(name);
                    recordUser.setText(zombies);
                    btnPlay.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}