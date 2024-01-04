package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SachinApps.Whatscan.Pro.WhatsClone.PurchaseItemClickListener;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters.PurchaseAdapter;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.Security;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.UserHelper;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PremiumActivity extends AppCompatActivity implements  PurchaseItemClickListener {
    String TAG = "PremiumActivityItems";
    UserHelper userHelper;
    PremiumActivity activity;

    BillingClient billingClient;
    PurchaseAdapter purchaseAdapter;
    private List<ProductDetails> list = new ArrayList<>();
    private String Licensing = "Licensing_key";
    String InAppId = "your_item_id";

    TextView manageSub, subPolicy, termsSev, refundPolicy;
    Button continues;

    RecyclerView rvPurchaseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        activity = this;
        userHelper = new UserHelper(activity);

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(purchasesUpdatedListener)
                .build();


        establishConnection();

        Log.d(TAG, "onCreate: ");

        continues = findViewById(R.id.continues);
        manageSub = findViewById(R.id.manage_sub);
        subPolicy = findViewById(R.id.sub_policy);
        termsSev = findViewById(R.id.terms_sev);
        refundPolicy = findViewById(R.id.refund_policy);
        rvPurchaseList = findViewById(R.id.rv_purchaseList);

        purchaseAdapter = new PurchaseAdapter(PremiumActivity.this, list, PremiumActivity.this);
        rvPurchaseList.setLayoutManager(new LinearLayoutManager(PremiumActivity.this));
        rvPurchaseList.setAdapter(purchaseAdapter);


        manageSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PremiumActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.manage_subscription))));

            }
        });

        subPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PremiumActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.google_privacy))));

            }
        });

        refundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PremiumActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.refund_policy))));

            }
        });
        termsSev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PremiumActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.google_terms))));

            }
        });

        continues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PurchaseFlow();
            }
        });


    }


    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                for (Purchase purchase : list) {
                    try {
                        verifySubPurchase(purchase);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED && list != null) {
                Log.d(TAG, "purchasesUpdatedListener: item already owned");
                
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED
                    || billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE
                    || billingResult.getResponseCode() == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE
                    || billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE
                    || billingResult.getResponseCode() == BillingClient.BillingResponseCode.DEVELOPER_ERROR
                    || billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR
                    || billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_NOT_OWNED
            ) {
                
                Log.d(TAG, "purchasesUpdatedListener: Something was wrong");
            }
        }
    };

    private void establishConnection() {

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    showProducts();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to Google Play
                // by calling the startConnection() method.
                establishConnection();
            }
        });

    }

    private void showProducts() {

        List<QueryProductDetailsParams.Product> productList_one = Arrays.asList(
                // Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(InAppId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()

        );


        QueryProductDetailsParams params_one = QueryProductDetailsParams.newBuilder()
                .setProductList(productList_one)
                .build();


        billingClient.queryProductDetailsAsync(params_one, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<ProductDetails> prodDetailsList) {
                // Process the result
                if (prodDetailsList != null) {

                    Log.d(TAG, prodDetailsList.size() + " number of products InApp");
//                    Log.d(TAG, "onProductDetailsResponse: product list all \n" + prodDetailsList + "\n\n");
//                    Log.d(TAG, "onProductDetailsResponse: " + prodDetailsList.get(0).getName());

                    if (list.size() > 2) {
                        list.clear();
                    }
                    list.addAll(prodDetailsList);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            
                            purchaseAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });
    }

    private void verifySubPurchase(Purchase purchase) throws IOException {
        Log.d(TAG, "verifySubPurchase: ");
/***********/
        ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
        ConsumeResponseListener listener = (billingResult, s) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Successfully consumed purchase.");
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

            if (!Security.verifyPurchase(Licensing, purchase.getOriginalJson(), purchase.getSignature())) {
                // invalid purchase
                Log.d(TAG, "invalid purchase");
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "invalid purchase!", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
            billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);

        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            Log.d("purchase_confirm", "Purchase : pending");
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            Log.d("purchase_confirm", "Purchase : UNSPECIFIED_STATE");
        }

/*********/
//        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
//                .setPurchaseToken(purchase.getPurchaseToken())
//                .build();

//        billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);

        Log.d("purchase_confirm", "Purchase Token: " + purchase.getPurchaseToken());
        Log.d("purchase_confirm", "Purchase Time: " + purchase.getPurchaseTime());
        Log.d("purchase_confirm", "Purchase OrderID: " + purchase.getOrderId());
    }

    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                // User prefs to set premium
                userHelper.setBillingInfo(true);
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Subscription activated, Enjoy!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };


    List<BillingFlowParams.ProductDetailsParams> productforbutton = new ArrayList<>();

    @Override
    public void onPurchaseItemClicked(int position, List<BillingFlowParams.ProductDetailsParams> productDetailsList) {
//        productforbutton.clear();
        productforbutton = productDetailsList;
        Log.d("itemClickPurchse", "onCreate: instance result " + productforbutton);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (billingClient != null) {
            billingClient.endConnection();
        }
    }

    public void PurchaseFlow() {
        if (billingClient != null && productforbutton.size() > 0) {

            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productforbutton)
                    .build();

            BillingResult billingResult = billingClient.launchBillingFlow(PremiumActivity.this, billingFlowParams);

            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "onPurchaseItemClicked: ");
            } else {
                Log.d(TAG, "onPurchaseItemClicked: " + billingResult.getDebugMessage());
            }
        } else {
            Toast.makeText(PremiumActivity.this, "Select Item to continue !!!", Toast.LENGTH_SHORT).show();
        }
    }


}