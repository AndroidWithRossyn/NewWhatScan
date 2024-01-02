package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters.AppListAdapter;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.db.recentNumberDB;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;
import io.github.dreierf.materialintroscreen.animations.IViewTranslation;

public class IntroActivit extends MaterialIntroActivity {

    private boolean asking = false;

    private ArrayList<String> addedpackages;
    private ArrayList<String> addedpackagestocompare;
    private AppListAdapter appListAdapter;
    private ArrayList<String> apppacklist;
    private Handler handler;
    private int key = 0;
    private LinearLayout main;
    private Utils utils;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final Intent[] AUTO_START_INTENTS = {
            new Intent().setComponent(new ComponentName("com.samsung.android.lool",
                    "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT),
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(
                    Uri.parse("mobilemanager://function/entry/AutoStart"))
    };
    private boolean getInstalledInfo(String s) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean notystem(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & 1) != 0;
    }

    private void saveSetup() {
        SharedPreferences.Editor edit = getSharedPreferences("SETUP", 0).edit();
        edit.putBoolean("setup", true);
        edit.apply();

    }

    private void saveTodb() {
        new bg().execute();
    }

    private void setUpAppList() {
        main.removeAllViews();
         new AsyncLayoutInflater(this).inflate(R.layout.app_list_recycler, new LinearLayout(this), new AsyncLayoutInflater.OnInflateFinishedListener() {
            public void onInflateFinished(@NonNull View view, int i, @Nullable ViewGroup viewGroup) {
                 main.addView(view);
                setupAppListRecycler(view.findViewById(R.id.recycle), findViewById(R.id.button));
            }
        });
    }

    private void setupAppListRecycler(RecyclerView recyclerView, Button button) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String[] strArr = new String[]{"com.whatsapp", "com.whatsapp.w4b", "com.gbwhatsapp", "com.facebook.lite", "com.facebook.orca", "com.facebook.mlite", "org.telegram.messenger"};
        new Thread(new Runnable() {
            public void run() {
                List arrayList = new ArrayList();
                List arrayList2 = new ArrayList();
                PackageManager packageManager = getPackageManager();
                List installedPackages = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
                int i = 0;
                int i2 = 0;
                while (true) {
                    if (i2 >= strArr.length) {
                        break;
                    }
                    if (getInstalledInfo(strArr[i2])) {
                        try {
                            arrayList.add(packageManager.getPackageInfo(strArr[i2], PackageManager.GET_PERMISSIONS));
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    i2++;
                }
                for (i2 = 0; i2 < installedPackages.size(); i2++) {
                    String str = ((PackageInfo) installedPackages.get(i2)).packageName;
                    int i3 = 0;
                    while (true) {
                        String[] strArr2 = strArr;
                        if (i3 >= strArr2.length) {
                            break;
                        }
                        if (str.contains(strArr2[i3])) {
                            installedPackages.remove(i2);
                        }
                        i3++;
                    }
                }
                while (i < installedPackages.size()) {
                    if (!(notystem((PackageInfo) installedPackages.get(i)) || arrayList.contains(installedPackages.get(i)))) {
                        arrayList.add(installedPackages.get(i));
                    }
                    i++;
                }
                if (key == 1) {
                    Iterator it = new recentNumberDB(getApplicationContext()).getAllPackages().iterator();
                    while (it.hasNext()) {
                        String str2 = (String) it.next();
                        try {
                            arrayList2.add(packageManager.getPackageInfo(str2, PackageManager.GET_PERMISSIONS));
                            apppacklist.add(str2);
                            addedpackages.add(str2);
                            addedpackagestocompare.add(str2);
                        } catch (PackageManager.NameNotFoundException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                appListAdapter = new AppListAdapter(arrayList, IntroActivit.this, addedpackages);
                handler.post(new Runnable() {
                    public void run() {
                        recyclerView.setAdapter(appListAdapter);
                         button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                if (apppacklist.size() > 0) {
                                    saveTodb();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Add atleast one application", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }
    private void terms_setup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IntroActivit.this);
        alertDialog.setTitle("Terms & Conditions");
        alertDialog.setMessage(Html.fromHtml(getString(R.string.app_name) + " is a backup &amp; utility app. It is designed to provide backup services for notifications, specific location of storage &amp; some utility features. This app uses common Android APIs to achieve services. It's not designed to interfere with other apps or services.<br>However the use of app may be incompatible with terms of use of other apps. If this kind of incompatibility occurs, you shall not use this app.<br><br>Using this app you accept its <a href='#'>Terms &amp; Conditions</a> and <a href='#'>Privacy Policy</a>"));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int n) {
                dialogInterface.cancel();
            }
        });
        alertDialog.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
        ((TextView) alertDialog1.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void addtolist(String str) {
        if (apppacklist.contains(str)) {
            apppacklist.remove(str);
        } else {
            apppacklist.add(str);
        }
        String sb = " size " + apppacklist.size();
        Log.d("addlistlog", sb);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);
        apppacklist = new ArrayList<String>();
         handler = new Handler();
        utils = new Utils((Activity) this);
        addedpackages = new ArrayList<String>();
        addedpackagestocompare = new ArrayList<String>();
        key = getIntent().getIntExtra("key", 0);
//        if (key == 1) {
//            setUpAppList();
//        } else {
//            setUpAppList();
        terms_setup();
//        }
        new bg().execute();
        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        .image(R.drawable.logo)
                        .title("Notification Access is necessary to save and detect notifications")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setupNotificationPermission();
                    }
                }, "Allow"));



        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            addSlide(new SlideFragmentBuilder()
                            .backgroundColor(R.color.third_slide_background)
                            .buttonsColor(R.color.third_slide_buttons)
                            .possiblePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                            .neededPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                            .image(R.drawable.logo)
                            .title("We Need storage permission to recover deleted media")
                            .description("ever")
                            .build(),
                    new MessageButtonBehaviour(new View.OnClickListener() {
                        @SuppressLint("UseCheckPermission")
                        @Override
                        public void onClick(View v) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                checkPermission(
                                        Manifest.permission.READ_MEDIA_IMAGES);
                            }else{
                                checkPermission(
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }

                        }
                    }, "Allow"));

            addSlide(new SlideFragmentBuilder()
                            .backgroundColor(R.color.third_slide_background)
                            .buttonsColor(R.color.third_slide_buttons)
                            .possiblePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                            .neededPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                            .image(R.drawable.logo)
                            .title("Allow Battery usage for keeping the app running")
                            .description("ever")
                            .build(),
                    new MessageButtonBehaviour(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (Intent intent : AUTO_START_INTENTS) {
                                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                                    startActivity(intent);
                                    break;
                                }
                                else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        String packageName = getPackageName();
                                        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                                        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                                            Intent intent1 = new Intent();
                                            intent1.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                                            intent1.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                            intent1.setData(Uri.parse("package:" + packageName));
                                            startActivity(intent1);
                                        }
                                    }
                                }
                            }

                        }
                    }, "Allow"));

        }




