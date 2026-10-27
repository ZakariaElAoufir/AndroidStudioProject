package com.example.project__controle_hopital.Medecin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project__controle_hopital.Login;
import com.example.project__controle_hopital.R;

public class Dashboard_Medecin extends AppCompatActivity {

    Button btnProfile, btnCreneaux, btnRendezVous, btnDeconnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_medecin);

        // Initialisation des boutons
        btnProfile = findViewById(R.id.btnProfile);
        btnCreneaux = findViewById(R.id.btnCreneaux);
        btnRendezVous = findViewById(R.id.btnRendezVous);
        btnDeconnexion = findViewById(R.id.btnDeconnexion);

        // Gestion du clic sur "Mon Profil"
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard_Medecin.this, Profile_Medecin.class);
            startActivity(intent);
        });

        // Gestion du clic sur "Gestion des Créneaux"
        btnCreneaux.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard_Medecin.this, Liste_Creneau_Medecin.class);
            startActivity(intent);
        });

        // Gestion du clic sur "Gestion des Rendez-vous"
        btnRendezVous.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard_Medecin.this, Medecin_RendezVous.class);
            startActivity(intent);
        });

        // Gestion du clic sur "Déconnexion"
        btnDeconnexion.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard_Medecin.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
