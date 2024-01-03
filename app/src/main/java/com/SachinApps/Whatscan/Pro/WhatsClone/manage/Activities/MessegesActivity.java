package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdManager;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.UserHelper;
import com.google.android.gms.ads.AdSize;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters.msgAdapter;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models.DataModel;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.db.recentNumberDB;

import java.lang.ref.WeakReference;
import java.util.List;

public class MessegesActivity extends AppCompatActivity {

    FrameLayout adView;
    private static class loadMsg extends AsyncTask<String, Void, List<DataModel>> {
        WeakReference<MessegesActivity> mwr;

        private loadMsg(WeakReference<MessegesActivity> weakReference) {
            this.mwr = weakReference;
        }

        protected List<DataModel> doInBackground(String... strArr) {
            return new recentNumberDB((mwr.get()).getApplicationContext()).getMsg(strArr[0], strArr[1]);
        }

        protected void onPostExecute(List<DataModel> list) {
            super.onPostExecute(list);
            try {
                ((MessegesActivity) mwr.get()).findViewById(R.id.progressBar).setVisibility(View.GONE);
                RecyclerView recyclerView = (RecyclerView) ((MessegesActivity) mwr.get()).findViewById(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(((MessegesActivity) mwr.get()).getApplicationContext());
//                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new msgAdapter((mwr.get()).getApplicationContext(), list));
            } catch (Exception list2) {
                list2.printStackTrace();
            }
        }
    }
    UserHelper userHelper;
    MessegesActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messeges);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity = this;
        userHelper = new UserHelper(activity);
        String stringExtra = getIntent().getStringExtra("name");
        new loadMsg(new WeakReference(this)).execute(stringExtra, getIntent().getStringExtra("pack"));
        setTitle(stringExtra);

        Button replays = findViewById(R.id.replays);
        replays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        adView = findViewById(R.id.adView0);

        if (!userHelper.getBillingInfo() && userHelper.getStringValue(UserHelper.bannerAdId) != null) {
            Log.d("databseConfig", "load Ads");
            AdManager.loadBannerAd(MessegesActivity.this, adView, userHelper.getStringValue(UserHelper.bannerAdId));
        } else {
            adView.setVisibility(View.GONE);
        }
    }

}
