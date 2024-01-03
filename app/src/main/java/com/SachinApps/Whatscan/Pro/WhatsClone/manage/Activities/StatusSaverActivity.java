package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.ViewPager;

import com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdManager;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.UserHelper;
import com.google.android.gms.ads.AdSize;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.StatusSaverVideosSaver;
import com.SachinApps.Whatscan.Pro.WhatsClone.StatusSaverImagesSaver;
import com.SachinApps.Whatscan.Pro.WhatsClone.WhstWebPagerAdapter;

import java.util.ArrayList;
import java.util.Iterator;

public class StatusSaverActivity extends AppCompatActivity {

    WhstWebPagerAdapter pager_adapter_view;
    TabLayout tl;
    ViewPager view_Pager;
    StatusSaverActivity activity;

    TextView permissionTV;
    ArrayList<Uri> fileArrayList = new ArrayList<>();

    ProgressBar progressBar;

    FrameLayout adView;

    UserHelper userHelper;
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.whatsappscanactivity_home_screen);
        activity = this;
        userHelper = new UserHelper(activity);
        adView = findViewById(R.id.adView0);

        if (!userHelper.getBillingInfo() && userHelper.getStringValue(UserHelper.bannerAdId) != null) {
            Log.d("databseConfig", "load Ads");
            AdManager.loadBannerAd(StatusSaverActivity.this, adView, userHelper.getStringValue(UserHelper.bannerAdId));
        } else {
            adView.setVisibility(View.GONE);
        }

        tl = (TabLayout) findViewById(R.id.tabs);
        view_Pager = (ViewPager) findViewById(R.id.container);

        permissionTV = findViewById(R.id.tv_permission);

        progressBar = findViewById(R.id.pb_progress);

        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.toolbar);
        setSupportActionBar(toolbar);
        ((FloatingActionButton) findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.fab)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                StatusSaverActivity.this.shareThisApp();
            }
        });


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == -1 || ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == -1) {
                requestAllPermissions();
            }
            pager_adapter_view = new WhstWebPagerAdapter(getSupportFragmentManager());
            pager_adapter_view.add_fragments(new StatusSaverImagesSaver(), "Status Images");
            pager_adapter_view.add_fragments(new StatusSaverVideosSaver(), "Status Videos");
            view_Pager.setAdapter(pager_adapter_view);
            tl.setupWithViewPager(view_Pager);

            fileArrayList = new ArrayList<>();
        } else {

            if (getContentResolver().getPersistedUriPermissions().size() > 0) {
                new LoadAllFiles().execute();

                Log.d("TAG", "onCreate: getContentResolver().getPersistedUriPermissions().size() > 0");
                permissionTV.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            } else {

                Log.d("TAG", "onCreate: <0");
                permissionTV.setVisibility(View.VISIBLE);
            }

            permissionTV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            progressBar.setVisibility(View.VISIBLE);
                            StorageManager sm = (StorageManager) activity.getSystemService(Context.STORAGE_SERVICE);
                            Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                            String startDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
                            Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
                            String scheme = uri.toString();
                            scheme = scheme.replace("/root/", "/document/");
                            scheme += "%3A" + startDir;
                            uri = Uri.parse(scheme);
                            intent.putExtra("android.provider.extra.INITIAL_URI", uri);
                            startActivityForResult(intent, 004);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            });

        }
    }


    public void requestAllPermissions() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
        arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (ContextCompat.checkSelfPermission(this, str) != 0) {
                arrayList.add(str);
            }
        }
        requestGroupPermission(arrayList);
    }

    public void shareThisApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }

    }

    private void requestGroupPermission(ArrayList<String> arrayList) {
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        ActivityCompat.requestPermissions(this, strArr, 1000);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.SachinApps.Whatscan.Pro.WhatsClone.R.menu.whatsappscanmenu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == com.SachinApps.Whatscan.Pro.WhatsClone.R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (itemId == com.SachinApps.Whatscan.Pro.WhatsClone.R.id.menu_rate) {
            ;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + menuItem)));

        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 1000) {
            String str = "";
            int i2 = 0;
            for (String str2 : strArr) {
                str = iArr[i2] == 0 ? "GRANTED" : "DENIED";
                i2++;
            }
            if (str.equals("DENIED")) {
                Toast.makeText(this, "Please allow all permission in your app settings", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Please allow all permission in your app settings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
            } else if (str.equals("GRANTED")) {
                finish();
                startActivity(getIntent());
            }
        } else if (i == 004) {
            Log.d("TAG", "onRequestPermissionsResult: ");
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 004 && resultCode == RESULT_OK) {
            Uri dataUri = data.getData();
            if (dataUri.toString().contains(".Statuses")) {
                getContentResolver().takePersistableUriPermission(dataUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    permissionTV.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    new LoadAllFiles().execute();
                }
            } else {

            }
        }

    }


    class LoadAllFiles extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... furl) {
            DocumentFile documentFile = DocumentFile.fromTreeUri(activity, activity.getContentResolver().getPersistedUriPermissions().get(0).getUri());
            for (DocumentFile file : documentFile.listFiles()) {
                if (file.isDirectory()) {

                } else {
                    if (!file.getName().equals(".nomedia")) {


                        fileArrayList.add(file.getUri());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {

            Log.d("TAG", "onProgressUpdate: ");
        }

        @Override
        protected void onPostExecute(String fileUrl) {
            permissionTV.setVisibility(View.GONE);
            pager_adapter_view = new WhstWebPagerAdapter(getSupportFragmentManager());
            pager_adapter_view.add_fragments(new StatusSaverImagesSaver(fileArrayList), "Status Images");
            pager_adapter_view.add_fragments(new StatusSaverVideosSaver(fileArrayList), "Status Videos");
            view_Pager.setAdapter(pager_adapter_view);
            tl.setupWithViewPager(view_Pager);
            Log.d("TAG", "onPostExecute: ");
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            permissionTV.setVisibility(View.GONE);
        }
    }
}