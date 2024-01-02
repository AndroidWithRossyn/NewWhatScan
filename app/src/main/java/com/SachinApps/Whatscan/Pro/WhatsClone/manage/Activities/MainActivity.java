package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.APP_PREFERENCES;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.DATE_END;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.DATE_LIFETIME;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.DATE_PURCHASE;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.GOOGLE_BILLING_KEY;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.IAP_LIFETIME;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.IAP_MONTHLY;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.IAP_YEARLY;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.IS_SUBS;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.IS_VIP;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.SUBS_12_MONTH;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.SUBS_1_MONTH;
import static com.SachinApps.Whatscan.Pro.WhatsClone.AppData.SUBS_LIFETIME;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.SplashActivity.versionCode;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.loadNative;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.loadRewarded;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.mNativeAd;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.showGInter;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.showRewarded;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity /*implements BillingProcessor.IBillingHandler*/ {
    Context mContext = this;
    BillingProcessor bp;
    SharedPreferences prefs;
    Date currentDate;
    Date expiryDate;
    Date expiryDateCheck;
    Calendar c;
    SimpleDateFormat sdf;
    Boolean dateLifetime;
    private boolean readyToPurchase = false;
    public static Boolean isVip = false;
    private ReviewManager reviewManager;
    RelativeLayout adLayout;

    TemplateView templateView;

    CardView cv_update,cv_download;

    TextView tv_cancel;

    ImageView iv_cancel;


    CardView cv_statusSaver,cv_whatsWeb,cv_deleted_Msg,cv_direct_Chat,cv_stylish_Font,cv_repeatText,cv_hoeToUse,cv_remove_ads,cv_RateApp;

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.whatsappscanactivity_first_screen1);

        int appVersionCode = AppUtils.getAppVersionCode(getApplicationContext());

        cv_statusSaver = findViewById(R.id.cv_statusSaver);

        cv_whatsWeb = findViewById(R.id.cv_whatsappWeb);

        cv_deleted_Msg = findViewById(R.id.cv_deletedMsg);

        cv_direct_Chat = findViewById(R.id.cv_durectChat);

        cv_stylish_Font = findViewById(R.id.cv_stylishFont);

        cv_repeatText = findViewById(R.id.cv_repeatText);

        cv_hoeToUse = findViewById(R.id.cv_howToUse);

        cv_remove_ads = findViewById(R.id.cv_removeAds);

        cv_RateApp = findViewById(R.id.cv_rate);

        templateView = findViewById(R.id.TemplateView);

//        adLayout = findViewById(R.id.adLayout);

        cv_update = findViewById(R.id.cv_update);

        cv_download = findViewById(R.id.tv_download);

        tv_cancel = findViewById(R.id.tv_cancel);

        iv_cancel = findViewById(R.id.iv_cancel);

        if (appVersionCode < versionCode){

            cv_update.setVisibility(View.VISIBLE);
            iv_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    cv_update.setVisibility(View.GONE);
                }
            });

            tv_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    cv_update.setVisibility(View.GONE);
                }
            });

            cv_download.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                    }
                    catch (ActivityNotFoundException e){
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                    }
                }
            });

        }else{
            cv_update.setVisibility(View.GONE);
        }

        loadNative(this);
