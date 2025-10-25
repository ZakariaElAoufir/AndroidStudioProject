package com.example.project__controle_hopital.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.example.project__controle_hopital.Patient.PatientNotificationsActivity;
import com.example.project__controle_hopital.R;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Creneau;
import com.example.project__controle_hopital.entity.Notification;
import com.example.project__controle_hopital.entity.RendezVous;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RendezVousAdapter extends RecyclerView.Adapter<RendezVousAdapter.RendezVousViewHolder> {

    private final List<RendezVous> rendezVousList;
    private final Context context;
    private final AppDatabase db;
    private final String[] statuts = {"en_attente", "confirmé", "annulé", "terminé"};
    private final String[] statutsDisplay = {"En attente", "Confirmé", "Annulé", "Terminé"};

    public RendezVousAdapter(List<RendezVous> rendezVousList, Context context) {
        this.rendezVousList = rendezVousList;
        this.context = context;
        this.db = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public RendezVousViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rendezvous, parent, false);
        return new RendezVousViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RendezVousViewHolder holder, int position) {
        RendezVous rendezVous = rendezVousList.get(position);

        new Thread(() -> {
            Utilisateur patient = db.utilisateurDao().getById(rendezVous.patientId);
            Creneau creneau = db.creneauDao().getById(rendezVous.creneauId);
            Utilisateur medecin = UserSession.getInstance().getCurrentUser();

            ((Activity)context).runOnUiThread(() -> {
                if (patient != null) {
                    holder.tvPatient.setText("Patient: " + patient.prenom + " " + patient.nom);
                }
                if (creneau != null) {
                    holder.tvDate.setText("Date: " + formatDate(creneau.date));
                    holder.tvHeure.setText("Heure: " + creneau.heureDebut + " - " + creneau.heureFin);
                }

                // Configurer le Spinner des statuts
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_item,
                        statutsDisplay
                );
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinnerStatut.setAdapter(spinnerAdapter);

                // Sélectionner le statut actuel
                int selectedPosition = Arrays.asList(statuts).indexOf(rendezVous.statut);
                holder.spinnerStatut.setSelection(selectedPosition >= 0 ? selectedPosition : 0);

                // Gestion de l'envoi de notification
                holder.btnSendNotification.setOnClickListener(v -> {
                    if (patient != null && creneau != null && medecin != null) {
                        sendNotificationToPatient(patient, medecin, creneau);
                    } else {
                        Toast.makeText(context, "Impossible d'envoyer la notification", Toast.LENGTH_SHORT).show();
                    }
                });

                // Gestion du changement de statut
                holder.btnUpdate.setOnClickListener(v -> {
                    int selectedPos = holder.spinnerStatut.getSelectedItemPosition();
                    String newStatut = statuts[selectedPos];
                    updateRendezVousStatus(rendezVous, newStatut, position);
                });
            });
        }).start();
    }

    private void sendNotificationToPatient(Utilisateur patient, Utilisateur medecin, Creneau creneau) {
        new Thread(() -> {
            String title = "Rappel de rendez-vous";
            String message = String.format("Bonjour %s,\nVotre rendez-vous avec Dr. %s est prévu le %s à %s.\nMerci de votre confiance.",
                    patient.prenom,
                    medecin.nom,
                    formatDate(creneau.date),
                    creneau.heureDebut);

            // Créer et insérer la notification
            Notification notification = new Notification(
                    patient.id,
                    title,
                    message,
                    "rendezvous_rappel"
            );
            db.notificationDao().insert(notification);

            ((Activity)context).runOnUiThread(() -> {
                Toast.makeText(context, "Notification envoyée à " + patient.prenom, Toast.LENGTH_SHORT).show();
                createSystemNotification(title, message, patient.id);
            });
        }).start();
    }

    private void createSystemNotification(String title, String message, long userId) {
        Intent intent = new Intent(context, PatientNotificationsActivity.class); // Adaptez à votre activité
        intent.putExtra("user_id", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "RDV_CHANNEL")
                .setSmallIcon(R.drawable.ic_medical_services) // Icône à personnaliser
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } else {
            // Demander la permission si elle n'est pas accordée
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    101); // code de requête arbitraire
        }
    }

    private void updateRendezVousStatus(RendezVous rendezVous, String newStatut, int position) {
        new Thread(() -> {
            rendezVous.statut = newStatut;
            db.rendezVousDao().update(rendezVous);
            rendezVousList.get(position).statut = newStatut;

            ((Activity)context).runOnUiThread(() -> {
                Toast.makeText(context, "Statut mis à jour", Toast.LENGTH_SHORT).show();
                notifyItemChanged(position);
            });
        }).start();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateStr;
        }
    }

    @Override
    public int getItemCount() {
        return rendezVousList.size();
    }

    public void updateList(List<RendezVous> newList) {
        rendezVousList.clear();
        rendezVousList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class RendezVousViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatient, tvDate, tvHeure;
        Spinner spinnerStatut;
        Button btnUpdate, btnSendNotification;

        public RendezVousViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatient = itemView.findViewById(R.id.tvPatient);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHeure = itemView.findViewById(R.id.tvHeure);
            spinnerStatut = itemView.findViewById(R.id.spinnerStatut);
            btnUpdate = itemView.findViewById(R.id.btnUpdateStatus);
            btnSendNotification = itemView.findViewById(R.id.btnSendNotification);
        }
    }
}