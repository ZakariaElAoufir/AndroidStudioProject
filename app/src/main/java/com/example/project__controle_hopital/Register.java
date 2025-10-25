package com.example.project__controle_hopital;

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

import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Utilisateur;

public class Register extends AppCompatActivity {

    private EditText etPrenom, etNom, etTelephone, etEmail, etPassword;
    private Button btnRegister, btnGoToLogin;

    private AppDatabase db;  // Remplace AppDatabase par le nom de ta Room Database si différent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialisation des éléments UI
        etPrenom = findViewById(R.id.etPrenom);
        etNom = findViewById(R.id.etNom);
        etTelephone = findViewById(R.id.etTelephone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        // Initialisation de la base de données
        db = AppDatabase.getInstance(getApplicationContext());

        // Action bouton register
        btnRegister.setOnClickListener(v -> register());

        // Action bouton login
        btnGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
        });
    }

    private void register() {
        String prenom = etPrenom.getText().toString().trim();
        String nom = etNom.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (prenom.isEmpty() || nom.isEmpty() || telephone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer l'utilisateur avec le rôle "Patient" par défaut
        new Thread(() -> {
            Utilisateur newUser = new Utilisateur(prenom, nom, telephone, email, password);
            db.utilisateurDao().insert(newUser);  // Insertion dans la base de données
            runOnUiThread(() -> {
                Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                // Démarrer l'activité de connexion
                startActivity(new Intent(Register.this, Login.class));
            });
        }).start();
    }
}