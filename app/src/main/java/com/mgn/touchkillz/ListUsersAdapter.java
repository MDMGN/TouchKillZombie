package com.mgn.touchkillz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.Myholder>{
    private Context context;
    private List<User> listUser;

    public ListUsersAdapter(Context context, List<User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    //Inflamos el diseño
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.players_list,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        //Obtener datos del modelo
        String image=listUser.get(position).getImage();
        String name=listUser.get(position).getName();
        String country=listUser.get(position).getCountry();
        String date=listUser.get(position).getDate();
        int score=listUser.get(position).getScore();
        String zombie=String.valueOf(score);
        holder.playerName.setText(name);
        holder.countryPlayer.setText(country);
        holder.datePlayer.setText(date);
        holder.scorePlayer.setText(zombie);
        try{
            Picasso.get().load(image).into(holder.imagePlayer);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Myholder extends RecyclerView.ViewHolder{

        CircleImageView imagePlayer;
        TextView playerName,countryPlayer,datePlayer,scorePlayer;

        public Myholder(View itemView){
            super(itemView);
            imagePlayer=itemView.findViewById(R.id.imgProfile);
            playerName=itemView.findViewById(R.id.playerName);
            datePlayer=itemView.findViewById(R.id.playerDate);
            countryPlayer=itemView.findViewById(R.id.playerCountry);
            scorePlayer=itemView.findViewById(R.id.playerScore);
        }

    }
}