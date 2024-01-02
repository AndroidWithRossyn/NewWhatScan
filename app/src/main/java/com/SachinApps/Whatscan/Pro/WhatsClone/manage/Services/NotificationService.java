package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat.Builder;

import com.SachinApps.Whatscan.Pro.WhatsClone.manage.App;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.HomeActivity;

public class NotificationService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        startForeground(1,
                new Builder(this, App.channelId)
                        .setContentTitle("Service Running")
                .setContentText("Waiting for a deleted messege")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(PendingIntent.getActivity(this, 1, new Intent(this, HomeActivity.class), 0)).build());
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
    }
}
