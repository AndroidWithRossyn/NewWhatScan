package com.SachinApps.Whatscan.Pro.WhatsClone;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models.WhatsappStatusModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class StatusSaverVideoRecycler extends Adapter<StatusSaverVideoRecycler.MyViewHolder> {
    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/storage/emulated/0/WA Status Saver/Status Videos/";
    public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Whatsapp");
    Activity activity;
    private ArrayList<File> filesList;

    ImageView iv;

    String fileName = "";
    private int nextPos = 0;
    ImageButton play_btn;
    ProgressBar pb;
    TextView tv;

    ArrayList<WhatsappStatusModel> fileArrayList;


    public StatusSaverVideoRecycler(ArrayList<WhatsappStatusModel> statusModelArrayList, FragmentActivity activity) {
        this.fileArrayList = statusModelArrayList;
        this.activity = activity;
    }


    public class MyViewHolder extends ViewHolder {
        TextView save;
        ImageView image_thumbnail;

        public MyViewHolder(View view) {
            super(view);
            this.image_thumbnail = (ImageView) view.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.video_thumbnail);
            this.save = (TextView) view.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.save_video);
        }
    }

    public StatusSaverVideoRecycler(ArrayList<File> arrayList, Activity activity) {
        this.filesList = arrayList;
        this.activity = activity;

    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(com.SachinApps.Whatscan.Pro.WhatsClone.R.layout.whatsappscanvideos_item_view, viewGroup, false));
    }

    private void LoadInterstitialAds() {

    }

    public void ShowInterstitialAds(){


    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        WhatsappStatusModel statusModel = null;
        File file = null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.Q){
             statusModel = fileArrayList.get(i);
            Glide.with(this.activity).load(statusModel.getUri()).into(myViewHolder.image_thumbnail);
        }else{

            file = (File) this.filesList.get(i);
            File finalFile1 = file;
            Glide.with(this.activity).load(Uri.fromFile(new File(file.getAbsolutePath()))).into(myViewHolder.image_thumbnail);
            myViewHolder.image_thumbnail.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    StatusSaverVideoRecycler.this.nextPos = StatusSaverVideoRecycler.this.filesList.indexOf(finalFile1);
                    final Builder view1 = new Builder(StatusSaverVideoRecycler.this.activity);
                    final View inflate = LayoutInflater.from(StatusSaverVideoRecycler.this.activity).inflate(com.SachinApps.Whatscan.Pro.WhatsClone.R.layout.whatsappscanimage_view, null);
                    StatusSaverVideoRecycler.this.iv = (ImageView) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.dialog_image);
                    StatusSaverVideoRecycler.this.tv = (TextView) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.long_press_msg);
                    StatusSaverVideoRecycler.this.tv.setVisibility(View.GONE);
                    StatusSaverVideoRecycler.this.play_btn = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.bhagva_play);
                    StatusSaverVideoRecycler.this.play_btn.setVisibility(View.VISIBLE);

                    ShowInterstitialAds();
                    Glide.with(StatusSaverVideoRecycler.this.activity).load(finalFile1.getAbsolutePath()).skipMemoryCache(false).into(StatusSaverVideoRecycler.this.iv);
                    view1.setView(inflate);
                    view1.setCancelable(true);
                    ImageButton imageButton = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.nav_forward);
                    ImageButton imageButton2 = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.nav_back);
                    ImageButton imageButton3 = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.save);
                    StatusSaverVideoRecycler.this.pb = (ProgressBar) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.loading);
                    ImageButton imageButton4 = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.share);
                    StatusSaverVideoRecycler.this.play_btn.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            view = StatusSaverVideoRecycler.this.pb;
                            View view2 = inflate;
                            view.setVisibility(View.VISIBLE);
                            String view3 = finalFile1.getAbsolutePath().toString();
                            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(view3));
                            intent.setDataAndType(Uri.parse(view3), "video/*");
                            StatusSaverVideoRecycler.this.activity.startActivity(intent);
                        }
                    });
                    imageButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            view = StatusSaverVideoRecycler.this.pb;
                            View view2 = inflate;
                            view.setVisibility(View.GONE);
                            StatusSaverVideoRecycler.this.nextPos = StatusSaverVideoRecycler.this.nextPos + 1;
                            if (StatusSaverVideoRecycler.this.nextPos < StatusSaverVideoRecycler.this.filesList.size()) {
                                Glide.with(StatusSaverVideoRecycler.this.activity).load(((File) StatusSaverVideoRecycler.this.filesList.get(StatusSaverVideoRecycler.this.nextPos)).getAbsolutePath()).skipMemoryCache(false).into(StatusSaverVideoRecycler.this.iv);
                                return;
                            }
                            StatusSaverVideoRecycler.this.nextPos = StatusSaverVideoRecycler.this.nextPos - 1;
                            Toast.makeText(StatusSaverVideoRecycler.this.activity, "No further video", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imageButton2.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            view = StatusSaverVideoRecycler.this.pb;
                            View view2 = inflate;
                            view.setVisibility(View.GONE);
                            StatusSaverVideoRecycler.this.nextPos = StatusSaverVideoRecycler.this.nextPos - 1;
                            if (StatusSaverVideoRecycler.this.nextPos >= 0) {
                                Glide.with(StatusSaverVideoRecycler.this.activity).load(((File) StatusSaverVideoRecycler.this.filesList.get(StatusSaverVideoRecycler.this.nextPos)).getAbsolutePath()).skipMemoryCache(false).into(StatusSaverVideoRecycler.this.iv);
                                return;
                            }
                            StatusSaverVideoRecycler.this.nextPos = 0;
                            Toast.makeText(StatusSaverVideoRecycler.this.activity, "No previous video", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imageButton4.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            Uri view4 = FileProvider.getUriForFile(StatusSaverVideoRecycler.this.activity, "com.SachinApps.whatscan.pro.WhatWIthWebScan.fileprovider", finalFile1);
                            Uri view3 = null;
                            try {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                                String sAux = "\nLet me recommend you this application\n\n";
                                sAux = sAux + "https://play.google.com/store/apps/details?id="+activity.getPackageName();
                                i.putExtra(Intent.EXTRA_TEXT, sAux);
                                activity.startActivity(i);
                            } catch(Exception e) {
                                //e.toString();
                            }
                            //                QRCode_videoRecycler.this.activity.startActivity(Intent.createChooser(intent, "Share to"));
                            view = StatusSaverVideoRecycler.this.pb;
                            View view2 = inflate;
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                    imageButton3.setOnClickListener(new OnClickListener()  {
                        public void onClick(View view) {
                            ProgressBar progressBar = StatusSaverVideoRecycler.this.pb;
                            View view2 = inflate;
                            progressBar.setVisibility(View.GONE);
                            try {
                                StatusSaverVideoRecycler.this.save_video((File) StatusSaverVideoRecycler.this.filesList.get(StatusSaverVideoRecycler.this.nextPos), view);
                            } catch (IOException e) {

                            }
                        }
                    });
                    view1.show();
                }
            });

        }

        WhatsappStatusModel finalStatusModel = statusModel;
        File finalFile = file;
        myViewHolder.save.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT>Build.VERSION_CODES.Q){
                        try {
                            if (finalStatusModel.getUri().toString().endsWith(".mp4")) {
                                fileName= "status_"+System.currentTimeMillis()+".mp4";
                                new DownloadFileTask().execute(finalStatusModel.getUri().toString());
                                Toast.makeText(activity, "Download Complete", Toast.LENGTH_SHORT).show();
                            }else {
                                fileName = "status_"+System.currentTimeMillis()+".png";
                                new DownloadFileTask().execute(finalStatusModel.getUri().toString());
                                Toast.makeText(activity, "Download Complete", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        StatusSaverVideoRecycler.this.save_video(finalFile, view);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }



    public void save_video(File file, View view) throws IOException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(DIRECTORY_TO_SAVE_MEDIA_NOW);
            stringBuilder.append(file.getName());
            copyFile(file, new File(stringBuilder.toString()), this.activity);
        } catch (Throwable file2) {
            file2.printStackTrace();
        }
        Snackbar file2 = Snackbar.make(view, (CharSequence) "Video Saved Successfully", BaseTransientBottomBar.LENGTH_LONG);
        view = file2.getView();
        view.setBackgroundColor(this.activity.getResources().getColor(com.SachinApps.Whatscan.Pro.WhatsClone.R.color.colorPrimaryDark));
        file2.show();
    }

    public int getItemCount() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            return this.fileArrayList.size();
        }else {
            return this.filesList.size();
        }
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
            FileChannel file1=null;
            channel = file1;
            if (file1 != null) {
                file1.close();
            }
            if (channel != null) {
                channel.close();
            }
            throw file22;
        }
    }

    class DownloadFileTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... furl) {
            try {
                // Ensure that the directory exists or create it

                if (!RootDirectoryWhatsappShow.exists()) {
                    RootDirectoryWhatsappShow.mkdirs(); // Creates directories including any necessary but nonexistent parent directories.
                }

                InputStream in = activity.getContentResolver().openInputStream(Uri.parse(furl[0]));
                File f = new File(RootDirectoryWhatsappShow+File.separator+fileName); // Use the directory when creating the file
                f.setWritable(true, false);
                OutputStream outputStream = new FileOutputStream(f);
                byte buffer[] = new byte[1024];
                int length = 0;

                while ((length = in.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                in.close();


            } catch (IOException e) {
                System.out.println("error in creating a file");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            // Update progress if needed
        }

        @Override
        protected void onPostExecute(String fileUrl) {
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    MediaScannerConnection.scanFile(activity, new String[]
                                    {new File(DIRECTORY_TO_SAVE_MEDIA_NOW, fileName).getAbsolutePath()},
                            null, (path, uri) -> {
                                // no action
                            });
                } else {
                    activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED",
                            Uri.fromFile(new File(DIRECTORY_TO_SAVE_MEDIA_NOW, fileName))));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}