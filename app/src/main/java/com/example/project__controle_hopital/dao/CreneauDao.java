package com.example.project__controle_hopital.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project__controle_hopital.entity.Creneau;

import java.util.List;

@Dao
public interface CreneauDao {
    @Insert
    void insert(Creneau creneau);

    @Update
    void update(Creneau creneau);

    @Delete
    void delete(Creneau creneau);

    @Query("SELECT * FROM Creneau WHERE medecin_id = :medecinId")
    List<Creneau> getByMedecinId(long medecinId);

    @Query("SELECT * FROM Creneau")
    List<Creneau> getAll();

    @Query("SELECT * FROM Creneau WHERE id = :id LIMIT 1")
    Creneau getById(long id);
}
