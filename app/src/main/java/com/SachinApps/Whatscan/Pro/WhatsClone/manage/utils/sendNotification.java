package com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.MainActivity;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.App;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;

public class sendNotification {
    @SuppressLint("NotificationPermission")
    public void sendBackground(final Context context, final String contentTitle, final String contentText) {
        try {
            final NotificationCompat.Builder setContentIntent = new NotificationCompat.Builder(context, App.channelId)
                    .setContentTitle((CharSequence) contentTitle)
                    .setContentText((CharSequence) contentText)
                    .setSmallIcon(IconCompat.createWithBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo)))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                    .setBadgeIconType(R.drawable.logo)
                    .setAutoCancel(true)
                    .setPriority(1)
                    .setContentIntent(PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
            if (Build.VERSION.SDK_INT >= 24) {
                setContentIntent.setPriority(5);
            }
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, setContentIntent.build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