//        public static void openAppSettings(Context context) {
//            Intent intent = new Intent();
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//                intent.setData(uri);
//            } else {
//                // For older versions of Android, use the app settings intent
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.setData(Uri.parse("package:" + context.getPackageName()));
//            }
//
//            // Check if the intent can be resolved before starting the activity
//            if (intent.resolveActivity(context.getPackageManager()) != null) {
//                context.startActivity(intent);
//            }
//        }


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.fourth_slide_background)
                .buttonsColor(R.color.fourth_slide_buttons)
                .title("That's it")
                .build());
    }


    public void checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        STORAGE_PERMISSION_CODE);
            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        STORAGE_PERMISSION_CODE);
            }
        } else {
            Toast.makeText(this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            // Check if the permission is granted
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                                "Permission granted",
                                Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(this,
                                "Permission denied",
                                Toast.LENGTH_SHORT)
                        .show();
                // Handle the case where the user denied the permission
            }
        }
    }
    public class bg extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... array) {
            recentNumberDB recentNumberDB = new recentNumberDB(IntroActivit.this);
            recentNumberDB.addPackages("com.whatsapp");
            return null;
        }

        protected void onPostExecute(Void void1) {
            super.onPostExecute(void1);
            SharedPreferences.Editor edit = getSharedPreferences("SETUP", 0).edit();
            edit.putBoolean("setup", true);
            edit.apply();
//            setupNotificationPermission();
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (asking && utils.isNotificationEnabled()) {
//            saveSetup();
            Intent intent = new Intent(getString(R.string.noti_obserb));
            intent.putExtra(getString(R.string.noti_obserb), "update");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }
    private void setupNotificationPermission() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IntroActivit.this);
        alertDialog.setTitle("Notification Access");
        alertDialog.setMessage(Html.fromHtml("Notification access allows " + getString(R.string.app_name) + " to read and backup deleted messages &amp; media."));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int n) {
                asking = true;
                try {
                    if (Build.VERSION.SDK_INT >= 22) {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    } else {
                        try {
                            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        } catch (Exception ex) {
                            finish();
                            startActivity(new Intent(IntroActivit.this, HomeActivity.class));
                        }
                    }
                } catch (Exception ex2) {
                    finish();
                    startActivity(new Intent(IntroActivit.this, HomeActivity.class));
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }

    @Override
    public void onFinish() {
        super.onFinish();
        SharedPreferences.Editor editor = getSharedPreferences("shared", MODE_PRIVATE).edit();
        editor.putInt("idName", 1);
        editor.apply();
        startActivity(new Intent(IntroActivit.this, HomeActivity.class));
        finish();
    }
}
