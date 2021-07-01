package com.mgn.touchkillz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText eTemail,eTpassword;
    Button btnLogin;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eTemail=findViewById(R.id.email);
        eTpassword=findViewById(R.id.password);
        btnLogin=findViewById(R.id.btnLogin);
        auth=FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=eTemail.getText().toString();
                String password=eTpassword.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    eTemail.setError("Correo electrónico no válido");
                    eTemail.setFocusable(true);
                }else if(password.length()<6){
                    eTpassword.setError("6 carácteres como mínimo.");
                    eTpassword.setFocusable(true);
                }else{
                    PlayerLogin(email,password);
                }
            }
        });
    }

    private void PlayerLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=auth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,MenuActivity.class));
                            assert user!=null; //Si el usuario no es null.
                            Toast.makeText(LoginActivity.this, "BIENVENIDO(A) "+user.getEmail()+".", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}