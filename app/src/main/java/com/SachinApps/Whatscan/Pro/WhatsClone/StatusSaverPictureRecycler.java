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

public class StatusSaverPictureRecycler extends Adapter<StatusSaverPictureRecycler.MyViewHolder> {
    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/storage/emulated/0/WA Status Saver/Status Pictures/";

    public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Whatsapp");
    Activity activity;
    public static ArrayList<File> filesList;
    ArrayList<WhatsappStatusModel> fileArrayList;
    ImageView iv;

    String fileName = "";
    private int nextPos = 0;
    ImageButton play_btn;
    ProgressBar pb;
    TextView tv;



    public StatusSaverPictureRecycler(ArrayList<WhatsappStatusModel> statusModelArrayList, FragmentActivity activity) {
        this.fileArrayList = statusModelArrayList;
        this.activity =activity;
    }

    public class MyViewHolder extends ViewHolder {
        TextView save;
        ImageView image_thumbnail;

        public MyViewHolder(View view) {
            super(view);
            this.image_thumbnail = (ImageView) view.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.pictures_thumbnail);
            this.save = (TextView) view.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.save_picture);
        }
    }

    public StatusSaverPictureRecycler(ArrayList<File> arrayList, Activity activity) {
        this.filesList = arrayList;
        this.activity = activity;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(com.SachinApps.Whatscan.Pro.WhatsClone.R.layout.whatsappscanpictures_item_view, viewGroup, false));
    }



    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        WhatsappStatusModel statusModel = null;
        File file = null;

        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.Q){
             statusModel = fileArrayList.get(i);
            Glide.with(this.activity).load(statusModel.getUri()).into(myViewHolder.image_thumbnail);
        }else{
            file = (File) this.filesList.get(i);
            Glide.with(this.activity).load(Uri.fromFile(new File(file.getAbsolutePath()))).into(myViewHolder.image_thumbnail);
            File finalFile1 = file;
            myViewHolder.image_thumbnail.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    StatusSaverPictureRecycler.this.nextPos = StatusSaverPictureRecycler.this.filesList.indexOf(finalFile1);
                    final Builder view1 = new Builder(StatusSaverPictureRecycler.this.activity);
                    final View inflate = LayoutInflater.from(StatusSaverPictureRecycler.this.activity).inflate(com.SachinApps.Whatscan.Pro.WhatsClone.R.layout.whatsappscanimage_view, null);
                    StatusSaverPictureRecycler.this.iv = (ImageView) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.dialog_image);
                    StatusSaverPictureRecycler.this.tv = (TextView) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.long_press_msg);
                    StatusSaverPictureRecycler.this.tv.setVisibility(View.GONE);
                    StatusSaverPictureRecycler.this.play_btn = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.bhagva_play);
                    StatusSaverPictureRecycler.this.play_btn.setVisibility(View.VISIBLE);
