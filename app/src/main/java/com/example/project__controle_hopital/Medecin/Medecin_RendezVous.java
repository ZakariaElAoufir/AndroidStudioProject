package com.example.project__controle_hopital.Medecin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.adapter.RendezVousAdapter;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.RendezVous;
import com.example.project__controle_hopital.entity.Utilisateur;
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Medecin_RendezVous extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RendezVousAdapter adapter;
    private List<RendezVous> rendezVousList = new ArrayList<>();
    private AppDatabase db;
    private Utilisateur currentMedecin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medecin_rendez_vous);

        // Initialisation
        db = AppDatabase.getInstance(this);
        currentMedecin = UserSession.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recyclerViewRendezVous);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Vérification rôle
        if (currentMedecin == null || !"Medecin".equals(currentMedecin.role)) {
            Toast.makeText(this, "Accès réservé aux médecins", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configuration de l'adapter
        adapter = new RendezVousAdapter(rendezVousList, this);
        recyclerView.setAdapter(adapter);

        // Chargement des rendez-vous
        loadRendezVous();

        // Bouton retour
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadRendezVous() {
        new Thread(() -> {
            List<RendezVous> rdvs = db.rendezVousDao().getByMedecinId(currentMedecin.id);
            runOnUiThread(() -> {
                if (rdvs.isEmpty()) {
                    Toast.makeText(this, "Aucun rendez-vous trouvé", Toast.LENGTH_SHORT).show();
                }
                adapter.updateList(rdvs);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRendezVous();
    }
}