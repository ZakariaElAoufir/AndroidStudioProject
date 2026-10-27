# Hospital Mobile App

Application Android native (**Java**) de gestion des rendez-vous d'un hôpital. Trois profils l'utilisent : l'**administrateur** gère les comptes, le **médecin** publie ses créneaux et suit ses rendez-vous, le **patient** réserve une consultation et reçoit des rappels. Toutes les données sont stockées localement sur l'appareil avec **Room** (SQLite).

## Fonctionnalités

### Commun

- Inscription (compte patient par défaut) et connexion par e-mail et mot de passe.
- Redirection vers le tableau de bord correspondant au rôle : Admin, Médecin ou Patient.
- Déconnexion depuis chaque tableau de bord.

### Administrateur

- Liste de tous les utilisateurs.
- Ajout d'un utilisateur avec choix du rôle (Admin, Médecin, Patient).
- Modification d'un utilisateur, rôle compris.
- Compte administrateur créé automatiquement au premier lancement.

### Médecin

- Ajout de créneaux de consultation (date, heure de début, heure de fin).
- Liste, modification et suppression de ses créneaux.
- Liste de ses rendez-vous avec le patient, la date et le motif.
- Changement du statut d'un rendez-vous : en attente, confirmé, annulé, terminé.
- Envoi d'un **rappel** au patient : notification enregistrée dans l'application et notification système Android.
- Modification de son profil, spécialité comprise.

### Patient

- Consultation des créneaux disponibles de tous les médecins.
- Réservation d'un rendez-vous avec un motif facultatif.
- Historique de ses rendez-vous avec leur statut.
- Centre de notifications avec badge du nombre de notifications non lues.
- Modification de son profil et de son mot de passe.

## Stack technique

- **Java**, Android SDK (minSdk 21, targetSdk 35)
- **Room** pour la persistance locale (entités, DAO, clés étrangères avec suppression en cascade)
- **LiveData** pour le compteur de notifications non lues
- **RecyclerView** et adapters personnalisés pour les listes
- **NotificationCompat** pour les notifications système
- Material Components, ConstraintLayout

## Modèle de données

| Entité | Contenu |
| --- | --- |
| `Utilisateur` | Nom, prénom, téléphone, e-mail (unique), mot de passe, rôle, antécédents (patient), spécialité (médecin) |
| `Creneau` | Médecin, date, heure de début, heure de fin |
| `RendezVous` | Patient, médecin, créneau, motif, statut |
| `Consultation` | Rendez-vous, note du médecin, date |
| `Notification` | Destinataire, titre, message, type, date, lue ou non |

## Structure du projet

```text
app/src/main/java/com/example/project__controle_hopital/
├── Login.java, Register.java, UserSession.java
├── Admin/      # Tableau de bord, liste, ajout et modification des utilisateurs
├── Medecin/    # Tableau de bord, créneaux, rendez-vous, profil
├── Patient/    # Tableau de bord, réservation, historique, notifications, profil
├── adapter/    # Adapters des RecyclerView
├── dao/        # AppDatabase (Room) et DAO
└── entity/     # Entités Room
```

## Installation

### Prérequis

- Android Studio (AGP 8.9) et JDK 17 ou plus récent
- Un émulateur ou un appareil Android 5.0 (API 21) ou plus récent

### Lancement

```bash
git clone https://github.com/ZakariaElAoufir/AndroidStudioProject.git
```

Ouvrir le dossier dans Android Studio, laisser Gradle synchroniser le projet, puis lancer l'application avec **Run ▶**.

Sur Android 13 et plus, autoriser les notifications pour recevoir les rappels de rendez-vous.
