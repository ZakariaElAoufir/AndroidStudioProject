package com.example.project__controle_hopital.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Creneau", foreignKeys = {
        @ForeignKey(entity = Utilisateur.class, parentColumns = "id", childColumns = "medecin_id", onDelete = ForeignKey.CASCADE)
})
public class Creneau implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "medecin_id")
    public long medecinId;

    @NonNull
    @ColumnInfo(name = "date")
    public String date;

    @NonNull
    @ColumnInfo(name = "heure_debut")
    public String heureDebut;

    @NonNull
    @ColumnInfo(name = "heure_fin")
    public String heureFin;

    @ColumnInfo(name = "disponible")
    public boolean disponible = true;  // Valeur par défaut

    // Getters et Setters

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @NonNull
    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(@NonNull String heureDebut) {
        this.heureDebut = heureDebut;
    }

    @NonNull
    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(@NonNull String heureFin) {
        this.heureFin = heureFin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(long medecinId) {
        this.medecinId = medecinId;
    }
}

