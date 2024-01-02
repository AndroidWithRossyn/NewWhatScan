package com.SachinApps.Whatscan.Pro.WhatsClone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class WhstWebFullScreenImage extends AppCompatActivity {
    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/storage/emulated/0/WA Status Saver/Status Pictures/";
    ImageButton btn_left;
    ImageButton btn_right;
    ImageButton btn_save;
    ImageButton btn_share;


    int position;
    ProgressBar pb;
    private String position1;

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) com.SachinApps.Whatscan.Pro.WhatsClone.R.layout.whatsappscanactivity_full_screen_image);
        final ArrayList<File> bundle1 = StatusSaverPictureRecycler.filesList;
        this.position = getIntent().getIntExtra("pos", 0);
        final ImageView imageView = (ImageView) findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.img);
        this.btn_left = (ImageButton) findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.left);
        this.btn_right = (ImageButton) findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.right);
        this.btn_share = (ImageButton) findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.share_image);
        this.btn_save = (ImageButton) findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.save_image);

//        WhstWebPickAds.loadAdmobBannerAds((AdView) findViewById(R.id.adView));
        this.pb = (ProgressBar) findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.loading2);
        Glide.with((FragmentActivity) this).load(((File) bundle1.get(this.position)).getAbsolutePath()).skipMemoryCache(false).into(imageView);
        this.btn_left.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                WhstWebFullScreenImage.this.pb.setVisibility(View.GONE);
                WhstWebFullScreenImage view1 = WhstWebFullScreenImage.this;
                view1.position--;
                if (WhstWebFullScreenImage.this.position > 0) {
                    Glide.with(WhstWebFullScreenImage.this).load(((File) bundle1.get(WhstWebFullScreenImage.this.position)).getAbsolutePath()).skipMemoryCache(false).into(imageView);
                    return;
                }
                WhstWebFullScreenImage.this.position = 0;
                Toast.makeText(WhstWebFullScreenImage.this, "No previous image", Toast.LENGTH_SHORT).show();
            }
        });
        this.btn_right.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                WhstWebFullScreenImage.this.pb.setVisibility(View.GONE);
                WhstWebFullScreenImage view1 = WhstWebFullScreenImage.this;
                view1.position++;
                if (WhstWebFullScreenImage.this.position < bundle1.size()) {
                    Glide.with(WhstWebFullScreenImage.this).load(((File) bundle1.get(WhstWebFullScreenImage.this.position)).getAbsolutePath()).skipMemoryCache(false).into(imageView);
                    return;
                }
                WhstWebFullScreenImage view2 = WhstWebFullScreenImage.this;
                view2.position--;
                Toast.makeText(WhstWebFullScreenImage.this, "No further image", Toast.LENGTH_SHORT).show();
            }
        });
        this.btn_save.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                WhstWebFullScreenImage.this.pb.setVisibility(View.GONE);
                WhstWebFullScreenImage.this.savePicture((File) bundle1.get(WhstWebFullScreenImage.this.position), view);
            }
        });
        this.btn_share.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                File file = (File) bundle1.get(WhstWebFullScreenImage.this.position);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id="+getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                // WhstWebFull_Screen_Image.this.startActivity(Intent.createChooser(intent, "Share Image to"));
                WhstWebFullScreenImage.this.pb.setVisibility(View.VISIBLE);
            }
        });
    }

    private void LoadInterstitialAds() {



    }

    public void ShowInterstitialAds(){


    }

    public void savePicture(File file, View view) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(DIRECTORY_TO_SAVE_MEDIA_NOW);
            stringBuilder.append(file.getName());
            copyFile(file, new File(stringBuilder.toString()), this);
        } catch (Throwable file2) {
            file2.printStackTrace();
        }
        Snackbar file2 = Snackbar.make(view, (CharSequence) "Picture Saved Successfully", BaseTransientBottomBar.LENGTH_LONG);
        view = file2.getView();
        view.setBackgroundColor(getResources().getColor(com.SachinApps.Whatscan.Pro.WhatsClone.R.color.colorPrimaryDark));
//        TextView textView = (TextView) view.findViewById(com.whatscan.whatsweb.whatzweb.whatwebscan.R.id.snackbar_text);
//        textView.setTextColor(getResources().getColor(com.whatscan.whatsweb.whatzweb.whatwebscan.R.color.white));
//        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.whatscan.whatsweb.whatzweb.whatwebscan.R.drawable.image_iconic_done_black_24dp, 0);
        file2.show();
    }

    public static void copyFile(File file, File file2, Activity activity) throws Throwable {
        FileChannel channel;
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        try {
            FileChannel file1 = new FileInputStream(file).getChannel();
            try {
                channel = new FileOutputStream(file2).getChannel();
                try {
                    channel.transferFrom(file1, 0, file1.size());
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(file2));
                    activity.sendBroadcast(intent);
                    if (file1 != null) {
                        file1.close();
                    }
                    if (channel != null) {
                        channel.close();
                    }
                } catch (Throwable th) {
                    Throwable file22 = th;
                    if (file1 != null) {
                        file1.close();
                    }
                    if (channel != null) {
                        channel.close();
                    }
                    throw file22;
                }
            } catch (Throwable th2) {
                Throwable file22 = th2;
                channel = null;
                if (file1 != null) {
                    file1.close();
                }
                if (channel != null) {
                    channel.close();
                }
                throw file22;
            }
        } catch (Throwable th3) {
            Throwable file22 = th3;
            FileChannel file3 = null;
            channel = file3;
            if (file3 != null) {
                file3.close();
            }
            if (channel != null) {
                channel.close();
            }
            throw file22;
        }
    }

    public void onBackPressed() {

        super.onBackPressed();
    }


}