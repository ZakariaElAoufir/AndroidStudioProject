package com.example.project__controle_hopital.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.project__controle_hopital.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Creneau;
import com.example.project__controle_hopital.entity.RendezVous;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<RendezVous> appointments;
    private Context context;
    private AppDatabase db;

    public AppointmentAdapter(List<RendezVous> appointments, Context context) {
        this.appointments = appointments != null ? appointments : new ArrayList<>();
        this.context = context;
        this.db = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        RendezVous appointment = appointments.get(position);

        // Charger les données en arrière-plan
        Executors.newSingleThreadExecutor().execute(() -> {
            Utilisateur medecin = db.utilisateurDao().getById(appointment.medecinId);
            Creneau creneau = db.creneauDao().getById(appointment.creneauId);

            ((Activity)context).runOnUiThread(() -> {
                if (medecin != null) {
                    holder.tvMedecin.setText("Dr. " + medecin.prenom + " " + medecin.nom);
                }
                if (creneau != null) {
                    holder.tvDate.setText("Date: " + formatDate(creneau.date));
                    holder.tvTime.setText("Heure: " + creneau.heureDebut);
                }
                holder.tvStatus.setText("Statut: " + formatStatus(appointment.statut));
                holder.tvMotif.setText("Motif: " + (appointment.motif != null ? appointment.motif : "Non précisé"));
            });
        });
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRENCH);
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateStr;
        }
    }

    private String formatStatus(String status) {
        switch(status) {
            case "en_attente": return "En attente";
            case "confirmé": return "Confirmé";
            case "annulé": return "Annulé";
            case "terminé": return "Terminé";
            default: return status;
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void updateAppointments(List<RendezVous> newAppointments) {
        this.appointments = newAppointments != null ? newAppointments : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedecin, tvDate, tvTime, tvStatus, tvMotif;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedecin = itemView.findViewById(R.id.tvMedecin);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvMotif = itemView.findViewById(R.id.tvMotif);
        }
    }
}