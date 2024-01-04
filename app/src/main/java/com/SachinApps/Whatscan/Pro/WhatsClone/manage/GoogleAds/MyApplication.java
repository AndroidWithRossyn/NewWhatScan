package com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds;

import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.UserHelper.SHAREDPREFERENCEFILENAME;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.UserHelper.openAdId;

import android.app.Application;
import android.content.SharedPreferences;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;

import papaya.in.admobopenads.AppOpenManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this, initializationStatus -> {
        });

        SharedPreferences prefs = getSharedPreferences(SHAREDPREFERENCEFILENAME, MODE_PRIVATE);
        boolean billing = prefs.getBoolean("night_mode", false);
        String adId = prefs.getString(openAdId, getResources().getString(R.string.open_id));

        if (!billing) {
            new AppOpenManager(this, adId);
        }

    }


}
