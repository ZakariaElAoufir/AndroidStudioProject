package com.example.project__controle_hopital.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.adapter.CreneauDisponibleAdapter;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.dao.CreneauDao;
import com.example.project__controle_hopital.dao.RendezVousDao;
import com.example.project__controle_hopital.dao.UtilisateurDao;
import com.example.project__controle_hopital.entity.Creneau;
import com.example.project__controle_hopital.entity.RendezVous;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class AppointmentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CreneauDisponibleAdapter adapter;
    private List<Creneau> creneauxDisponibles = new ArrayList<>();
    private Map<Long, Utilisateur> medecinMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        // Initialisation
        recyclerView = findViewById(R.id.recyclerViewCreneaux);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnRetour = findViewById(R.id.btnRetourDashboard);
        btnRetour.setOnClickListener(v -> finish());

        // Chargement des données
        loadCreneaux();
    }

    private void loadCreneaux() {
        AppDatabase db = AppDatabase.getInstance(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Creneau> creneaux = db.creneauDao().getAll();
            for (Creneau c : creneaux) {
                if (c.disponible) {
                    creneauxDisponibles.add(c);
                    if (!medecinMap.containsKey(c.medecinId)) {
                        medecinMap.put(c.medecinId, db.utilisateurDao().getById(c.medecinId));
                    }
                }
            }
            runOnUiThread(() -> {
                adapter = new CreneauDisponibleAdapter(creneauxDisponibles, medecinMap, this);
                recyclerView.setAdapter(adapter);
            });
        });
    }
}


