package com.SachinApps.Whatscan.Pro.WhatsClone;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class WhstWebPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList();
    ArrayList<String> tab_titles = new ArrayList();

    public void add_fragments(Fragment fragment, String str) {
        this.fragments.add(fragment);
        this.tab_titles.add(str);
    }

    public WhstWebPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int i) {
        return (Fragment) this.fragments.get(i);
    }

    public int getCount() {
        return this.fragments.size();
    }

    public CharSequence getPageTitle(int i) {
        return (CharSequence) this.tab_titles.get(i);
    }
}