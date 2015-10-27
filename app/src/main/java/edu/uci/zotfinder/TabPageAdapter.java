package edu.uci.zotfinder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
public class TabPageAdapter extends FragmentStatePagerAdapter {
    public TabPageAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
        case 0:
            return new EmergencyInfoFragment();
        case 1:
            return new RolesExpectationsFragment();
      
        }
        return null;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2; //No of Tabs
    }
    }
