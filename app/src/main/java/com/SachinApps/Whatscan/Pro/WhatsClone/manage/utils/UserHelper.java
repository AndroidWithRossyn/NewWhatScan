package com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserHelper {

    public static final String SHAREDPREFERENCEFILENAME = "SHAREDPREFERENCE";
    private Context context;
    private SharedPreferences preferences;
    private Activity activity;
    public static String bannerAdId = "bannerAdId";
    public static String interstitialAdId = "interstitialAdId";
    public static String nativeAdId = "nativeAdId";
    public static String rewordAdId = "rewordAdId";
    public static String openAdId = "openAdId";
    public static String BillingTag = "premium";
    public static String LifetimeSub = "LifetimeSub";


    public UserHelper(Activity activity) {
        this.activity = activity;
        context = activity;
        preferences = activity.getSharedPreferences(SHAREDPREFERENCEFILENAME, Context.MODE_PRIVATE);
    }

    public boolean getBillingInfo() {
        return preferences.getBoolean(BillingTag, false);
    }

    public void setBillingInfo(boolean status) {
        preferences.edit().putBoolean(BillingTag, status).apply();
    }

    public void saveBooleanValue(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBooleanValue(String key) {
        return preferences.getBoolean(key, false);
    }

    public void saveStringValue(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getStringValue(String key) {
        return preferences.getString(key, "");
    }


    public void saveInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }


    public void saveLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

}
