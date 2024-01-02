package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters.UsersAdapter;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.db.recentNumberDB;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models.userModel;

import java.util.List;

public class ChatFragment extends Fragment {
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onreceivelog", intent.getStringExtra("refresh"));
            updatelist();
        }
    };
    private Context context;
    private Dialog dialog;
    private Handler handler;
    private LinearLayout linearLayout;
    private List<userModel> list;
    private String pack;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private LinearLayout waiting;
    private TextView emptyText;

    public ChatFragment newInstance(String str) {
        ChatFragment users_fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pack", str);
        users_fragment.setArguments(bundle);
        return users_fragment;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        pack = getArguments().getString("pack");
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter("refresh"));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        load();
        waiting = getView().findViewById(R.id.waiting);
    }

    private void load() {
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected Void doInBackground(Void... voidArr) {
                try {
                    Thread.sleep(200);
                    list = new recentNumberDB(context).getHomeList(getArguments().getString("pack"));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void voidR) {
                super.onPostExecute(voidR);
                if (list.size() > 0) {
                    linearLayout.addView(getRecylcerView());
                    usersAdapter = new UsersAdapter(context, list, pack);
                    recyclerView.setAdapter(usersAdapter);
                    if (waiting != null) {
                        linearLayout.removeView(waiting);
                    }
                    return;
                }
                addwaiting();
            }
        }.execute();
    }

    private void updatelist() {
        new AsyncTask<Void, Void, List<userModel>>() {
            protected List<userModel> doInBackground(Void... voidArr) {
                return new recentNumberDB(context).getHomeList(getArguments().getString("pack"));
            }

            @Override
            protected void onPostExecute(List<userModel> userModels) {
                super.onPostExecute(userModels);
                if (userModels!=null){
                    if (userModels.size() > 0) {
                        if (recyclerView == null) {
                            linearLayout.addView(getRecylcerView());
                            linearLayout.removeView(emptyText);
                        }
                        recyclerView.setAdapter(new UsersAdapter(context, userModels, pack));
                    }
                }
            }
        }.execute();
    }

    private RecyclerView getRecylcerView() {
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return recyclerView;
    }

    private void addwaiting() {
        emptyText = new TextView(context);
        emptyText.setText(context.getString(R.string.empty_message));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        emptyText.setLayoutParams(layoutParams);
        emptyText.setGravity(Gravity.CENTER);
        layoutParams.setMargins(8, 8, 8, 8);
        linearLayout.addView(emptyText);
    }

}
