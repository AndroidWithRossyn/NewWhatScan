package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdSize;
import com.google.android.material.tabs.TabLayout;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;

import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.App.adCount;


public class HomeFragment extends Fragment {
    private Context context;
    private Fragment_files fragment_files;
    private ChatFragment users_fragment;
    private String pack;
    private ViewPager pager;
    private View view;
    FrameLayout adView;

    public HomeFragment newInstance(String str) {
        HomeFragment wA_fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pack", str);
        wA_fragment.setArguments(bundle);
        return wA_fragment;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        view = layoutInflater.inflate(R.layout.wa_fragment, viewGroup, false);

        return view;
    }
    private void LoadInterstitialAds() {



    }

    public void ShowInterstitialAds(){



    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        pack = getArguments().getString("pack");
        Log.e("packkkkk", pack);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pager = (ViewPager) view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tab);

        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Messages");
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Media files");
        tabLayout.addTab(secondTab);


//
//        TabLayout.Tab secondTab2 = tabLayout.newTab();
//        secondTab2.setText("Quick Reply");
//        tabLayout.addTab(secondTab2);
//
//        TabLayout.Tab secondTab4 = tabLayout.newTab();
//        secondTab4.setText("Text Repeat");
//        tabLayout.addTab(secondTab4);
//
//        TabLayout.Tab secondTab5 = tabLayout.newTab();
//        secondTab5.setText("Blank Message");
//        tabLayout.addTab(secondTab5);

        pager.setAdapter((PagerAdapter) new FragAdapter(getChildFragmentManager()));
        try {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    try {
                        if (adCount % 8 == 0) {
                            ShowInterstitialAds();
                        }
                        adCount++;
                        sendtoCleansaverAdapter();
                        sendToCleanHome();
                        pager.setCurrentItem(tab.getPosition());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendToCleanHome() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("update").putExtra("update", 2));
    }

    private void sendtoCleansaverAdapter() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("cleansaver"));
            }
        }, 500);
    }

    private class FragAdapter extends FragmentPagerAdapter {

        public int getCount() {
            return 2;
        }

        public FragAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int i) {
            if (i == 0) {
                users_fragment = new ChatFragment().newInstance(pack);
                return users_fragment;
            } else if (i == 1) {
                fragment_files = new Fragment_files().newInstance(pack);
                return fragment_files;
            }else {
                fragment_files = new Fragment_files().newInstance(pack);
                return fragment_files;
            }
//            else if (i == 4) {
//                QuickReplayFragment quickReplayActivity = new QuickReplayFragment();
//                return quickReplayActivity;
//            }  else if (i == 5) {
//                TextRepeaterFragment textRepeaterFragment = new TextRepeaterFragment();
//                return textRepeaterFragment;
//            }
//            else {
//                BlankMessageActivity blankMessageActivity = new BlankMessageActivity();
//                return blankMessageActivity;
//
//            }
        }

    }
}
