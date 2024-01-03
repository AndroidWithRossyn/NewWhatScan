package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdManager;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.UserHelper;
import com.google.android.ads.nativetemplates.TemplateView;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WhatsappDirectActivity extends AppCompatActivity {

    Button btn_send;
    CountryCodePicker ccp;
    EditText edt_message;
    EditText edt_phonenumber;
    FrameLayout templateView;
    UserHelper userHelper;
    WhatsappDirectActivity activity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_direct);
        activity = this;
        userHelper = new UserHelper(activity);
        templateView = findViewById(R.id.TemplateView);

        if (!userHelper.getBillingInfo() && userHelper.getStringValue(UserHelper.bannerAdId) != null) {
            Log.d("databseConfig", "load Ads");
            templateView.setVisibility(View.VISIBLE);
          AdManager.loadNativeAds(WhatsappDirectActivity.this, templateView, userHelper.getStringValue(UserHelper.nativeAdId));
        }


        this.btn_send = (Button) findViewById(R.id.btn_send);
        this.edt_message = (EditText) findViewById(R.id.edt_message);
        this.edt_phonenumber = (EditText) findViewById(R.id.etphonenumber);
        this.ccp = (CountryCodePicker) findViewById(R.id.ccp);
        this.btn_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WhatsappDirectActivity.this.openWhatsApp();
            }
        });
    }


    @SuppressLint({"WrongConstant"})
    public void openWhatsApp() {
        String str = this.ccp.getSelectedCountryCode() + " " + this.edt_phonenumber.getText().toString();
        if (whatsappInstalledOrNot("com.whatsapp")) {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("whatsapp://send/?text=" + URLEncoder.encode(this.edt_message.getText().toString(), "UTF-8") + "&phone=" + str)));
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this, "Please install WhatsApp",Toast.LENGTH_LONG).show();
            } catch (UnsupportedEncodingException unused2) {
                Toast.makeText(this, "Error while encoding your text message",Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.whatsapp"));
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }

    private boolean whatsappInstalledOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}