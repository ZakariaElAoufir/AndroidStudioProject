package com.example.project__controle_hopital.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Consultation", foreignKeys = {
        @ForeignKey(entity = RendezVous.class, parentColumns = "id", childColumns = "rendezvous_id", onDelete = ForeignKey.CASCADE)
})
public class Consultation {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "rendezvous_id")
    public long rendezvousId;

    @ColumnInfo(name = "note_medecin")
    public String noteMedecin;

    @NonNull
    @ColumnInfo(name = "date_consult")
    public String dateConsult;

    // Getters et Setters

    @NonNull
    public String getDateConsult() {
        return dateConsult;
    }

    public void setDateConsult(@NonNull String dateConsult) {
        this.dateConsult = dateConsult;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNoteMedecin() {
        return noteMedecin;
    }

    public void setNoteMedecin(String noteMedecin) {
        this.noteMedecin = noteMedecin;
    }

    public long getRendezvousId() {
        return rendezvousId;
    }

    public void setRendezvousId(long rendezvousId) {
        this.rendezvousId = rendezvousId;
    }
}

