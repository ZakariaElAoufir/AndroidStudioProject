package com.example.project__controle_hopital.Medecin;

import android.os.Bundle;
import android.view.View;
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
import com.example.project__controle_hopital.entity.Creneau;
import com.example.project__controle_hopital.entity.Utilisateur;

public class Ajout_Creneau_Medecin extends AppCompatActivity {

    private EditText etDate, etHeureDebut, etHeureFin;
    private Button btnAjouterCreneau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_creneau_medecin);

        etDate = findViewById(R.id.etDate);
        etHeureDebut = findViewById(R.id.etHeure);
        etHeureFin = findViewById(R.id.etHeureFin);
        btnAjouterCreneau = findViewById(R.id.btnAjouterCreneau);

        btnAjouterCreneau.setOnClickListener(v -> ajouterCreneau());
    }

    private void ajouterCreneau() {
        String date = etDate.getText().toString().trim();
        String heureDebut = etHeureDebut.getText().toString().trim();
        String heureFin = etHeureFin.getText().toString().trim();

        if (date.isEmpty() || heureDebut.isEmpty() || heureFin.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Utilisateur medecin = UserSession.getInstance().getCurrentUser();
        if (medecin == null) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        Creneau creneau = new Creneau();
        creneau.setDate(date);
        creneau.setHeureDebut(heureDebut);
        creneau.setHeureFin(heureFin);
        creneau.setDisponible(true);
        creneau.setMedecinId(medecin.id);

        new Thread(() -> {
            AppDatabase.getInstance(getApplicationContext()).creneauDao().insert(creneau);
            runOnUiThread(() -> {
                Toast.makeText(this, "Créneau ajouté avec succès", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); //Important pour rafraîchir la liste
                finish();
            });
        }).start();
    }
}
