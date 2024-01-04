package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.UserHelper;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    FirebaseDatabase database;

    DatabaseReference reference;

    public static String idBanner;
    public static String idInterstitial;
    public static String idNative;
    public static String idReworded;
    public static String idAppopen;

    public static int versionCode;
    BillingClient billingClient;
    SplashActivity activity;
    UserHelper userHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity = this;
        userHelper = new UserHelper(activity);
        checkSubscription();
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
                
                      idBanner = getString(R.string.banner_id);
        idInterstitial = getString(R.string.interstitial_id);
        idNative = getString(R.string.native_id);
        idReworded = getString(R.string.reword_id);
        idAppopen = getString(R.string.open_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                idBanner = getString(R.string.banner_id);
                idInterstitial = getString(R.string.interstitial_id);
                idNative = getString(R.string.native_id);
                idReworded = getString(R.string.reword_id);
                idAppopen = getString(R.string.open_id);


            }
        });

  

        userHelper.saveStringValue(UserHelper.bannerAdId, idBanner);
        userHelper.saveStringValue(UserHelper.interstitialAdId, idInterstitial);
        userHelper.saveStringValue(UserHelper.nativeAdId, idNative);
        userHelper.saveStringValue(UserHelper.rewordAdId, idReworded);
        userHelper.saveStringValue(UserHelper.openAdId, idAppopen);
        HomeScreen();
    }

    public void HomeScreen() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkSubscription() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                            for (Purchase purchase : purchases) {
                                verifySubPurchase(purchase);
                            }

                        }
                    }
                })
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                Log.d("billingcheckitem", "onBillingServiceDisconnected: ");
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, new PurchasesResponseListener() {
                        @Override
                        public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                Log.d("testOffer", list.size() + " size InApp");
                                if (list.size() > 0) {
                                    userHelper.setBillingInfo(true);
                                    Log.d("testOffer", list.size() + " size InApp>0");
                                    userHelper.saveBooleanValue(UserHelper.LifetimeSub, true);
                                    int i = 0;
                                    for (Purchase purchase : list) {
                                        // Here you can manage each product, if you have multiple subscriptions
                                        Log.d("testOffer", purchase.getOriginalJson()); // Get to see the order information
                                        Log.d("testOffer", " index " + i);
                                        i++;
                                    }
//                                    activity.runOnUiThread(new Runnable() {
//                                        public void run() {
//                                            Toast.makeText(activity, "Subscription activated, Enjoy!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });

                                }

                            }

                        }
                    });
                }
            }
        });
    }

    private void verifySubPurchase(Purchase purchase) {

        Log.d("billingcheckitem", "verifySubPurchase: ");

        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();

        billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // User prefs to set premium
                    userHelper.setBillingInfo(true);
                    Log.d("billingcheckitem", "Purchase User prefs to set premium");

          /*          activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Subscription activated, Enjoy!", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        });

        Log.d("billingcheckitem", "Purchase Token: " + purchase.getPurchaseToken());
        Log.d("billingcheckitem", "Purchase Time: " + purchase.getPurchaseTime());
        Log.d("billingcheckitem", "Purchase OrderID: " + purchase.getOrderId());
    }
}
