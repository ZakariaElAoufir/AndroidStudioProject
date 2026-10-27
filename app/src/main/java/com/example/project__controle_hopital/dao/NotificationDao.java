package com.example.project__controle_hopital.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project__controle_hopital.entity.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    void insert(Notification notification);

    // Méthode synchrone pour le chargement initial
    @Query("SELECT * FROM notifications WHERE user_id = :userId ORDER BY timestamp DESC")
    List<Notification> getNotificationsByUserSync(long userId);

    // Méthode LiveData pour les observations
    @Query("SELECT * FROM notifications WHERE user_id = :userId ORDER BY timestamp DESC")
    LiveData<List<Notification>> getNotificationsByUser(long userId);

    @Query("UPDATE notifications SET is_read = 1 WHERE id = :notificationId")
    void markAsRead(long notificationId);

    @Query("SELECT COUNT(*) FROM notifications WHERE user_id = :userId AND is_read = 0")
    LiveData<Integer> getUnreadCount(long userId);
}
