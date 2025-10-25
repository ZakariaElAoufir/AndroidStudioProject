package com.example.project__controle_hopital.Admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.dao.UtilisateurDao;
import com.example.project__controle_hopital.entity.Utilisateur;

public class Admin_Add_Users extends AppCompatActivity {

    EditText editPrenom, editNom, editTelephone, editEmail, editPassword, editAntecedents, editSpecialite;
    Spinner spinnerRole;
    Button btnEnregistrer;

    AppDatabase db;
    UtilisateurDao utilisateurDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_users);

        editPrenom = findViewById(R.id.editPrenom);
        editNom = findViewById(R.id.editNom);
        editTelephone = findViewById(R.id.editTelephone);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editAntecedents = findViewById(R.id.editAntecedents);
        editSpecialite = findViewById(R.id.editSpecialite);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);

        // Initialisation de Room
        db = AppDatabase.getInstance(getApplicationContext());
        utilisateurDao = db.utilisateurDao();

        // Remplir le spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        btnEnregistrer.setOnClickListener(v -> {
            String prenom = editPrenom.getText().toString().trim();
            String nom = editNom.getText().toString().trim();
            String telephone = editTelephone.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();
            String antecedents = editAntecedents.getText().toString().trim();
            String specialite = editSpecialite.getText().toString().trim();

            if (prenom.isEmpty() || nom.isEmpty() || telephone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            Utilisateur utilisateur = new Utilisateur(prenom, nom, telephone, email, password);
            utilisateur.setRole(role);
            utilisateur.setAntecedents(antecedents);
            utilisateur.setSpecialite(specialite);

            // Insertion en thread séparé
            new Thread(() -> {
                utilisateurDao.insert(utilisateur);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Utilisateur ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish(); // Retour à l'écran précédent
                });
            }).start();
        });
    }
}
