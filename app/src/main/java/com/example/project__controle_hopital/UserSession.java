package com.example.project__controle_hopital;

import com.example.project__controle_hopital.entity.Utilisateur;

public class UserSession {
    private static UserSession instance;
    private Utilisateur currentUser;

    // Constructeur privé pour garantir que cette classe est un singleton
    private UserSession() {}

    // Méthode pour obtenir l'instance unique de la classe
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Méthode pour obtenir l'utilisateur actuel
    public Utilisateur getCurrentUser() {
        return currentUser;
    }

    // Méthode pour définir l'utilisateur actuel
    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
    }
}

