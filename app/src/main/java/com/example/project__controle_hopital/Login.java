package com.example.project__controle_hopital;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project__controle_hopital.Admin.Dashboard_Admin;
import com.example.project__controle_hopital.Medecin.Dashboard_Medecin;
import com.example.project__controle_hopital.Patient.Dashboard_Patient;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Utilisateur;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoToRegister;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialisation des éléments UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // Initialisation de la base de données
        db = AppDatabase.getInstance(getApplicationContext());

        // Action bouton login
        btnLogin.setOnClickListener(v -> login());

        // Action bouton register
        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Utilisateur user = db.utilisateurDao().getUserByEmailAndPassword(email, password);
            runOnUiThread(() -> {
                if (user != null) {
                    // Connexion réussie, affichage d'un toast
                    Toast.makeText(this, "Connexion réussie ! Bienvenue " + user.nom, Toast.LENGTH_SHORT).show();

                    // Stocker l'utilisateur connecté dans UserSession
                    UserSession.getInstance().setCurrentUser(user);

                    // Rediriger en fonction du rôle
                    Intent intent;
                    if ("Patient".equals(user.role)) {
                        intent = new Intent(Login.this, Dashboard_Patient.class);
                    } else if ("Admin".equals(user.role)) {
                        intent = new Intent(Login.this, Dashboard_Admin.class);
                    } else if ("Medecin".equals(user.role)) {
                        intent = new Intent(Login.this, Dashboard_Medecin.class);
                    } else {
                        Toast.makeText(this, "Rôle non reconnu", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    startActivity(intent);
                    finish(); // Fermer l'écran de login
                } else {
                    Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

}
