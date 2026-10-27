package com.example.project__controle_hopital.Patient;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.adapter.NotificationAdapter;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Notification;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class PatientNotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList = new ArrayList<>();
    private AppDatabase db;
    private Utilisateur currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_notifications);

        // Initialisation
        db = AppDatabase.getInstance(this);
        currentUser = UserSession.getInstance().getCurrentUser();

        // Configuration du bouton retour
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Configuration du RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificationAdapter(notificationList, this);
        recyclerView.setAdapter(adapter);

        // Chargement des notifications
        loadNotifications();
    }

    private void loadNotifications() {
        if (currentUser == null) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        new Thread(() -> {
            List<Notification> notifications = db.notificationDao()
                    .getNotificationsByUserSync(currentUser.id);

            runOnUiThread(() -> {
                if (notifications == null || notifications.isEmpty()) {
                    Toast.makeText(this, "Aucune notification disponible", Toast.LENGTH_LONG).show();
                } else {
                    adapter.updateList(notifications);
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }
}