package com.example.project__controle_hopital.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project__controle_hopital.Login;
import com.example.project__controle_hopital.Medecin.Profile_Medecin;
import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Utilisateur;

public class Dashboard_Patient extends AppCompatActivity {

    private TextView badgeNotifications;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_patient);

        // Initialisation de la base de données
        db = AppDatabase.getInstance(this);

        // Récupérer l'utilisateur connecté
        Utilisateur currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            TextView tvNom = findViewById(R.id.tvNom);
            tvNom.setText("Bienvenue, " + currentUser.prenom + " " + currentUser.nom);
        }

        // Initialiser le badge
        badgeNotifications = findViewById(R.id.badgeNotifications);

        // Gérer l'action du bouton "Consulter le Profil"
        Button btnConsultProfile = findViewById(R.id.btnConsultProfile);
        btnConsultProfile.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard_Patient.this, Profile_Patient.class));
        });

        // Gérer l'action du bouton "Prendre un Rendez-vous"
        Button btnMakeAppointment = findViewById(R.id.btnMakeAppointment);
        btnMakeAppointment.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard_Patient.this, AppointmentActivity.class));
        });

        // Gérer l'action du bouton "Historique Rendez-vous"
        Button btnAppointmentHistory = findViewById(R.id.btnAppointmentHistory);
        btnAppointmentHistory.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard_Patient.this, AppointmentHistoryActivity.class));
        });

        // Gérer l'action du bouton "Notifications"
        Button btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard_Patient.this, PatientNotificationsActivity.class));
        });

        // Gérer l'action de déconnexion
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            UserSession.getInstance().setCurrentUser(null);
            startActivity(new Intent(Dashboard_Patient.this, Login.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUnreadNotifications();
    }

    private void checkUnreadNotifications() {
        Utilisateur currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null) return;

        db.notificationDao().getUnreadCount(currentUser.id).observe(this, count -> {
            if (count != null && count > 0) {
                badgeNotifications.setVisibility(View.VISIBLE);
                badgeNotifications.setText(String.valueOf(count));

                // Option: changer l'icône pour indiquer des nouvelles notifications
                Button btnNotifications = findViewById(R.id.btnNotifications);
                btnNotifications.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_notifications_active, 0, 0, 0);
            } else {
                badgeNotifications.setVisibility(View.GONE);

                // Revenir à l'icône normale
                Button btnNotifications = findViewById(R.id.btnNotifications);
                btnNotifications.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_notifications, 0, 0, 0);
            }
        });
    }
}
