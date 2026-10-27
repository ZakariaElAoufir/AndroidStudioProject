package com.example.project__controle_hopital.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.dao.UtilisateurDao;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Admin_Update_Users extends AppCompatActivity {

    EditText editNom, editPrenom, editEmail, editMotDePasse, editTelephone, editSpecialite;
    Spinner spinnerRole;
    Button btnModifier;

    private Utilisateur utilisateur;
    private UtilisateurDao utilisateurDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_users);

        // Liaison des vues
        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editEmail = findViewById(R.id.editEmail);
        editMotDePasse = findViewById(R.id.editMotDePasse);
        editTelephone = findViewById(R.id.editTelephone);
        editSpecialite = findViewById(R.id.editSpecialite);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnModifier = findViewById(R.id.btnModifier);

        // Remplir le Spinner avec les rôles depuis strings.xml
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.roles_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // DAO
        utilisateurDao = AppDatabase.getInstance(getApplicationContext()).utilisateurDao();

        long utilisateurId = getIntent().getLongExtra("utilisateur_id", -1);
        if (utilisateurId != -1) {
            executorService.execute(() -> {
                utilisateur = utilisateurDao.getById(utilisateurId);
                runOnUiThread(() -> {
                    if (utilisateur != null) {
                        editNom.setText(utilisateur.nom);
                        editPrenom.setText(utilisateur.prenom);
                        editEmail.setText(utilisateur.email);
                        editMotDePasse.setText(utilisateur.password);
                        editTelephone.setText(utilisateur.telephone);
                        editSpecialite.setText(utilisateur.specialite);

                        int spinnerPosition = adapter.getPosition(utilisateur.role);
                        spinnerRole.setSelection(spinnerPosition);
                    }
                });
            });
        }

        // Action de modification
        btnModifier.setOnClickListener(v -> {
            if (utilisateur != null) {
                utilisateur.nom = editNom.getText().toString();
                utilisateur.prenom = editPrenom.getText().toString();
                utilisateur.email = editEmail.getText().toString();
                utilisateur.password = editMotDePasse.getText().toString();
                utilisateur.telephone = editTelephone.getText().toString();
                utilisateur.specialite = editSpecialite.getText().toString();
                utilisateur.role = spinnerRole.getSelectedItem().toString();

                executorService.execute(() -> {
                    utilisateurDao.update(utilisateur);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Utilisateur mis à jour", Toast.LENGTH_SHORT).show();
                        // Notifier l'activité précédente pour rafraîchir la liste
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updated_user_id", utilisateur.getId());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    });
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
