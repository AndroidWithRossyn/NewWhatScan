package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SachinApps.Whatscan.Pro.WhatsClone.PurchaseItemClickListener;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ProductDetails;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {
    List<ProductDetails> skus;
    List<BillingFlowParams.ProductDetailsParams> params = new ArrayList<>();

    private PurchaseItemClickListener itemClickListener;

    Context context;
    int selecteditem = -5;

    public PurchaseAdapter(Context applicationContext, List<ProductDetails> skuList, PurchaseItemClickListener clickListener) {
        this.skus = skuList;
        this.itemClickListener = clickListener;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_list, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ProductDetails productDetails = skus.get(position);

        holder.tv_title.setText(productDetails.getName());


        if (selecteditem == position) {
            holder.description.setVisibility(View.VISIBLE);

            holder.tv_title.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_price.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_offer.setTextColor(context.getResources().getColor(R.color.white));
            holder.time_period.setTextColor(context.getResources().getColor(R.color.white));

            holder.tv_offer_per.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_price_month.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_effective.setTextColor(context.getResources().getColor(R.color.white));

        } else {
            holder.description.setVisibility(View.GONE);
            holder.maincard.setBackgroundColor(context.getResources().getColor(R.color.white));

            holder.tv_title.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_price.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_offer.setTextColor(context.getResources().getColor(R.color.black));
            holder.time_period.setTextColor(context.getResources().getColor(R.color.black));

            holder.tv_offer_per.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_price_month.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_effective.setTextColor(context.getResources().getColor(R.color.black));
        }


        if (productDetails.getOneTimePurchaseOfferDetails() != null) {
            holder.description.setText(productDetails.getDescription());
            Log.d("adapterresult_one", "onBindViewHolder: offer " + new Gson().toJson(productDetails.getOneTimePurchaseOfferDetails()));
            String formattedPrice = productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
            holder.tv_price.setText(currencyFormat(formattedPrice));

            holder.time_period.setText("Life time");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetails productDetails1 = skus.get(position);
                    params.clear();
                    assert productDetails1.getOneTimePurchaseOfferDetails() != null;
                    params.add(BillingFlowParams
                            .ProductDetailsParams
                            .newBuilder()
                            .setProductDetails(productDetails1)
//                            .setOfferToken(productDetails1.getOneTimePurchaseOfferDetails())
                            .build());
                    if (params != null && itemClickListener != null) {
                        itemClickListener.onPurchaseItemClicked(position, params);
                    }

                    selecteditem = position;

                    holder.description.setVisibility(View.VISIBLE);


                    holder.tv_title.setTextColor(context.getResources().getColor(R.color.white));
                    holder.tv_price.setTextColor(context.getResources().getColor(R.color.white));
                    holder.tv_offer.setTextColor(context.getResources().getColor(R.color.white));
                    holder.time_period.setTextColor(context.getResources().getColor(R.color.white));
                    holder.tv_offer_per.setTextColor(context.getResources().getColor(R.color.white));


                    notifyDataSetChanged();
                }
            });
        } else if (productDetails.getSubscriptionOfferDetails() != null) {

            String formattedPrice = productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice();
            holder.tv_price.setText(currencyFormat(formattedPrice));
            holder.description.setText(context.getResources().getString(R.string.purchase_list));

            if (productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getBillingPeriod().equals("P1Y")) {
                holder.time_period.setText("Per Year");
                holder.tv_price_month.setVisibility(View.VISIBLE);
                int introductoryPriceMicros = (int) productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceAmountMicros() / 1000000;
                if (introductoryPriceMicros > 0) {
                    float month = (float) introductoryPriceMicros / 12;
                    holder.tv_price_month.setText(month + " " + productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceCurrencyCode() + " Par Month");
                }

            } else if (productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getBillingPeriod().equals("P1M")) {
                holder.time_period.setText("Per Month");
                holder.tv_effective.setVisibility(View.GONE);
                holder.tv_price_month.setVisibility(View.GONE);
            } else if (productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getBillingPeriod().equals("P3M")) {
                holder.time_period.setText("Every 3 Month");
                holder.tv_price_month.setVisibility(View.VISIBLE);
                int introductoryPriceMicros = (int) productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceAmountMicros() / 1000000;
                if (introductoryPriceMicros > 0) {
                    float month = (float) introductoryPriceMicros / 3;
                    holder.tv_price_month.setText(month + " " + productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceCurrencyCode() + " Par Month");
                }

            } else {
                holder.time_period.setVisibility(View.GONE);
                holder.tv_price_month.setVisibility(View.GONE);
            }


            if (productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().size() > 1) {
                holder.tv_offer.setText(currencyFormat(productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(1).getFormattedPrice()));
                holder.tv_offer.setPaintFlags(holder.tv_offer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                int introductoryPriceMicros = (int) productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceAmountMicros() / 1000000;
                int originalPriceMicros = (int) productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(1).getPriceAmountMicros() / 1000000;

                Log.d("adapterresult", "onBindViewHolder: orgprice " + originalPriceMicros);
                Log.d("adapterresult", "onBindViewHolder: offer " + introductoryPriceMicros);

                if (originalPriceMicros > introductoryPriceMicros) {
                    int discountMicros = originalPriceMicros - introductoryPriceMicros;
                    int percentageDiscount = (int) ((discountMicros / (double) originalPriceMicros) * 100);

                    holder.tv_offer_per.setVisibility(View.VISIBLE);
                    holder.tv_offer_per.setText("You are saving " + percentageDiscount + "%");

                    if (percentageDiscount > 50) {
                        holder.recommend.setVisibility(View.VISIBLE);
                        holder.tv_effective.setVisibility(View.VISIBLE);
                    } else {
                        holder.recommend.setVisibility(View.GONE);
                        holder.tv_effective.setVisibility(View.GONE);
                    }
                } else {
                    holder.recommend.setVisibility(View.GONE);
                }

            } else {
                holder.tv_offer_per.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetails productDetails1 = skus.get(position);
                    params.clear();
                    assert productDetails1.getSubscriptionOfferDetails() != null;
//                    Log.d("adapterresult", "onBindViewHolder: offer " + productDetails1.getSubscriptionOfferDetails().get(0).getOfferToken());
                    params.add(BillingFlowParams
                            .ProductDetailsParams
                            .newBuilder()
                            .setProductDetails(productDetails1)
                            .setOfferToken(productDetails1.getSubscriptionOfferDetails().get(0).getOfferToken())
                            .build());
                    if (params != null && itemClickListener != null) {
                        itemClickListener.onPurchaseItemClicked(position, params);
                    }

                    selecteditem = position;

                    holder.description.setVisibility(View.VISIBLE);

                    holder.tv_title.setTextColor(context.getResources().getColor(R.color.white));
                    holder.tv_price.setTextColor(context.getResources().getColor(R.color.white));
                    holder.tv_offer.setTextColor(context.getResources().getColor(R.color.white));
                    holder.time_period.setTextColor(context.getResources().getColor(R.color.white));
                    holder.tv_offer_per.setTextColor(context.getResources().getColor(R.color.white));


                    notifyDataSetChanged();
                }
            });
        } else {

        }

    }

    public static String currencyFormat(String input) {
        if (input.endsWith(".00")) {
            return input.substring(0, input.length() - 3);
        }
        return input;
    }

    @Override
    public int getItemCount() {
        return skus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_price, tv_offer, tv_offer_per, time_period, recommend, description, tv_price_month, tv_effective;
        LinearLayout maincard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_offer = itemView.findViewById(R.id.tv_offer);
            tv_offer_per = itemView.findViewById(R.id.tv_offer_percentage);


            time_period = itemView.findViewById(R.id.timeperiod);
            recommend = itemView.findViewById(R.id.recommend);
            description = itemView.findViewById(R.id.description);
            maincard = itemView.findViewById(R.id.maincardpurchase);

            tv_effective = itemView.findViewById(R.id.effactive);
            tv_price_month = itemView.findViewById(R.id.tv_price_month);


        }
    }
}
