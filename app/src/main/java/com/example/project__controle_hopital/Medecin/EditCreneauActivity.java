package com.example.project__controle_hopital.Medecin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Creneau;

public class EditCreneauActivity extends AppCompatActivity {

    private EditText editDate, editHeureDebut, editHeureFin;
    private CheckBox checkDisponible;
    private Button btnSave, btnCancel;

    private Creneau creneau;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_creneau);

        editDate = findViewById(R.id.editDate);
        editHeureDebut = findViewById(R.id.editHeureDebut);
        editHeureFin = findViewById(R.id.editHeureFin);
        checkDisponible = findViewById(R.id.checkDisponible);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        db = AppDatabase.getInstance(this);

        creneau = (Creneau) getIntent().getSerializableExtra("creneau");

        if (creneau == null) {
            Toast.makeText(this, "Erreur : aucun créneau reçu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Préremplir les champs
        editDate.setText(creneau.date);
        editHeureDebut.setText(creneau.heureDebut);
        editHeureFin.setText(creneau.heureFin);
        checkDisponible.setChecked(creneau.disponible);

        btnSave.setOnClickListener(v -> {
            creneau.date = editDate.getText().toString().trim();
            creneau.heureDebut = editHeureDebut.getText().toString().trim();
            creneau.heureFin = editHeureFin.getText().toString().trim();
            creneau.disponible = checkDisponible.isChecked();

            new Thread(() -> {
                try {
                    db.creneauDao().update(creneau);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Créneau modifié", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish(); // Retourner à l’activité précédente
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}