package com.example.project__controle_hopital.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project__controle_hopital.entity.Consultation;

import java.util.List;

@Dao
public interface ConsultationDao {
    @Insert
    void insert(Consultation consultation);

    @Update
    void update(Consultation consultation);

    @Delete
    void delete(Consultation consultation);

    @Query("SELECT * FROM Consultation WHERE rendezvous_id = :rdvId")
    Consultation getByRendezVousId(int rdvId);

    @Query("SELECT * FROM Consultation")
    List<Consultation> getAll();
}
