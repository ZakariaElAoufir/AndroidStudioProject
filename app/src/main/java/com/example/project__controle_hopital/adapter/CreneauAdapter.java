package com.example.project__controle_hopital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project__controle_hopital.R;

import com.example.project__controle_hopital.entity.Creneau;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreneauAdapter extends RecyclerView.Adapter<CreneauAdapter.CreneauViewHolder> {

    private List<Creneau> creneauList;
    private final OnCreneauClickListener listener;
    private final SimpleDateFormat displayDateFormat;
    private final SimpleDateFormat dbDateFormat;

    public CreneauAdapter(List<Creneau> creneauList, OnCreneauClickListener listener) {
        this.creneauList = creneauList != null ? creneauList : new ArrayList<>();
        this.listener = listener;
        this.displayDateFormat = new SimpleDateFormat("EEE d MMM yyyy", Locale.FRENCH);
        this.dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
    }

    @NonNull
    @Override
    public CreneauViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_creneau, parent, false);
        return new CreneauViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreneauViewHolder holder, int position) {
        Creneau creneau = creneauList.get(position);
        Context context = holder.itemView.getContext();

        try {
            Date date = dbDateFormat.parse(creneau.date);
            holder.tvDate.setText(displayDateFormat.format(date));
        } catch (ParseException e) {
            holder.tvDate.setText(creneau.date);
        }

        holder.tvHeure.setText(String.format(Locale.FRENCH, "%s - %s",
                creneau.heureDebut, creneau.heureFin));

        if (creneau.disponible) {
            holder.tvDisponible.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(context, R.drawable.ic_check), null, null, null);
            holder.tvDisponible.setText(" Disponible");
            holder.tvDisponible.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.tvDisponible.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(context, R.drawable.ic_close), null, null, null);
            holder.tvDisponible.setText(" Réservé");
            holder.tvDisponible.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        holder.editCreneau.setOnClickListener(v -> listener.onEditClick(creneau));
        holder.deleteCreneau.setOnClickListener(v -> {
            if (!creneau.disponible) {
                Toast.makeText(context,
                        "Impossible de supprimer un créneau réservé",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            listener.onDeleteClick(creneau);
        });
    }

    @Override
    public int getItemCount() {
        return creneauList.size();
    }

    public void updateList(List<Creneau> newList) {
        this.creneauList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class CreneauViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvHeure, tvDisponible;
        ImageButton editCreneau, deleteCreneau;

        public CreneauViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHeure = itemView.findViewById(R.id.tvHeure);
            tvDisponible = itemView.findViewById(R.id.tvDisponible);
            editCreneau = itemView.findViewById(R.id.editCreneau);
            deleteCreneau = itemView.findViewById(R.id.deleteCreneau);
        }
    }

    public interface OnCreneauClickListener {
        void onEditClick(Creneau creneau);
        void onDeleteClick(Creneau creneau);
    }
}



