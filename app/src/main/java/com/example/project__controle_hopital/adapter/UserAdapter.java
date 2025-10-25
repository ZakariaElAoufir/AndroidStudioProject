package com.example.project__controle_hopital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context context;
    private List<Utilisateur> utilisateurList;
    private AdapterListener adapterListener;

    public UserAdapter(Context context, AdapterListener adapterListener){
        this.context = context;
        this.adapterListener = adapterListener;
        utilisateurList = new ArrayList<>();
    }

    public void addUtilisateur(Utilisateur utilisateur){
        utilisateurList.add(utilisateur);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.utilisateur_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Utilisateur utilisateur = utilisateurList.get(position);
        holder.nom.setText(utilisateur.getNom());
        holder.prenom.setText(utilisateur.getPrenom());
        holder.email.setText(utilisateur.getEmail());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.OnUpdate(utilisateur.getId(),position);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.OnDelete(utilisateur.getId(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return utilisateurList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nom,prenom,email;
        private ImageView update,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom);
            prenom = itemView.findViewById(R.id.prenom);
            email = itemView.findViewById(R.id.email);
            update = itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);
        }
    }
    public void removeUtilisateurAt(int position) {
        if (position >= 0 && position < utilisateurList.size()) {
            utilisateurList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, utilisateurList.size());
        }
    }

    public void clear() {
        utilisateurList.clear();
        notifyDataSetChanged();
    }


}
