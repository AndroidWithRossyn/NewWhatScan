package com.SachinApps.Whatscan.Pro.WhatsClone.manage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build.VERSION;

import androidx.multidex.MultiDexApplication;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;

public class App extends MultiDexApplication {
    public static String channelId = "";
    public static com.SachinApps.Whatscan.Pro.WhatsClone.manage.App instance;
    public static int adCount = 0;

    public void onCreate() {
        super.onCreate();
        instance = this;
        channelId = getString(R.string.app_name);
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(getString(R.string.app_name));
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
        }
    }

    public static com.SachinApps.Whatscan.Pro.WhatsClone.manage.App getInstance() {
        return instance;
    }

    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}
