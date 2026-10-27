package com.example.project__controle_hopital.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project__controle_hopital.R;
import com.example.project__controle_hopital.dao.AppDatabase;
import com.example.project__controle_hopital.entity.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notificationList;
    private final Context context;

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.tvTitle.setText(notification.title);
        holder.tvMessage.setText(notification.message);
        holder.tvTime.setText(formatTime(notification.timestamp));

        // Style différent pour les notifications non lues
        if (!notification.isRead) {
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.unread_notification_bg)
            );
        } else {
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(context, android.R.color.transparent)
            );
        }

        // Marquage comme lu au clic
        holder.itemView.setOnClickListener(v -> {
            if (!notification.isRead) {
                markNotificationAsRead(notification, position);
            }
        });
    }

    private void markNotificationAsRead(Notification notification, int position) {
        new Thread(() -> {
            AppDatabase.getInstance(context).notificationDao()
                    .markAsRead(notification.id);

            notification.isRead = true;

            ((Activity) context).runOnUiThread(() -> {
                notifyItemChanged(position);
            });
        }).start();
    }

    private String formatTime(long timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH)
                .format(new Date(timestamp));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void updateList(List<Notification> newList) {
        notificationList.clear();
        notificationList.addAll(newList);
        notifyDataSetChanged();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvTime;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvTime = itemView.findViewById(R.id.tvNotificationTime);
        }
    }
}
