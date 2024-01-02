package com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds;


import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.SplashActivity.idBanner;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.SplashActivity.idInterstitial;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.SplashActivity.idNative;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.SplashActivity.idReworded;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;


public class AdsDecalration {

    public static InterstitialAd mInterstitialAd;
    public static AdLoader adLoader;
    public static NativeAd mNativeAd;

    public static RewardedInterstitialAd rewardedInterstitialAd;

    public static int count = 0;

    public static RewardedAd rewardedAd;

    String TAG ="TAG";


    // ---------------------------------Google Ads (Banner)------------------------------


    public static void loadAdmobBannerAds(Context context, FrameLayout adview,AdSize adSize) {
        if (idBanner != null) {
            AdView mAdView = new AdView(context);
            if (adSize != null){
                mAdView.setAdSize(adSize);
            }else{
                mAdView.setAdSize(AdSize.BANNER);
            }

            mAdView.setAdUnitId((String) idBanner);

            AdRequest adRequest = new AdRequest.Builder().build();

            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError p0) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    Log.d("Tag", "onAdFailedToLoad: " + p0.getMessage());
                }

                @Override
                public void onAdLoaded() {
                    adview.addView(mAdView);
                    Log.d("Tag", "onAdLoaded: Loaded");
                }
            });
        }
    }



    // ---------------------------------Google Ads (Interstitial)------------------------------

    public static void LoadGInter(Activity context) {

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, idInterstitial, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;

                    }
                });
    }


    public static void showGInter(Activity context, Intent intent) {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    LoadGInter(context);
                    startActivity(context, intent, 0);

                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    LoadGInter(context);
                    startActivity(context, intent, 0);
                }
            });
        } else {
            LoadGInter(context);
            startActivity(context, intent, 0);
        }
    }

    // ---------------------------------Google Ads (Native)------------------------------

    public static void loadNative(Activity context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        if (idNative != null) {
            adLoader = new AdLoader.Builder(context, idNative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            mNativeAd = nativeAd;
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            Log.i("TagAdmobNative", adError.getMessage());
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }

    }

    public static void loadRewarded(Context context){

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, idReworded,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                    }
                });
    }

    public static void showRewarded(Activity context,Intent intent){

        if (rewardedAd  != null){
            rewardedAd.show(context, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                }
            });
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    startActivity(context,intent,0);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    startActivity(context,intent,0);
                }
            });

        }else{
            startActivity(context,intent,0);
        }
    }

    public static void startActivity(Activity context, Intent intent, int requstCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requstCode);
        } else {
            context.finish();
        }

    }





}
