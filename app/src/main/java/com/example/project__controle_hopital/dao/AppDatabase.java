package com.example.project__controle_hopital.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project__controle_hopital.entity.Consultation;
import com.example.project__controle_hopital.entity.Creneau;
import com.example.project__controle_hopital.entity.Notification;
import com.example.project__controle_hopital.entity.RendezVous;
import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Utilisateur.class,
        Creneau.class,
        RendezVous.class,
        Consultation.class,
        Notification.class
}, version = 1, exportSchema = false) // Réinitialisez à version 1
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    // ... vos DAOs ...
    public abstract UtilisateurDao utilisateurDao();
    public abstract CreneauDao creneauDao();
    public abstract RendezVousDao rendezVousDao();
    public abstract ConsultationDao consultationDao();
    public abstract NotificationDao notificationDao();

    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "hospital_database2")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(() -> {
                                        // Création du compte admin
                                        Utilisateur admin = new Utilisateur();
                                        admin.nom = "Admin";
                                        admin.prenom = "Principal";
                                        admin.telephone = "0000000000";
                                        admin.email = "admin@hospital.com";
                                        admin.password = "admin123";
                                        admin.role = "admin";
                                        admin.antecedents = null;
                                        admin.specialite = null;

                                        getInstance(context).utilisateurDao().insert(admin);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}