package com.example.project__controle_hopital.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "RendezVous", foreignKeys = {
        @ForeignKey(entity = Utilisateur.class, parentColumns = "id", childColumns = "patient_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Utilisateur.class, parentColumns = "id", childColumns = "medecin_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Creneau.class, parentColumns = "id", childColumns = "creneau_id", onDelete = ForeignKey.CASCADE)
})
public class RendezVous {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "patient_id")
    public long patientId;

    @ColumnInfo(name = "medecin_id")
    public long medecinId;

    @ColumnInfo(name = "creneau_id")
    public long creneauId;

    @ColumnInfo(name = "motif")
    public String motif;

    @ColumnInfo(name = "statut")
    public String statut = "en_attente"; // Valeur par défaut

    @ColumnInfo(name = "rappel_envoye")
    public boolean rappelEnvoye = false;  // Valeur par défaut

    // Getters et Setters

    public long getCreneauId() {
        return creneauId;
    }

    public void setCreneauId(long creneauId) {
        this.creneauId = creneauId;
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

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public boolean isRappelEnvoye() {
        return rappelEnvoye;
    }

    public void setRappelEnvoye(boolean rappelEnvoye) {
        this.rappelEnvoye = rappelEnvoye;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}