//        loadRewarded(this);

        if (mNativeAd != null){
            templateView.setVisibility(View.VISIBLE);
            templateView.setNativeAd(mNativeAd);
        }

        cv_statusSaver.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StatusSaverActivity.class);
                showGInter(MainActivity.this,intent);
            }
        });

        cv_whatsWeb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WhatsWebActivity.class);
                showGInter(MainActivity.this,intent);
            }
        });

        cv_deleted_Msg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("shared", MODE_PRIVATE);
                int idName = prefs.getInt("idName", 0); //0 is the default value.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (idName == 0) {
                            Intent intent = new Intent(MainActivity.this, IntroActivit.class);
                            showRewarded(MainActivity.this,intent);
                        }
                        if (idName == 1) {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            showRewarded(MainActivity.this,intent);
                        }
                    }
                }, 2000);
            }
        });

        cv_direct_Chat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,WhatsappDirectActivity.class);
               showGInter(MainActivity.this,intent);
            }
        });

        cv_stylish_Font.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StylishFontsActivity.class);
                showGInter(MainActivity.this,intent);
            }
        });

        cv_repeatText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RepeatTextActivity.class);
                showGInter(MainActivity.this,intent);
            }
        });

        cv_hoeToUse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                showGInter(MainActivity.this,intent);
            }
        });

        cv_remove_ads.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateLifetime) {
                    // jika true
                    Toast.makeText(MainActivity.this, "Your subscription is active forever", Toast.LENGTH_LONG).show();
                } else {
                    // cek subscribe
                    if (expiryDateCheck.before(currentDate)) {
                        // expied
                        // purchase
                        boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                        if (isSubsUpdateSupported) {
                            // launch payment flow
                            bp.subscribe(MainActivity.this, SUBS_12_MONTH);
                        } else {
                            Toast.makeText(MainActivity.this, "This device not supported to purchase", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        // subscribe
                        Toast.makeText(MainActivity.this, "Your subscription has not ended", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        cv_RateApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showRateAppFallbackDialog();
            }
        });


        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        isVip = prefs.getBoolean(IS_VIP, false);
        reviewManager = ReviewManagerFactory.create(this);
        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        expiryDateCheck = new Date(prefs.getLong(DATE_END, 0));
        dateLifetime = prefs.getBoolean(DATE_LIFETIME, false);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        c = Calendar.getInstance();
        currentDate = new Date();
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);
        if (!isAvailable) {
            // continue
            Toast.makeText(this, "Sorry, Purchase is not ready on your device", Toast.LENGTH_LONG).show();
        }
/*        bp = new BillingProcessor(this, GOOGLE_BILLING_KEY, this);
        bp.initialize();*/



    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("progress", MODE_PRIVATE);
        int appUsedCount = preferences.getInt("appUsedCount",0);
        appUsedCount++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("appUsedCount", appUsedCount);
        editor.apply();

        if (appUsedCount==3){
            showRateAppFallbackDialog();
        }
    }

    private void showRateAppFallbackDialog() {

        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.item_rate_dialog);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.getWindow().setFlags(Window.FEATURE_NO_TITLE,Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(layoutParams);

        dialog.setCancelable(false);

        ImageView iv_cancel = dialog.findViewById(R.id.iv_cancel);

        iv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView iv_dislike = dialog.findViewById(R.id.iv_dislike);

        iv_dislike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView iv_like = dialog.findViewById(R.id.iv_like);

        iv_like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToPlayStore();
            }
        });

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

/*    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

        if (productId.equals(SUBS_1_MONTH)) {
            prefs.edit().putBoolean(IAP_MONTHLY, true).apply();
            prefs.edit().putBoolean(IS_VIP, true).apply();
            prefs.edit().putBoolean(IS_SUBS, true).apply();
            prefs.edit().putLong(DATE_PURCHASE, currentDate.getTime()).apply();

            // convert date to calendar
            c.setTime(currentDate);
            // manipulate date
            c.add(Calendar.MONTH, 1);
            // convert calendar to date
            expiryDate = c.getTime();

            prefs.edit().putLong(DATE_END, expiryDate.getTime()).apply();
        }

        if (productId.equals(SUBS_12_MONTH)) {
            prefs.edit().putBoolean(IAP_YEARLY, true).apply();
            prefs.edit().putBoolean(IS_VIP, true).apply();
            prefs.edit().putBoolean(IS_SUBS, true).apply();

            //tambah point 365 day
            prefs.edit().putLong(DATE_PURCHASE, currentDate.getTime()).apply();

            // convert date to calendar
            c.setTime(currentDate);
            // manipulate date
            c.add(Calendar.YEAR, 1);
            // convert calendar to date
            expiryDate = c.getTime();

            prefs.edit().putLong(DATE_END, expiryDate.getTime()).apply();
        }

        if (productId.equals(SUBS_LIFETIME)) {
            prefs.edit().putBoolean(IAP_LIFETIME, true).apply();
            prefs.edit().putBoolean(IS_VIP, true).apply();
            prefs.edit().putBoolean(DATE_LIFETIME, true).apply();
            prefs.edit().putBoolean(IS_SUBS, true).apply();

            prefs.edit().putLong(DATE_PURCHASE, currentDate.getTime()).apply();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {
        for (String sku : bp.listOwnedProducts())
            System.out.println("Owned Managed Product: " + sku);
        for (String sku : bp.listOwnedSubscriptions())
            System.out.println("Owned Subscription: " + sku);

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(this, "Purchase error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
    }*/

    @Override
    public void onBackPressed() {

        Dialog exitdialog = new Dialog(this);

        exitdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        exitdialog.setContentView(R.layout.item_exit_dialog);

        exitdialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

        exitdialog.getWindow().setFlags(Window.FEATURE_NO_TITLE,Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(exitdialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        exitdialog.getWindow().setAttributes(layoutParams);

        exitdialog.getWindow().setGravity(Gravity.BOTTOM);

        exitdialog.setCancelable(true);

        loadNative(this);

        TemplateView templateView1 = exitdialog.findViewById(R.id.templateViewDialog);

        TextView tv_exit = exitdialog.findViewById(R.id.tv_exit);

        tv_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        if (mNativeAd != null){
            templateView1.setVisibility(View.VISIBLE);
            templateView1.setNativeAd(mNativeAd);
        }

        exitdialog.show();

    }

}