//                WhstWebPickAds.loadAdmobBannerAds((AdView) inflate.findViewById(R.id.adView));

                    Glide.with(StatusSaverPictureRecycler.this.activity).load(finalFile1.getAbsolutePath()).skipMemoryCache(false).into(StatusSaverPictureRecycler.this.iv);
                    view1.setView(inflate);
                    view1.setCancelable(true);
                    ImageButton imageButton = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.nav_forward);
                    ImageButton imageButton2 = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.nav_back);
                    ImageButton imageButton3 = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.save);
                    StatusSaverPictureRecycler.this.pb = (ProgressBar) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.loading);
                    ImageButton imageButton4 = (ImageButton) inflate.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.share);
                    StatusSaverPictureRecycler.this.play_btn.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            view = StatusSaverPictureRecycler.this.pb;
                            View view2 = inflate;
                            view.setVisibility(View.VISIBLE);
                            String view3 = finalFile1.getAbsolutePath().toString();
                            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(view3));
                            intent.setDataAndType(Uri.parse(view3), "image/*");
                            StatusSaverPictureRecycler.this.activity.startActivity(intent);
                        }
                    });
                    imageButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            view = StatusSaverPictureRecycler.this.pb;
                            View view2 = inflate;
                            view.setVisibility(View.GONE);
                            StatusSaverPictureRecycler.this.nextPos = StatusSaverPictureRecycler.this.nextPos + 1;
                            if (StatusSaverPictureRecycler.this.nextPos < StatusSaverPictureRecycler.this.filesList.size()) {
                                Glide.with(StatusSaverPictureRecycler.this.activity).load(((File) StatusSaverPictureRecycler.this.filesList.get(StatusSaverPictureRecycler.this.nextPos)).getAbsolutePath()).skipMemoryCache(false).into(StatusSaverPictureRecycler.this.iv);
                                return;
                            }
                            StatusSaverPictureRecycler.this.nextPos = StatusSaverPictureRecycler.this.nextPos - 1;
                            Toast.makeText(StatusSaverPictureRecycler.this.activity, "No further video", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imageButton2.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            view = StatusSaverPictureRecycler.this.pb;
                            View view2 = inflate;
                            view.setVisibility(View.GONE);
                            StatusSaverPictureRecycler.this.nextPos = StatusSaverPictureRecycler.this.nextPos - 1;
                            if (StatusSaverPictureRecycler.this.nextPos >= 0) {
                                Glide.with(StatusSaverPictureRecycler.this.activity).load(((File) StatusSaverPictureRecycler.this.filesList.get(StatusSaverPictureRecycler.this.nextPos)).getAbsolutePath()).skipMemoryCache(false).into(StatusSaverPictureRecycler.this.iv);
                                return;
                            }
                            StatusSaverPictureRecycler.this.nextPos = 0;
                            Toast.makeText(StatusSaverPictureRecycler.this.activity, "No previous video", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imageButton4.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            Uri view4 = FileProvider.getUriForFile(StatusSaverPictureRecycler.this.activity, "com.SachinApps.whatscan.pro.WhatWIthWebScan.fileprovider", finalFile1);
                            Uri view3 = null;
//                        WhstWebPicture_Recycler.this.activity.grantUriPermission(WhstWebPicture_Recycler.this.activity.getPackageName(), view3, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            //                      Intent intent = new Intent("android.intent.action.SEND");
                            //                    intent.setType("video/*");
                            //                  intent.putExtra("android.intent.extra.STREAM", view4);
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
                            //                WhstWebPicture_Recycler.this.activity.startActivity(Intent.createChooser(intent, "Share to"));
                            view = StatusSaverPictureRecycler.this.pb;
                            View view2 = inflate;
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                    imageButton3.setOnClickListener(new OnClickListener()  {
                        public void onClick(View view) {
                            ProgressBar progressBar = StatusSaverPictureRecycler.this.pb;
                            View view2 = inflate;
                            progressBar.setVisibility(View.GONE);
                            try {
                                StatusSaverPictureRecycler.this.save_picture((File) StatusSaverPictureRecycler.this.filesList.get(StatusSaverPictureRecycler.this.nextPos), view);
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
                        save_picture(finalFile,view);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }



    public void save_picture(File file, View view) throws IOException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(DIRECTORY_TO_SAVE_MEDIA_NOW);
            stringBuilder.append(file.getName());
            copyFile(file, new File(stringBuilder.toString()), this.activity);
        } catch (Throwable file2) {
            file2.printStackTrace();
        }
        Snackbar file2 = Snackbar.make(view, (CharSequence) "Picture Saved Successfully", BaseTransientBottomBar.LENGTH_LONG);
        view = file2.getView();
        view.setBackgroundColor(this.activity.getResources().getColor(com.SachinApps.Whatscan.Pro.WhatsClone.R.color.colorPrimaryDark));
//        TextView textView = (TextView) view.findViewById(com.whatscan.whatsweb.whatzweb.whatwebscan.R.id.snackbar_text);
//        textView.setTextColor(this.activity.getResources().getColor(com.whatscan.whatsweb.whatzweb.whatwebscan.R.color.white));
//        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.whatscan.whatsweb.whatzweb.whatwebscan.R.drawable.image_iconic_done_black_24dp, 0);
        file2.show();
    }

    public int getItemCount() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            return this.fileArrayList.size();
        }else{
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
            try{

                if (!RootDirectoryWhatsappShow.exists()) {
                    RootDirectoryWhatsappShow.mkdirs(); // Creates directories including any necessary but nonexistent parent directories.
                }

                InputStream in = activity.getContentResolver().openInputStream(Uri.parse(furl[0]));
                File f =  null;
                f = new File(RootDirectoryWhatsappShow + File.separator + fileName);
                f.setWritable(true, false);
                OutputStream outputStream = new FileOutputStream(f);
                byte buffer[] = new byte[1024];
                int length = 0;

                while((length=in.read(buffer)) > 0) {
                    outputStream.write(buffer,0,length);
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

        }
        @Override
        protected void onPostExecute(String fileUrl) {

            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    MediaScannerConnection.scanFile(activity, new String[]
                                    {new File(DIRECTORY_TO_SAVE_MEDIA_NOW+File.separator+fileName).getAbsolutePath()},
                            null, (path, uri) -> {
                                //no action
                            });
                } else {
                    activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED",
                            Uri.fromFile(new File(DIRECTORY_TO_SAVE_MEDIA_NOW+File.separator+fileName))));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
