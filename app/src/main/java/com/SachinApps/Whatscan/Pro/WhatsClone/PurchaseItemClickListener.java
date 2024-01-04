package com.SachinApps.Whatscan.Pro.WhatsClone;

import com.android.billingclient.api.BillingFlowParams;

import java.util.List;

public interface PurchaseItemClickListener {
    void onPurchaseItemClicked(int position, List<BillingFlowParams.ProductDetailsParams> productDetailsList);
}
