package com.mgn.touchkillz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListScoreUsersActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    RecyclerView recycler;
    ListUsersAdapter adapter;
    List<User> listUsers;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_score_users);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Puntuaciones");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        linearLayoutManager=new LinearLayoutManager(this);
        auth=FirebaseAuth.getInstance();
        recycler=findViewById(R.id.recyclerView);
        linearLayoutManager.setReverseLayout(true);/*ORDENA A-Z*/
        linearLayoutManager.setStackFromEnd(true);/*EMPIEZA DESDE ARRIBA SIN TENER DELIZ*/
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);
       listUsers= new ArrayList<>();
       getAllUsers();

    }
    private void getAllUsers() {
       FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();/*Obtener el usuario actual*/
      DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Data Players");
        reference.orderByChild("zombies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUsers.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    //DEL MODELO DE USUARIOS
                    User user=ds.getValue(User.class);
                    /*if(!user.getUid().equals(user.getUid())){
                        listUsers.add(user);
                    }*/
                    listUsers.add(user);
                }
                adapter=new ListUsersAdapter(ListScoreUsersActivity.this,listUsers);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}