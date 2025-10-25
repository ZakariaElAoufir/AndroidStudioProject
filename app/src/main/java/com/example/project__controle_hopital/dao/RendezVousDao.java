package com.example.project__controle_hopital.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project__controle_hopital.entity.RendezVous;

import java.util.List;

@Dao
public interface RendezVousDao {
    @Insert
    void insert(RendezVous rdv);

    @Update
    void update(RendezVous rdv);

    @Delete
    void delete(RendezVous rdv);

    @Query("SELECT * FROM RendezVous WHERE patient_id = :patientId")
    List<RendezVous> getByPatientId(int patientId);

    @Query("SELECT * FROM RendezVous")
    List<RendezVous> getAll();

    @Query("SELECT * FROM RendezVous WHERE medecin_id = :medecinId ORDER BY creneau_id DESC")
    List<RendezVous> getByMedecinId(long medecinId);

    @Query("SELECT * FROM RendezVous WHERE id = :id LIMIT 1")
    RendezVous getById(long id);

    @Query("SELECT * FROM RendezVous WHERE patient_id = :patientId ORDER BY creneau_id DESC")
    List<RendezVous> getByPatientId(long patientId);
}
