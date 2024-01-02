package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters.SaverAdapter;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models.files_model;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Fragment_files extends Fragment {
    ArrayList<Boolean> booleanArrayList = new ArrayList();
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("fragfiles", "received broadcast:--> " + intent.getStringExtra("files"));
            if (intent.getStringExtra(context.getString(R.string.files)).equals(context.getString(R.string.refresh_files))) {
                backgroundtask(false);
            } else if (intent.getStringExtra(context.getString(R.string.files)).equals(context.getString(R.string.remove_permission_framgent))) {
                remove_permission_fragment();
            }
        }
    };
    Activity context;
    TextView emptyText;
    List<files_model> filesList;
    SaverAdapter files_adapter;
    LinearLayout linearLayout;
    LinearLayout permissionLayout;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    int size;
    Utils utils;

    public Fragment_files newInstance(String str) {
        Fragment_files view_deleted_messages_fragment_files = new Fragment_files();
        Bundle bundle = new Bundle();
        bundle.putString("pack", str);
        view_deleted_messages_fragment_files.setArguments(bundle);
        return view_deleted_messages_fragment_files;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        utils = new Utils(context);
        try {
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(context.getString(R.string.files)));
            if (VERSION.SDK_INT >= 23) {
                boolean checkStoragePermission = utils.checkStoragePermission();
                if (checkStoragePermission) {
                    backgroundtask(checkStoragePermission);
                } else {
                    utils.isNeedGrantPermission();
                }
            } else {
                backgroundtask(true);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = (Activity) getActivity();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void backgroundtask(final boolean b) {
        new doin_bg(b).execute();
    }

    private File[] getFiles() {
        File file;
        if (getArguments().getString("pack").equals("com.whatsapp")) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), context.getString(R.string.app_name));
        } else {
            final String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String sb = context.getString(R.string.app_name) + "/" + getArguments().getString("pack");
            file = new File(absolutePath, sb);
        }
        final int exists = file.exists() ? 1 : 0;
        if (exists != 0) {
            File[] listFiles;
            try {
                listFiles = file.listFiles();
            } catch (Exception ex) {
                listFiles = new File[0];
            }
            return listFiles;
        }
        return new File[exists];
    }

    private void remove_permission_fragment() {
        backgroundtask(true);
        sendBroadcast(true);
    }

    private void sendBroadcast(boolean z) {
        Intent intent = new Intent(context.getString(R.string.noti_obserb));
        intent.putExtra(context.getString(R.string.noti_obserb), String.valueOf(z));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private RecyclerView getRecylcerView() {
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new LayoutParams(-1, -1));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        return recyclerView;
    }

    private ProgressBar getProgressBar() {
        progressBar = new ProgressBar(context);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        progressBar.setLayoutParams(layoutParams);
        layoutParams.gravity = 17;
        return progressBar;
    }

    private TextView getEmptyText() {
        emptyText = new TextView(context);
        emptyText.setText(context.getString(R.string.only_empty_text));
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        emptyText.setLayoutParams(layoutParams);
        emptyText.setGravity(17);
        layoutParams.setMargins(8, 8, 8, 8);
        return emptyText;
    }

    class doin_bg extends AsyncTask<Void, Void, Void> {
        boolean load_data;

        doin_bg(final boolean load_data) {
            this.load_data = load_data;
        }

        protected Void doInBackground(final Void... array) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (load_data) {
                filesList = new ArrayList();
            } else {
                filesList.clear();
            }
            try {
                final List<File> list = Arrays.asList(getFiles());
                Collections.sort(list, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        final long n = o2.lastModified() - o1.lastModified();
                        if (n > 0L) {
                            return 1;
                        }
                        if (n == 0L) {
                            return 0;
                        }
                        return -1;
                    }
                });
                size = list.size();
                for (int i = 0; i < list.size(); ++i) {
                    final boolean directory = list.get(i).isDirectory();
                    if (!directory) {
                        final String name = list.get(i).getName();
                        String s;
                        if (!list.get(i).getName().endsWith(".jpg") && !list.get(i).getName().endsWith(".jpeg") && !list.get(i).getName().endsWith(".png")) {
                            if (!list.get(i).getName().endsWith(".mp4") && !list.get(i).getName().endsWith(".3gp")) {
                                s = "unknown";
                            } else {
                                s = "video";
                            }
                        } else {
                            s = "image";
                        }
                        final long n = list.get(i).length() / 1024L;
                        long n2 = 0L;
                        boolean b;
                        if (n > 1024L) {
                            n2 = n / 1024L;
                            b = directory;
                        } else {
                            b = true;
                        }
                        String s2;
                        if (b) {
                            String sb = String.valueOf(n) + " KB";
                            s2 = sb;
                        } else {
                            String sb2 = String.valueOf(n2) + " MB";
                            s2 = sb2;
                        }
                        filesList.add(new files_model(name, s, s2, (File) list.get(i), directory));
                    }
                }
                for (int j = 0; j < filesList.size(); ++j) {
                    booleanArrayList.add(false);
                }
                return null;
            } catch (Exception ex2) {
                return null;
            }
        }

        protected void onPostExecute(final Void void1) {
            super.onPostExecute(void1);
            if (load_data) {
                if (filesList != null && filesList.size() > 0) {
                    linearLayout.addView((View) getRecylcerView());
                    files_adapter = new SaverAdapter((Context) context, filesList);
                    recyclerView.setAdapter(files_adapter);
                    linearLayout.removeView((View) progressBar);
                } else {
                    linearLayout.removeView((View) progressBar);
                    linearLayout.addView((View) getEmptyText());
                }
            } else {
                if (recyclerView != null) {
                    files_adapter.notifyDataSetChanged();
                } else {
                    linearLayout.addView(getRecylcerView());
                    files_adapter = new SaverAdapter((Context) context, filesList);
                    recyclerView.setAdapter(files_adapter);
                    linearLayout.removeView((View) emptyText);
                }
            }
            Log.d("fflog", "below post");
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (load_data) {
                linearLayout.addView(getProgressBar());
            }
        }
    }
}
