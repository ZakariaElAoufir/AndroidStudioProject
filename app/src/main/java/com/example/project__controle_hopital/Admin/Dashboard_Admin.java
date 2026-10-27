package com.example.project__controle_hopital.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project__controle_hopital.Login;
import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.entity.Utilisateur;

public class Dashboard_Admin extends AppCompatActivity {

    Button btnGoToUtilisateurManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        // Récupérer l'utilisateur connecté
        Utilisateur currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Afficher les informations de l'utilisateur dans le TextView
            TextView tvNom = findViewById(R.id.tvNom);
            tvNom.setText("Bienvenue, " + currentUser.nom);
        }

        // Gérer l'action de déconnexion
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            // Effacer l'utilisateur de la session
            UserSession.getInstance().setCurrentUser(null);

            // Démarrer l'activité de login
            Intent intent = new Intent(Dashboard_Admin.this, Login.class);
            startActivity(intent);
            finish(); // Fermer l'activité Dashboard
        });

        //partir dans la page de gestiondes utilisateur
        btnGoToUtilisateurManage = findViewById(R.id.btnManageUsers);

        if (btnGoToUtilisateurManage != null) {
            btnGoToUtilisateurManage.setOnClickListener(v -> {
                Intent intent = new Intent(Dashboard_Admin.this, CRUD_Admin.class);
                startActivity(intent);
            });
        } else {
            Toast.makeText(this, "Bouton btnManageUsers introuvable", Toast.LENGTH_LONG).show();
        }
    }
}