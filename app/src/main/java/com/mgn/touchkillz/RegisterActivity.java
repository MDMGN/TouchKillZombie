package com.mgn.touchkillz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
public class RegisterActivity extends AppCompatActivity {
    EditText eTname,eTcountry,eTemail,eTpassword;
    TextView eTdate,titleRegister;
    Button btnRegister;
    Typeface tf;
    FirebaseAuth auth; //Firebase Autenticación.
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_actionbar_logo);

        eTname=findViewById(R.id.name);
        eTcountry=findViewById(R.id.country);
        eTemail=findViewById(R.id.email);
        eTpassword=findViewById(R.id.password);
        eTdate=findViewById(R.id.date);
        titleRegister=findViewById(R.id.titleRegister);
        btnRegister=findViewById(R.id.btnRegister);
        tf = Typeface.createFromAsset(RegisterActivity.this.getAssets(), "fonts/edosz.ttf");
        this.titleRegister.setTypeface(tf);
        this.btnRegister.setTypeface(tf);

        progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Registrando..");

        auth=FirebaseAuth.getInstance();
        //Obteniendo valor para la fecha.
        Date date=new Date();
        SimpleDateFormat dateformat=new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String fecha=dateformat.format(date);
        eTdate.setText(fecha);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=eTname.getText().toString();
                name=name.trim();
                String country=eTcountry.getText().toString();
                String email=eTemail.getText().toString();
                String password=eTpassword.getText().toString();
                if(name.length()==0 || name.length()<2){
                    eTname.setError("3 carácteres como mínimo.");
                    eTname.setFocusable(true);
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    eTemail.setError("Correo electrónico no válido");
                    eTemail.setFocusable(true);
                }else if(password.length()<6){
                    eTpassword.setError("6 carácteres como mínimo.");
                    eTpassword.setFocusable(true);
                }else if(!isOnline()){
                    Toast.makeText(RegisterActivity.this, "¡Compruebe su conexión a internet!", Toast.LENGTH_SHORT).show();
                }else{
                    PlayerRegister(name,country,email,password);
                }
            }
        });
    }

    private void PlayerRegister(String name,String country,String email,String password)  {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            int point = 0;
                            assert user != null; //Si el usuario no es nulo.
                            String uid = user.getUid();
                            String date = eTdate.getText().toString();
                            //Crear onjeto de clave y valor para los datos del usuario.
                            HashMap<Object, Object> playerData = new HashMap<>();
                            playerData.put("uid", uid);
                            playerData.put("name", name);
                            playerData.put("country", country);
                            playerData.put("image", "");
                            playerData.put("email", email);
                            playerData.put("password", MD5(password));
                            playerData.put("date", date);
                            playerData.put("zombies", point);
                            //Enviar el objeto de datos a Firebase.
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Data Players");
                            reference.child(uid).setValue(playerData);
                            startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
                            Toast.makeText(RegisterActivity.this, "¡Registro realizado con éxito!.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "¡No se pudo completar el registro.!", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                //Error al registrar player.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("error message",e.getMessage());
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
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(RegisterActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}