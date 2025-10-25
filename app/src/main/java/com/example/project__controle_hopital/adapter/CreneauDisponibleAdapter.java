package com.example.project__controle_hopital.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import com.example.project__controle_hopital.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.dao.RendezVousDao;
import com.example.project__controle_hopital.entity.Creneau;
import com.example.project__controle_hopital.entity.RendezVous;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


public class CreneauDisponibleAdapter extends RecyclerView.Adapter<CreneauDisponibleAdapter.ViewHolder> {
    private List<Creneau> creneauList;
    private Map<Long, Utilisateur> medecinMap;
    private Context context;

    public CreneauDisponibleAdapter(List<Creneau> creneauList, Map<Long, Utilisateur> medecinMap, Context context) {
        this.creneauList = creneauList;
        this.medecinMap = medecinMap;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_creneau_disponible, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Creneau creneau = creneauList.get(position);
        Utilisateur medecin = medecinMap.get(creneau.medecinId);

        holder.tvMedecin.setText(medecin != null ?
                "Dr. " + medecin.prenom + " " + medecin.nom : "Médecin inconnu");
        holder.tvDate.setText("Date: " + creneau.date);
        holder.tvHeure.setText("Heure: " + creneau.heureDebut + " - " + creneau.heureFin);

        holder.btnPrendreRdv.setOnClickListener(v -> handleRdvClick(creneau, medecin));
    }

    private void handleRdvClick(Creneau creneau, Utilisateur medecin) {
        Utilisateur patient = UserSession.getInstance().getCurrentUser();
        if (patient == null || !"Patient".equals(patient.role)) {
            Toast.makeText(context, "Action réservée aux patients", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_appointment, null);
        TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        EditText etMotif = dialogView.findViewById(R.id.etMotif);

        tvMessage.setText("Confirmer avec Dr. " + medecin.nom + " le " + creneau.date + " à " + creneau.heureDebut + "?");

        new AlertDialog.Builder(context)
                .setTitle("Confirmer le rendez-vous")
                .setView(dialogView)
                .setPositiveButton("Confirmer", (dialog, which) -> {
                    String motif = etMotif.getText().toString().trim();
                    createAppointment(creneau, patient, medecin, motif);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void createAppointment(Creneau creneau, Utilisateur patient, Utilisateur medecin, String motif) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);

            // Vérifier dernière disponibilité
            Creneau currentCreneau = db.creneauDao().getById(creneau.id);
            if (!currentCreneau.disponible) {
                ((Activity)context).runOnUiThread(() ->
                        Toast.makeText(context, "Créneau déjà pris", Toast.LENGTH_SHORT).show());
                return;
            }

            // Créer RDV avec motif
            RendezVous rdv = new RendezVous();
            rdv.patientId = patient.id;
            rdv.medecinId = medecin.id;
            rdv.creneauId = creneau.id;
            rdv.statut = "confirmé";
            rdv.motif = motif.isEmpty() ? null : motif;

            // Transaction
            db.runInTransaction(() -> {
                db.rendezVousDao().insert(rdv);
                creneau.disponible = false;
                db.creneauDao().update(creneau);
            });

            ((Activity)context).runOnUiThread(() -> {
                creneauList.remove(creneau);
                notifyDataSetChanged();
                String confirmation = "Rendez-vous confirmé!";
                if (motif != null && !motif.isEmpty()) {
                    confirmation += "\nMotif: " + motif;
                }
                Toast.makeText(context, confirmation, Toast.LENGTH_LONG).show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return creneauList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedecin, tvDate, tvHeure;
        Button btnPrendreRdv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedecin = itemView.findViewById(R.id.tvMedecin);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHeure = itemView.findViewById(R.id.tvHeure);
            btnPrendreRdv = itemView.findViewById(R.id.btnPrendreRdv);
        }
    }
}
