package com.example.project__controle_hopital.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class Profile_Patient extends AppCompatActivity {

    private EditText etPrenom, etNom, etTelephone, etEmail, etAntecedents, etMotDePasse;
    private Button btnModifierProfil, btnGoToDashboard, btnModifierMotDePasse;
    private AppDatabase db;
    private Utilisateur user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_patient);

        db = AppDatabase.getInstance(getApplicationContext());
        user = UserSession.getInstance().getCurrentUser();

        // Lier les vues
        etPrenom = findViewById(R.id.etPrenom);
        etNom = findViewById(R.id.etNom);
        etTelephone = findViewById(R.id.etTelephone);
        etEmail = findViewById(R.id.etEmail);
        etAntecedents = findViewById(R.id.etAntecedents);
        etMotDePasse = findViewById(R.id.etMotDePasse); // Champ pour le mot de passe
        btnModifierProfil = findViewById(R.id.btnModifierProfil);
        btnGoToDashboard = findViewById(R.id.btnGotoDashboard);
        btnModifierMotDePasse = findViewById(R.id.btnModifierMotDePasse); // Bouton pour changer le mot de passe

        if (user != null) {
            etPrenom.setText(user.getPrenom());
            etNom.setText(user.getNom());
            etTelephone.setText(user.getTelephone());
            etEmail.setText(user.getEmail());
            etAntecedents.setText(user.getAntecedents() != null ? user.getAntecedents() : "");
        }

        btnModifierProfil.setOnClickListener(v -> {
            if (user != null) {
                user.setPrenom(etPrenom.getText().toString());
                user.setNom(etNom.getText().toString());
                user.setTelephone(etTelephone.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.setAntecedents(etAntecedents.getText().toString());

                new Thread(() -> {
                    db.utilisateurDao().update(user);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_SHORT).show()
                    );
                }).start();
            }
        });

        btnModifierMotDePasse.setOnClickListener(v -> {
            if (user != null) {
                String nouveauMotDePasse = etMotDePasse.getText().toString();

                if (!nouveauMotDePasse.isEmpty()) {
                    user.setPassword(nouveauMotDePasse);

                    new Thread(() -> {
                        db.utilisateurDao().update(user);
                        runOnUiThread(() ->
                                Toast.makeText(this, "Mot de passe mis à jour", Toast.LENGTH_SHORT).show()
                        );
                    }).start();
                } else {
                    Toast.makeText(this, "Veuillez entrer un nouveau mot de passe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoToDashboard.setOnClickListener(v ->
                startActivity(new Intent(Profile_Patient.this, Dashboard_Patient.class))
        );
    }
}