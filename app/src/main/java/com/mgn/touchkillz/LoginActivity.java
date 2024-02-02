package com.mgn.touchkillz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText eTemail,eTpassword;
    TextView titleLogin;
    ImageView bgz;
    Typeface tf;
    Button btnLogin;
    FirebaseAuth auth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        bgz=findViewById(R.id.bgz);
        eTemail=findViewById(R.id.email);
        eTpassword=findViewById(R.id.password);
        btnLogin=findViewById(R.id.btnLogin);
        titleLogin=findViewById(R.id.titleLogin);
        tf = Typeface.createFromAsset(LoginActivity.this.getAssets(), "fonts/edosz.ttf");
        this.titleLogin.setTypeface(tf);
        this.btnLogin.setTypeface(tf);

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
                }else if(!isOnline()){
                    Toast.makeText(LoginActivity.this, "¡Compruebe su conexión a internet!", Toast.LENGTH_SHORT).show();
                }else{
                    PlayerLogin(email,password);
                }
            }
        });
        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Iniciando..");
        progressDialog.setCancelable(false);
        changeImagen();
    }

    private void PlayerLogin(String email, String password) {
        progressDialog.show();
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user=auth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,MenuActivity.class));
                            assert user!=null; //Si el usuario no es null.
                            Toast.makeText(LoginActivity.this, "BIENVENIDO(A) "+user.getEmail()+".", Toast.LENGTH_SHORT).show();
                            MainActivity.fa.finish();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changeImagen(){
        String bgImages[] = {"bgz0", "bgz1","bgz2","bgz3"};
        Handler handler=new Handler();
        Runnable run=new Runnable() {
            @Override
            public void run() {
                int nrandom = (int) Math.floor(Math.random() * bgImages.length);
                int idImage = LoginActivity.this.getResources().getIdentifier(bgImages[nrandom], "drawable", LoginActivity.this.getPackageName());
                AlphaAnimation animation1 = new AlphaAnimation(0.5f, 1.0f); //here is a bit of animation for ya ;)
                animation1.setDuration(1000);
                animation1.setStartOffset(300); //time for that color effect
                animation1.setFillAfter(true);
                bgz.startAnimation(animation1);
                bgz.setImageResource(idImage);
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(run,1000);
    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(LoginActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}