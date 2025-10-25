package com.example.project__controle_hopital.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project__controle_hopital.entity.Utilisateur;

import java.util.List;

@Dao
public interface UtilisateurDao {
    @Insert
    void insert(Utilisateur utilisateur);

    @Query("SELECT * FROM utilisateur WHERE email = :email AND password = :password LIMIT 1")
    Utilisateur getUserByEmailAndPassword(String email, String password);


    @Update
    void update(Utilisateur utilisateur);

    @Delete
    void delete(Utilisateur utilisateur);

    @Query("SELECT * FROM Utilisateur WHERE email= :email LIMIT 1")
    Utilisateur getByEmail(String email);

    @Query("SELECT * FROM Utilisateur")
    List<Utilisateur> getAllUsres();

    @Query("SELECT * FROM Utilisateur WHERE id = :id LIMIT 1")
    Utilisateur getById(long id);


}
