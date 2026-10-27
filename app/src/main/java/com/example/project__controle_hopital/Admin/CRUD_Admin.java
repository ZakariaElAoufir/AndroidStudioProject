package com.example.project__controle_hopital.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.adapter.AdapterListener;
import com.example.project__controle_hopital.adapter.UserAdapter;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.dao.UtilisateurDao;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CRUD_Admin extends AppCompatActivity implements AdapterListener {

    RecyclerView myrecycler;
    Button btnRetour,btnAjouter;

    private AppDatabase db;
    private UtilisateurDao utilisateurDao;
    private UserAdapter userAdapter;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_admin);

        myrecycler = findViewById(R.id.utilisateurRecycler);

        db = AppDatabase.getInstance(getApplicationContext());
        utilisateurDao = db.utilisateurDao();

        userAdapter = new UserAdapter(this,this);
        myrecycler.setAdapter(userAdapter);
        myrecycler.setLayoutManager(new LinearLayoutManager(this));

        fetchData();

        btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CRUD_Admin.this,Dashboard_Admin.class));
                finish();
            }
        });

        btnAjouter = findViewById(R.id.btnAjouter);
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CRUD_Admin.this,Admin_Add_Users.class));
                finish();
            }
        });
    }

    private void fetchData() {
        executorService.execute(() -> {
            List<Utilisateur> utilisateurList = utilisateurDao.getAllUsres();

            // Met à jour l'adapter sur le thread principal
            runOnUiThread(() -> {
                for (Utilisateur utilisateur : utilisateurList) {
                    userAdapter.addUtilisateur(utilisateur);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Toujours arrêter les services si plus utilisés
    }

    @Override
    public void OnUpdate(Long id, int pos) {
        Intent intent = new Intent(CRUD_Admin.this, Admin_Update_Users.class);
        intent.putExtra("utilisateur_id", id);
        startActivityForResult(intent, 1); //permet de recevoir le résultat
    }

    @Override
    public void OnDelete(Long id, int pos) {
        executorService.execute(() -> {
            Utilisateur utilisateur = utilisateurDao.getById(id);
            if (utilisateur != null) {
                utilisateurDao.delete(utilisateur);

                // Mettre à jour l'adapter dans le thread principal
                runOnUiThread(() -> {
                    userAdapter.removeUtilisateurAt(pos);
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            // Si un utilisateur a été mis à jour, récupère l'ID
            long updatedUserId = data.getLongExtra("updated_user_id", -1);
            if (updatedUserId != -1) {
                refreshData();  // Rafraîchir la liste
            }
        }
    }
    private void refreshData() {
        executorService.execute(() -> {
            List<Utilisateur> utilisateurList = utilisateurDao.getAllUsres();

            // Met à jour l'adapter sur le thread principal
            runOnUiThread(() -> {
                userAdapter.clear();  // Effacer la liste actuelle
                for (Utilisateur utilisateur : utilisateurList) {
                    userAdapter.addUtilisateur(utilisateur);  // Ajouter les utilisateurs mis à jour
                }
            });
        });
    }


}