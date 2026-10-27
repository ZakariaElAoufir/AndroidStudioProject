package com.example.project__controle_hopital.Medecin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Utilisateur;

public class Profile_Medecin extends AppCompatActivity {

    private EditText etPrenom, etNom, etTelephone, etEmail, etSpecialite;
    private Button btnModifierProfil, btnGoToDashboard;
    private AppDatabase db;
    private Utilisateur user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_medecin);

        db = AppDatabase.getInstance(getApplicationContext());
        user = UserSession.getInstance().getCurrentUser();

        // Lier les vues
        etPrenom = findViewById(R.id.etPrenom);
        etNom = findViewById(R.id.etNom);
        etTelephone = findViewById(R.id.etTelephone);
        etEmail = findViewById(R.id.etEmail);
        etSpecialite = findViewById(R.id.etSpecialite);
        btnModifierProfil = findViewById(R.id.btnModifierProfil);
        btnGoToDashboard = findViewById(R.id.btnGotoDashboard);

        if (user != null) {
            etPrenom.setText(user.prenom);
            etNom.setText(user.nom);
            etTelephone.setText(user.telephone);
            etEmail.setText(user.email);
            etSpecialite.setText(user.specialite != null ? user.specialite : "");
        }

        btnModifierProfil.setOnClickListener(v -> {
            if (user != null) {
                user.prenom = etPrenom.getText().toString();
                user.nom = etNom.getText().toString();
                user.telephone = etTelephone.getText().toString();
                user.email = etEmail.getText().toString();
                user.specialite = etSpecialite.getText().toString();

                new Thread(() -> {
                    db.utilisateurDao().update(user);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_SHORT).show()
                    );
                }).start();
            }
        });

        btnGoToDashboard.setOnClickListener(v ->
                startActivity(new Intent(Profile_Medecin.this, Dashboard_Medecin.class))
        );
    }
}
