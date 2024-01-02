package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Services;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.db.recentNumberDB;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.SaveFiles;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.SaveMsg;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class NotifyListener extends NotificationListenerService {
    private static observer observer;
    BroadcastReceiver broadcastReceiver;
    Context context;
    boolean onserving = false;
    private ArrayList<String> packs;

    public NotifyListener() {
        this.onserving = false;
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Log.d("onserlog", "received");
                    final String stringExtra = intent.getStringExtra(context.getString(R.string.noti_obserb));
                    final boolean equals = stringExtra.equals("true");
                    if (equals) {
                        if (!onserving) {
                            startonserving();
                            onserving = equals;
                        }
                    } else if (stringExtra.equals("update")) {
                        updateList();
                    } else {
                        onserving = equals;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    private class observer extends FileObserver {
        public observer(String str) {
            super(str, 4095);
            Log.d("filedellog", "start");
        }

        public void onEvent(int i, String str) {
            String str2 = "filedellog";
            if (i == 256 || (i == 128 && !str.equals(".probe"))) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("create File path--> ");
                stringBuilder.append(str);
                Log.d(str2, stringBuilder.toString());
                try {
                    new SaveFiles().save(str, context);
                } catch (Exception e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("create error: ");
                    stringBuilder2.append(e.toString());
                    Log.d(str2, stringBuilder2.toString());
                }
            }
            if ((i & 512) != 0 || (i & 1024) != 0) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("dlete File path--> ");
                sb3.append(str);
                Log.d(str2, sb3.toString());
                try {
                    new SaveFiles().move(str, context);
                } catch (Exception i2) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("del error: ");
                    sb4.append(i2.toString());
                    Log.d(str2, sb4.toString());
                }
            }
        }
    }

    public IBinder onBind(Intent intent) {
        Log.d("notilogm", "bind");
        return super.onBind(intent);
    }

    public boolean onUnbind(Intent intent) {
        Log.d("unblog", "unb ");
        return super.onUnbind(intent);
    }

    public void onCreate() {
        super.onCreate();
        Log.d("notilogm", "on create");
        this.context = getApplicationContext();
        isNotificationServiceRunning();
        this.packs = new ArrayList();
        updateList();
        if (VERSION.SDK_INT < 23) {
            startonserving();
        } else if (check()) {
            startonserving();
        }
        LocalBroadcastManager.getInstance(this.context).registerReceiver(this.broadcastReceiver, new IntentFilter(this.context.getString(R.string.noti_obserb)));
    }

    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d("notilogm", "on connect");
    }

    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d("notilogm", "on dis connect");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d("notilogm", "on cresate");
        tryReconnectService();
        return START_STICKY;
    }

    private void startonserving() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Images");
        observer view_deleted_messages_Noti_observer = observer;
        if (view_deleted_messages_Noti_observer != null) {
            view_deleted_messages_Noti_observer.stopWatching();
        }
        observer = new observer(file.getPath());
        observer.startWatching();
    }

    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        super.onNotificationPosted(statusBarNotification);
        String str = "notilogm";
        Log.d(str, "on posted");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(this.packs.size());
        Log.d(str, stringBuilder.toString());
        Iterator it = this.packs.iterator();
        while (it.hasNext()) {
            Log.d("plog", (String) it.next());
        }
        try {
            String packageName = statusBarNotification.getPackageName();
            if (this.packs.contains(packageName)) {
                Bundle extras = statusBarNotification.getNotification().extras;
                String string = extras.getString(NotificationCompat.EXTRA_TITLE);
                String string1 = extras.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("on posted pack: ");
                stringBuilder2.append(string);
                Log.d(str, stringBuilder2.toString());
                new SaveMsg(getApplicationContext(), string, string1, packageName);
            }
        } catch (Exception statusBarNotification2) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("error: ");
            stringBuilder1.append(statusBarNotification2.toString());
            Log.d(str, stringBuilder1.toString());
        }
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        super.onNotificationRemoved(statusBarNotification);
        Log.d("notilogm", "on removed");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("notilogm", "on destroy");
    }

    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        Log.d("notilogm", "on task removed");
    }

    @RequiresApi(api = 23)
    private boolean check() {
        return this.context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;
    }

    private void updateList() {
        this.packs.clear();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... voidArr) {
                packs = new recentNumberDB(context).getAllPackages();
                return null;
            }
        }.execute(new Void[0]);
    }

    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (VERSION.SDK_INT >= 24) {
            requestRebind(new ComponentName(getApplicationContext(), NotifyListener.class));
        }
    }

    private void toggleNotificationListenerService() {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public boolean isNotificationServiceRunning() {
        String string = Secure.getString(this.context.getContentResolver(), "enabled_notification_listeners");
        CharSequence packageName = this.context.getPackageName();
        if (string != null) {
            boolean contains = string.contains(packageName);
            if (contains) {
                return contains;
            }
        }
        return false;
    }
}
