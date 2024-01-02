package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.db.recentNumberDB;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Fragments.HomeFragment;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Services.NotifyListener;

import java.io.File;
import java.util.ArrayList;

import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.App.adCount;

public class HomeActivity extends AppCompatActivity {

    private ViewPager pager;
    private HomeFragment wa_fragment_w;
    private RelativeLayout relativeLayout;
    private int PERMISSION_REQUEST_CODE = 111;



    private void ask() {
        try {
            if (Build.VERSION.SDK_INT >= 22) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            } else {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createFolder() {
        try {
            final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), getString(R.string.app_name));
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void navigationrelated() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupPager() {
        try {
            pager = (ViewPager) findViewById(R.id.pager);
            final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
            final ArrayList<String> allPackages = new recentNumberDB(this).getAllPackages();
            for (int i = 0; i < allPackages.size(); ++i) {
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo = null;
                packageInfo = packageManager.getPackageInfo((String) allPackages.get(i), PackageManager.GET_META_DATA);
                tabLayout.addTab(tabLayout.newTab().setText(packageManager.getApplicationLabel(packageInfo.applicationInfo)).setIcon(packageManager.getApplicationIcon(packageInfo.applicationInfo)));
            }
            tabLayout.setTabIndicatorFullWidth(false);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setTabTextColors(getResources().getColor(R.color.dinwhite), getResources().getColor(R.color.white));
            pager.setAdapter(new FragAdapter(getSupportFragmentManager(), allPackages));
            if (getIntent().getIntExtra("pos", 0) == 1) {
                pager.setCurrentItem(1);
            }
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (adCount % 4 == 0) {
                      //  ShowInterstitialAds();
                        //DoneIB
                    }
                    adCount++;
                    pager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            findViewById(R.id.progressBar2).setVisibility(View.GONE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class FragAdapter extends FragmentStatePagerAdapter {
        private ArrayList<String> packs;
        String selected;

        FragAdapter(FragmentManager fragmentManager, ArrayList<String> arrayList) {
            super(fragmentManager);
            packs = arrayList;
        }

        public Fragment getItem(int i) {
            Fragment fragment = null;
            wa_fragment_w = new HomeFragment().newInstance(packs.get(i));
            fragment = wa_fragment_w;

            return fragment;
        }

        public int getCount() {
            return packs.size();
        }
    }

    private void toggleNotificationListenerService() {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigationrelated();
                tryReconnectService();
                setupPager();
            }
        }, 1);
        RelativeLayout adLayout = findViewById(R.id.adLayout);

    }


    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == PERMISSION_REQUEST_CODE && iArr.length > 0) {
            if (iArr[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                createFolder();
                Intent intent = new Intent(getString(R.string.files));
                intent.putExtra(getString(R.string.files), getString(R.string.remove_permission_framgent));
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else {
                createFolder();
                final String format = String.format(getString(R.string.format_request_permision), getString(R.string.app_name));
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle((CharSequence) "Permission Required!");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage((CharSequence) format).setNeutralButton((CharSequence) "Grant", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, PERMISSION_REQUEST_CODE);
                    }
                }).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
              //  alertDialogBuilder.show();
            }
        }
    }
    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (Build.VERSION.SDK_INT >= 24) {
            NotificationListenerService.requestRebind(new ComponentName(getApplicationContext(), NotifyListener.class));
        }
    }



}
