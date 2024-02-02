package com.mgn.touchkillz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    String name,imagenProfile;
    public static String score;

    Button btnSingOut,btnPlay,btnRecordall,btnInfo;
    TextView nameUser,recordUser,recorText;
    CircleImageView imgProfile;
    ImageView imgpf;
    Dialog dialog,dialogDificult;
    Typeface tf;
    MediaPlayer soundClick;

    private StorageReference storageReference;
    private String pathStorage="pictures_profiles/*";
    /*Permisos*/
    private static  final int KEY_STORAGE=200;
    private static  final int SELECT_IMAGE=300;
    /*MATRICES*/
    private String [] permissionStorage;
    private Uri imageUri;
    private String  profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference("Data Players");

        profile="image";
        dialog=new Dialog(MenuActivity.this);
        dialogDificult=new Dialog(MenuActivity.this);
        imgpf=findViewById(R.id.imgpf);
        imgProfile=findViewById(R.id.imgProfile);
        storageReference= FirebaseStorage.getInstance().getReference();
        permissionStorage=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        nameUser=findViewById(R.id.nameUser);
        recordUser=findViewById(R.id.recorUser);

        recorText=findViewById(R.id.recordText);
        String path="fonts/edosz.ttf";
        Typeface tf=Typeface.createFromAsset(MenuActivity.this.getAssets(),path);
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
        isUserLogin();

       btnPlay.setOnClickListener(v -> selectDificult());

       btnRecordall.setOnClickListener(e ->{
            Intent intent=new Intent(MenuActivity.this,ListScoreUsersActivity.class);
            startActivity(intent);
        });

        btnInfo.setOnClickListener(v-> about());

        btnSingOut.setOnClickListener(v->onClickShowAlert(v));

        imgProfile.setOnClickListener(v->UpdateImageProfile());

        imgpf.setOnClickListener(v->UpdateImageProfile());

        initPlaySound();
    }

    public void initPlaySound() {
        int sound=R.raw.raw_menu;
        soundClick= MediaPlayer.create(this,sound);
        soundClick.setLooping(true);
    }
    private void about() {
        TextView tVdeveloped,tVdev;
        Button btnOk;
        dialog.setContentView(R.layout.about_dialog);
        tVdeveloped=dialog.findViewById(R.id.developedText);
        tVdev=dialog.findViewById(R.id.devText);
        btnOk=dialog.findViewById(R.id.btnOk);

        tf = Typeface.createFromAsset(MenuActivity.this.getAssets(), "fonts/edosz.ttf");
        tVdeveloped.setTypeface(tf);
        tVdev.setTypeface(tf);
        btnOk.setTypeface(tf);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void isUserLogin(){
        if(user != null){
            consultUsers();
            //Almacenar datos si no hay conexión de manera local.
            DatabaseReference myRef=FirebaseDatabase.getInstance().getReference("people");
            myRef.keepSynced(true);
            if(isOnline()) {
                Toast.makeText(MenuActivity.this, "Jugador en línea", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Jugador sin conexión", Toast.LENGTH_SHORT).show();
            }
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
        super.onStart();
       // isUserLogin();
    }
    private void consultUsers(){
        //Consulta
        Query query=reference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    score=ds.child("zombies").getValue().toString();
                    name=ds.child("name").getValue().toString();
                    nameUser.setText(name);
                    recordUser.setText(score);
                    btnPlay.setEnabled(true);
                    try {
                        imagenProfile=ds.child("image").getValue().toString();
                        Picasso.get().load(imagenProfile).into(imgProfile);
                        imgpf.setVisibility(View.GONE);
                        imgProfile.setVisibility(View.VISIBLE);

                    }catch (Exception e){
                        imgpf.setVisibility(View.VISIBLE);
                        changeImagen();
                        Toast.makeText(MenuActivity.this, "No hay Imagen cargada para tu perfil.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(MenuActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    @Override
    protected void onPause() {
        soundClick.pause();
        super.onPause();
    }
    @Override
    protected void onResume(){
        soundClick.start();
        super.onResume();
    }
    private void updateDataUser(){
            String[] option = {"Nombre","Imagen de perfil","País","Contraseña","Cancelar"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
            builder.setItems(option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        updateDataUsers("name","Nombre");
                    }else if (i == 1) {
                        //profile = "image";
                        UpdateImageProfile();
                    }else if (i == 2) {
                        updateDataUsers("country","País");
                    }else if (i == 3) {
                       startActivity(new Intent(MenuActivity.this,ChanguePasswordActivity.class));
                       finish();
                    }else if (i == 4) {
                            dialogInterface.cancel();
                    }
                }
            });
            builder.create().show();
    }
    private void updateDataUsers(String key,String text) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Editar "+text);
        LinearLayoutCompat linearLayoutCompat=new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText=new EditText(this);
        editText.setHint(text);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value=editText.getText().toString().trim();
                HashMap<String,Object> result=new HashMap<>();
                result.put(key,value);
                reference.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MenuActivity.this, text+" actualizado correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MenuActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MenuActivity.this, text+" no modificado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void UpdateImageProfile() {
        String[] opciones={"Galeria","Zombies"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   switch(which){
                        case 0:
                                //Seleccionar Galeria.
                                if(!isPermissionStorage()){
                                    SolicitarPermissionStorage();
                                }else{
                                    selectImageGalery();
                                }
                                break;
                       case 1:
                           imgProfile.setVisibility(View.GONE);
                           imgpf.setVisibility(View.VISIBLE);
                           changeImagen();
                }

            }
        });
        builder.create().show();
    }
//PERMISOS DE ALMACENAMIENTO EN TIEMPO DE EJECUCION
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void SolicitarPermissionStorage() {
        requestPermissions(permissionStorage,KEY_STORAGE);
    }
    //COMPRUEBA SI LOS PERMISOS DE ALMACENAMIENTO ESTAN HABILITADOS.
    private boolean isPermissionStorage() {
        boolean result= ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
//SE LLAMA EL USUARIO  O JUGADOR PRESIONA PERMITIR O DENEGAR EN EL CUADRO DE DIALOG.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  KEY_STORAGE:
                if(grantResults.length>0){
                    boolean writeStorage=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(writeStorage){
                        //PERMISO HABILITADO
                        selectImageGalery();
                    }else{
                        Toast.makeText(this, "No se obtuvo el permiso.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //SE LLAMA CUANDO EL USUARIO YA ELEGIDO LA IMAGEN.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_IMAGE){
                imageUri=data.getData();
                uploadPicture(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//Actulizar y subir imagen a la base de datos de FireBase.
    private void uploadPicture(Uri imageUri) {
        String pathFileandname=pathStorage+""+profile+user.getUid();
        StorageReference storageReferenceUpload=storageReference.child(pathFileandname);
        storageReferenceUpload.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUri=uriTask.getResult();
                            if(uriTask.isSuccessful()){
                                HashMap<String,Object> result=new HashMap<>();
                                result.put(profile,downloadUri.toString());
                                reference.child(user.getUid()).updateChildren(result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MenuActivity.this, "Imagen de perfil actualizada.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MenuActivity.this, "HA OCURRIDO UN ERROR.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(MenuActivity.this, "Ha ocurrido un error al subir la imagen.", Toast.LENGTH_SHORT).show();
                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                 Toast.makeText(MenuActivity.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Abrir galeria.
    private void selectImageGalery() {
        if(isOnline()){
            Intent intentGallery=new Intent(Intent.ACTION_PICK);
            intentGallery.setType("image/*");
            startActivityForResult(intentGallery,SELECT_IMAGE);
        }else {
            Toast.makeText(this, "Necesitas conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }
    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option:
                updateDataUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void selectDificult(){
            Button btnHard,btnNormal,btnEasy;
            dialogDificult.setContentView(R.layout.select_dificult);
            btnHard=dialogDificult.findViewById(R.id.btnHard);
            btnNormal=dialogDificult.findViewById(R.id.btnNormal);
            btnEasy=dialogDificult.findViewById(R.id.btnEasy);

            tf = Typeface.createFromAsset(MenuActivity.this.getAssets(), "fonts/edosz.ttf");

            btnHard.setTypeface(tf);
            btnNormal.setTypeface(tf);
            btnEasy.setTypeface(tf);

            btnHard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MenuActivity.this,ScreenLoadActivity.class);
                    String name=nameUser.getText().toString();
                    intent.putExtra("name",name);
                    intent.putExtra("dificult","hard");
                    startActivity(intent);
                    dialogDificult.dismiss();
                }
            });
        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this,ScreenLoadActivity.class);
                String name=nameUser.getText().toString();
                intent.putExtra("name",name);
                intent.putExtra("dificult","normal");
                startActivity(intent);
                dialogDificult.dismiss();
            }
        });
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this,ScreenLoadActivity.class);
                String name=nameUser.getText().toString();
                intent.putExtra("name",name);
                intent.putExtra("dificult","easy");
                startActivity(intent);
                dialogDificult.dismiss();
            }
        });
        dialogDificult.show();
    }
}