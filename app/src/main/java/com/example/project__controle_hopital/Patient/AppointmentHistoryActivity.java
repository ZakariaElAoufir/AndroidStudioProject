package com.example.project__controle_hopital.Patient;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.adapter.AppointmentAdapter;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Creneau;
import com.example.project__controle_hopital.entity.RendezVous;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private AppDatabase db;
    private Utilisateur currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        // Initialisation
        db = AppDatabase.getInstance(this);
        currentUser = UserSession.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recyclerViewAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Vérification utilisateur
        if (currentUser == null || !"Patient".equals(currentUser.role)) {
            Toast.makeText(this, "Accès réservé aux patients", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configuration de l'adapter
        adapter = new AppointmentAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Bouton retour
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Chargement des rendez-vous
        loadAppointments();
    }

    private void loadAppointments() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<RendezVous> appointments = db.rendezVousDao().getByPatientId(currentUser.id);
            runOnUiThread(() -> adapter.updateAppointments(appointments));
        });
    }
}
