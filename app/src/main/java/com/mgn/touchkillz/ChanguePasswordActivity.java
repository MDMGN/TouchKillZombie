package com.mgn.touchkillz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChanguePasswordActivity extends AppCompatActivity {
EditText eTcurrentPassword,eTnewPassword;
TextView tVtittleChanguePassword;
Button btnAcept,btnCancel;
DatabaseReference reference;
FirebaseAuth auth;
FirebaseUser user;
Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changue_password);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_actionbar_logo);

        eTcurrentPassword=findViewById(R.id.eTCurrentpassword);
        eTnewPassword=findViewById(R.id.eTNewPassword);
        tVtittleChanguePassword=findViewById(R.id.titleChanguePassword);
        btnAcept=findViewById(R.id.btnAcept);
        btnCancel=findViewById(R.id.btnCancel);

        reference= FirebaseDatabase.getInstance().getReference("Data Players");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        tf = Typeface.createFromAsset(ChanguePasswordActivity.this.getAssets(), "fonts/edosz.ttf");
        this.tVtittleChanguePassword.setTypeface(tf);
        this.btnCancel.setTypeface(tf);
        this.btnAcept.setTypeface(tf);

        btnAcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword=eTcurrentPassword.getText().toString().trim();
                String newPassword=eTnewPassword.getText().toString().trim();

                if(TextUtils.isEmpty(currentPassword)){
                    eTcurrentPassword.setError("El campo no debe estar vacío");
                    eTcurrentPassword.setFocusable(true);
                }else if(TextUtils.isEmpty(newPassword)){
                    eTnewPassword.setError("El campo no debe estar vacío");
                    eTnewPassword.setFocusable(true);
                }else if(currentPassword.length()<6 || newPassword.length()<6){
                    Toast.makeText(ChanguePasswordActivity.this, "6 carácteres como mínimo.", Toast.LENGTH_SHORT).show();
                }
                if(!TextUtils.isEmpty(currentPassword) && !TextUtils.isEmpty(newPassword) && currentPassword.length()>=6 && newPassword.length()>=6){
                    changuePasswordUser(currentPassword,newPassword);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChanguePasswordActivity.this,MenuActivity.class));
                finish();
            }
        });
    }

    private void changuePasswordUser(String currentPassword,String newPassword) {
        AuthCredential authCredential= EmailAuthProvider.getCredential(user.getEmail(),currentPassword);
        user.reauthenticate(authCredential)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPassword)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                HashMap<String,Object> result=new HashMap<>();
                                result.put("password",MD5(newPassword));
                                reference.child(user.getUid()).updateChildren(result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                                    auth.signOut();
                                    startActivity(new Intent(ChanguePasswordActivity.this,MainActivity.class));
                                Toast.makeText(ChanguePasswordActivity.this, "Se ha cerrado sesión", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChanguePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChanguePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}