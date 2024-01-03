package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.SplashActivity.versionCode;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdManager;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.UserHelper;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.AppUtils;


public class MainActivity extends AppCompatActivity {

    private ReviewManager reviewManager;
    CardView cv_update, cv_download;

    TextView tv_cancel;

    ImageView iv_cancel;
    CardView cv_statusSaver, cv_whatsWeb, cv_deleted_Msg, cv_direct_Chat, cv_stylish_Font, cv_repeatText, cv_hoeToUse, cv_remove_ads, cv_RateApp;
    UserHelper userHelper;
    MainActivity activity;
    FrameLayout templateView;

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.whatsappscanactivity_first_screen1);

        int appVersionCode = AppUtils.getAppVersionCode(getApplicationContext());
        activity = this;
        userHelper = new UserHelper(activity);

        templateView = findViewById(R.id.TemplateView);
        templateView.setVisibility(View.VISIBLE);

        if (!userHelper.getBillingInfo()) {
            Log.d("databseConfig", "load Ads");
            AdManager.loadInterAd(MainActivity.this, userHelper.getStringValue(UserHelper.interstitialAdId));
            AdManager.loadRewardedAd(MainActivity.this, userHelper.getStringValue(UserHelper.rewordAdId));
            AdManager.loadNativeAds(MainActivity.this, templateView, userHelper.getStringValue(UserHelper.nativeAdId));
        }


        cv_statusSaver = findViewById(R.id.cv_statusSaver);
        cv_whatsWeb = findViewById(R.id.cv_whatsappWeb);
        cv_deleted_Msg = findViewById(R.id.cv_deletedMsg);
        cv_direct_Chat = findViewById(R.id.cv_durectChat);
        cv_stylish_Font = findViewById(R.id.cv_stylishFont);
        cv_repeatText = findViewById(R.id.cv_repeatText);
        cv_hoeToUse = findViewById(R.id.cv_howToUse);
        cv_remove_ads = findViewById(R.id.cv_removeAds);
        cv_RateApp = findViewById(R.id.cv_rate);

//        adLayout = findViewById(R.id.adLayout);
        cv_update = findViewById(R.id.cv_update);
        cv_download = findViewById(R.id.tv_download);
        tv_cancel = findViewById(R.id.tv_cancel);
        iv_cancel = findViewById(R.id.iv_cancel);

        if (appVersionCode < versionCode) {

            cv_update.setVisibility(View.VISIBLE);

            tv_cancel.setOnClickListener(view -> cv_update.setVisibility(View.GONE));

            cv_download.setOnClickListener(view -> {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            });

        } else {
            cv_update.setVisibility(View.GONE);
        }


        cv_statusSaver.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StatusSaverActivity.class);
            startActivityes(intent);
        });

        cv_whatsWeb.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WhatsWebActivity.class);
            startActivityes(intent);
        });

        cv_deleted_Msg.setOnClickListener(view -> {
            SharedPreferences prefs = getSharedPreferences("shared", MODE_PRIVATE);
            int idName = prefs.getInt("idName", 0); //0 is the default value.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (idName == 0) {
                        Intent intent = new Intent(MainActivity.this, IntroActivit.class);
                        AdManager.showRewardedVideo(MainActivity.this,intent);
                    }
                    if (idName == 1) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        AdManager.showRewardedVideo(MainActivity.this,intent);
                    }
                }
            }, 2000);
        });

        cv_direct_Chat.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WhatsappDirectActivity.class);
            startActivityes(intent);
        });

        cv_stylish_Font.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StylishFontsActivity.class);
            startActivityes(intent);
        });

        cv_repeatText.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RepeatTextActivity.class);
            startActivityes(intent);
        });

        cv_hoeToUse.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivityes(intent);
        });

        cv_remove_ads.setOnClickListener(view -> {


        });

        cv_RateApp.setOnClickListener(view -> showRateAppFallbackDialog());
        reviewManager = ReviewManagerFactory.create(this);

    }
    public void startActivityes(Intent intent) {
        AdManager.adCounter++;
        AdManager.showInterAd(MainActivity.this, userHelper.getStringValue(UserHelper.interstitialAdId), intent, 0);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("progress", MODE_PRIVATE);
        int appUsedCount = preferences.getInt("appUsedCount", 0);
        appUsedCount++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("appUsedCount", appUsedCount);
        editor.apply();

        if (appUsedCount == 3) {
            showRateAppFallbackDialog();
        }
    }

    private void showRateAppFallbackDialog() {

        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.item_rate_dialog);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.getWindow().setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(layoutParams);

        dialog.setCancelable(false);

        ImageView iv_cancel = dialog.findViewById(R.id.iv_cancel);

        iv_cancel.setOnClickListener(view -> dialog.dismiss());

        ImageView iv_dislike = dialog.findViewById(R.id.iv_dislike);

        iv_dislike.setOnClickListener(view -> dialog.dismiss());

        ImageView iv_like = dialog.findViewById(R.id.iv_like);

        iv_like.setOnClickListener(view -> redirectToPlayStore());

        dialog.show();

    }

    // redirecting user to PlayStore
    public void redirectToPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException exception) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    @Override
    public void onBackPressed() {

        Dialog exitdialog = new Dialog(this);

        exitdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        exitdialog.setContentView(R.layout.item_exit_dialog);

        exitdialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

        exitdialog.getWindow().setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(exitdialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        exitdialog.getWindow().setAttributes(layoutParams);

        exitdialog.getWindow().setGravity(Gravity.BOTTOM);

        exitdialog.setCancelable(true);

        TemplateView templateView1 = exitdialog.findViewById(R.id.templateViewDialog);

        TextView tv_exit = exitdialog.findViewById(R.id.tv_exit);

        tv_exit.setOnClickListener(view -> finishAffinity());

        exitdialog.show();

    }

}