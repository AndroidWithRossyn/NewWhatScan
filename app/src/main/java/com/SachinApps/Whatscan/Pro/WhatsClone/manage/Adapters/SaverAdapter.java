package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models.files_model;

import java.io.File;
import java.util.List;

import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.App.adCount;

public class SaverAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<files_model> fileslist;

    private class itemHolder extends ViewHolder {
        ImageView icon;
        LinearLayout main;
        TextView name;
        ImageView play;
        TextView size;
        ImageView tick;
         LinearLayout saveimg;


        public itemHolder(@NonNull View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            main = (LinearLayout) view.findViewById(R.id.main);
            play = (ImageView) view.findViewById(R.id.play);
            tick = (ImageView) view.findViewById(R.id.tick);
            saveimg = (LinearLayout) view.findViewById(R.id.saveimg);
        }
    }

    public SaverAdapter(Context context, List<files_model> list) {
        this.context = context;
        this.fileslist = list;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new itemHolder(LayoutInflater.from(context).inflate(R.layout.grid_jtem, viewGroup, false));
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        itemHolder itemHolder = (SaverAdapter.itemHolder)viewHolder;
        files_model files_model = fileslist.get(i);

        Glide.with(context).load(files_model.getFile()).centerCrop().placeholder(R.drawable.image_icon_save_black_24dp).into(itemHolder.icon);

        String type = files_model.getType();

        itemHolder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adCount%4==0){
                 //NOIB
                }
                adCount++;
                File file = new File(files_model.getFile().getPath());
                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                if (type.equals("image")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.setDataAndType(uri, "image/*");
                    context.startActivity(intent);
                }
                else if (type.equals("video")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.setDataAndType(uri, "video/*");
                    context.startActivity(intent);
                }
            }
        });
        itemHolder.saveimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adCount%4==0){
                  //NoIB  AdmobHelper.showInterstitialAd(context, AdmobHelper.ADSHOWN);
                }
                adCount++;
                Uri mainUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", files_model.getFile());
                Intent sharingIntent;
                sharingIntent = new Intent("android.intent.action.SEND");
                sharingIntent.setType("image/*");
                sharingIntent.putExtra("android.intent.extra.STREAM", mainUri);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    context.startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public int getItemCount() {
        return fileslist.size();
    }

}
