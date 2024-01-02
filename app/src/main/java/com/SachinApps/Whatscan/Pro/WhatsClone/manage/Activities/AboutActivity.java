package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.RelativeLayout;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;


public class AboutActivity extends AppCompatActivity {

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.whatsappscanactivity_app_about);
//        FrameLayout fl_adplaceholder = (FrameLayout)findViewById(R.id.fl_adplaceholder);
//        Ads ads = new Ads();
//        ads.loadNativeAd(this,fl_adplaceholder);
        RelativeLayout adLayout = findViewById(R.id.adLayout);
    }




}