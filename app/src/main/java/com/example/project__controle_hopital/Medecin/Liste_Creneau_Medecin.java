package com.example.project__controle_hopital.Medecin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.UserSession;
import com.example.project__controle_hopital.adapter.CreneauAdapter;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Creneau;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Liste_Creneau_Medecin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CreneauAdapter adapter;
    private AppDatabase db;
    private Utilisateur currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_creneau_medecin);

        // Initialisation
        db = AppDatabase.getInstance(this);
        currentUser = UserSession.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recyclerViewCreneaux);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Vérification rôle
        if (currentUser == null || !"Medecin".equals(currentUser.role)) {
            Toast.makeText(this, "Accès non autorisé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configuration de l'adapter
        adapter = new CreneauAdapter(new ArrayList<>(), new CreneauAdapter.OnCreneauClickListener() {
            @Override
            public void onEditClick(Creneau creneau) {
                launchEditActivity(creneau);
            }

            @Override
            public void onDeleteClick(Creneau creneau) {
                confirmDelete(creneau);
            }
        });
        recyclerView.setAdapter(adapter);

        // Boutons
        findViewById(R.id.btnRetourDashboard).setOnClickListener(v -> finish());
        findViewById(R.id.btnAjouterCreneau).setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, Ajout_Creneau_Medecin.class),
                        RequestCodes.ADD_CRENEAU));

        // Chargement initial
        loadCreneaux();
    }

    private void loadCreneaux() {
        new Thread(() -> {
            List<Creneau> creneaux = db.creneauDao().getByMedecinId(currentUser.id);
            runOnUiThread(() -> adapter.updateList(creneaux));
        }).start();
    }

    private void launchEditActivity(Creneau creneau) {
        Intent intent = new Intent(this, EditCreneauActivity.class);
        intent.putExtra("creneau", creneau);
        startActivityForResult(intent, RequestCodes.EDIT_CRENEAU);
    }

    private void confirmDelete(Creneau creneau) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Supprimer ce créneau ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteCreneau(creneau))
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteCreneau(Creneau creneau) {
        new Thread(() -> {
            db.creneauDao().delete(creneau);
            runOnUiThread(() -> {
                Toast.makeText(this, "Créneau supprimé", Toast.LENGTH_SHORT).show();
                loadCreneaux(); // Rafraîchir
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (
                requestCode == RequestCodes.ADD_CRENEAU ||
                        requestCode == RequestCodes.EDIT_CRENEAU)) {
            loadCreneaux();
        }
    }

    private static class RequestCodes {
        static final int ADD_CRENEAU = 1001;
        static final int EDIT_CRENEAU = 1002;
    }
}


