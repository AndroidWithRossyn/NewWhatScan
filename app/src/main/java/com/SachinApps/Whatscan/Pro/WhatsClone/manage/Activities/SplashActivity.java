package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.LoadGInter;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.loadNative;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.loadRewarded;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.showGInter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;

public class SplashActivity extends AppCompatActivity {

    FirebaseDatabase database;

    DatabaseReference reference;


    public static String idBanner;
    public static String idInterstitial;
    public static String idNative;
    public static String idReworded;
    public static String idAppopen;

    public static int versionCode;

    AppOpenAd AD;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        FirebaseApp.initializeApp(this);

        database = FirebaseDatabase.getInstance();

        reference = database.getReference("Ads");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                idBanner = snapshot.child("idBanner").getValue(String.class);
                idInterstitial = snapshot.child("idInterstitial").getValue(String.class);
                idNative = snapshot.child("idNative").getValue(String.class);
                idReworded = snapshot.child("idReworded").getValue(String.class);
                idAppopen = snapshot.child("idAppopen").getValue(String.class);
                versionCode = snapshot.child("versionCode").getValue(Integer.class);


                loadNative(SplashActivity.this);
                loadRewarded(SplashActivity.this);
                LoadGInter(SplashActivity.this);

                SharedPreferences prefs = getSharedPreferences("shared", MODE_PRIVATE);

                AdRequest adRequest = new AdRequest.Builder().build();

                InterstitialAd.load(SplashActivity.this,idInterstitial, adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(SplashActivity.this);
                                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            super.onAdDismissedFullScreenContent();
                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                            super.onAdFailedToShowFullScreenContent(adError);
                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                Log.i("TAG", "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d("TAG", loadAdError.toString());
                                mInterstitialAd = null;
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
