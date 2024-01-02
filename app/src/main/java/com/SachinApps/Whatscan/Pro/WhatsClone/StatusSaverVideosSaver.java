package com.SachinApps.Whatscan.Pro.WhatsClone;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models.WhatsappStatusModel;

import java.io.File;
import java.util.ArrayList;

public class StatusSaverVideosSaver extends Fragment {
    private static final String WHATSAPP_STATUSES_LOCATION = "/storage/emulated/0/WhatsApp/Media/.Statuses";
    LayoutManager lm;
    RecyclerView rv;

    private ArrayList<Uri> fileArrayList;
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    public StatusSaverVideosSaver(ArrayList<Uri> fileArrayList) {
        this.fileArrayList = fileArrayList;
    }

    public StatusSaverVideosSaver() {

    }


    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View layoutInflater1 = layoutInflater.inflate(com.SachinApps.Whatscan.Pro.WhatsClone.R.layout.whatsappscanfragment_videos, viewGroup, false);
        this.rv = (RecyclerView) layoutInflater1.findViewById(com.SachinApps.Whatscan.Pro.WhatsClone.R.id.videos_recyclerView);
        this.lm = new GridLayoutManager(getContext(), 3);
        this.rv.setLayoutManager(this.lm);
        this.rv.addItemDecoration(new WhstWebItemDecoration(getResources().getDimensionPixelSize(com.SachinApps.Whatscan.Pro.WhatsClone.R.dimen.photos_list_spacing), 2));
        getListFiles(new File(WHATSAPP_STATUSES_LOCATION));

        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.Q){
            getData();
        }else{
            this.rv.setAdapter(new StatusSaverVideoRecycler(getListFiles(new File(WHATSAPP_STATUSES_LOCATION)), getActivity()));
        }



        return layoutInflater1;
    }

    private ArrayList<File> getListFiles(File file) {
        ArrayList<File> arrayList = new ArrayList();
        File[] file1 = file.listFiles();
        if (file1 != null) {
            for (File file2 : file1) {
                if ((file2.getName().endsWith(".mp4") || file2.getName().endsWith(".gif")) && !arrayList.contains(file2)) {
                    arrayList.add(file2);
                }
            }
        }
        return arrayList;
    }

    private void getData() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                statusModelArrayList = new ArrayList<>();

                for (int i = 0; i < fileArrayList.size(); i++) {
                    WhatsappStatusModel whatsappStatusModel;
                    Uri uri = fileArrayList.get(i);
                    if (uri.toString().endsWith(".mp4")) {
                        whatsappStatusModel = new WhatsappStatusModel("WhatsStatus: " + (i + 1),
                                uri,
                                new File(uri.toString()).getAbsolutePath(),
                                new File(uri.toString()).getName());
                        statusModelArrayList.add(whatsappStatusModel);
                    }
                }

                this.rv.setAdapter(new StatusSaverVideoRecycler(statusModelArrayList, getActivity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}