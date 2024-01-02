package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.IntroActivit;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends Adapter<ViewHolder> {
    private List<String> addedlist;
    private ArrayList<Boolean> checklist = new ArrayList<Boolean>();
    private IntroActivit context;
    Drawable drawable;
    private List<PackageInfo> list_info;
    private PackageManager packageManager;

    private class AppHolder extends ViewHolder {
        ImageView check;
        ImageView icon;
        LinearLayout main;
        TextView name;

        public AppHolder(@NonNull View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            name = (TextView) view.findViewById(R.id.name);
            check = (ImageView) view.findViewById(R.id.check);
            main = (LinearLayout) view.findViewById(R.id.main);
        }
    }

    public AppListAdapter(List<PackageInfo> list, IntroActivit setup, List<String> list2) {
        this.list_info = list;
        this.context = setup;
        this.addedlist = list2;
        for (int i = 0; i < list.size(); i++) {
            this.checklist.add(false);
        }
        this.packageManager = setup.getPackageManager();
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AppHolder(LayoutInflater.from(context).inflate(R.layout.item_app_list, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AppHolder appHolder = (AppHolder) viewHolder;
        appHolder.name.setText(packageManager.getApplicationLabel((list_info.get(i)).applicationInfo).toString());
        appHolder.icon.setImageDrawable(packageManager.getApplicationIcon((list_info.get(i)).applicationInfo));
        boolean contains = addedlist.contains((list_info.get(i)).packageName);
        String str1 = "contlog";
        if (contains) {
            checklist.set(i, contains);
            addedlist.remove((list_info.get(i)).packageName);
        } else {
            Log.d(str1, "fal");
        }

        if (checklist.get(i)) {
            Glide.with(context).load(R.drawable.correct).into(appHolder.check);
        } else {
            Glide.with(context).load(R.drawable.correct).into(appHolder.check);
        }
        appHolder.main.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String sb = "clicked " + viewHolder.getAdapterPosition();
                String str = "cheloh";
                Log.d(str, sb);
                if (checklist.get(viewHolder.getAdapterPosition())) {
                    checklist.set(viewHolder.getAdapterPosition(), false);
                    Glide.with(context).load(com.hbb20.R.drawable.flag_cuba).into(appHolder.check);
                } else {
                    Glide.with(context).load(R.drawable.correct).into(appHolder.check);
                    checklist.set(viewHolder.getAdapterPosition(), true);
                    String str2 = "click = " + viewHolder.getAdapterPosition() + " " + checklist.get(viewHolder.getAdapterPosition());
                    Log.d(str, str2);
                }
                context.addtolist((list_info.get(viewHolder.getAdapterPosition())).packageName);
            }
        });
    }

    public int getItemCount() {
        return list_info.size();
    }
}
