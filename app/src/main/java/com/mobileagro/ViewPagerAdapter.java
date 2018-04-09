package com.mobileagro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.user.mana_livechatv2.R;
/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    private int tabIcons[] = {R.drawable.sentra_a, R.drawable.lahan_a, R.drawable.varietas_a};
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            Tab1 tab1 = new Tab1();
            return tab1;
        }
        else if (position == 1) {
            Tab2 tab2 = new Tab2();
            return tab2;
        }
        else
        {
            Tab3 tab3 = new Tab3();
            return tab3;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public int getDrawableId(int position) {
        return tabIcons[position];
    }

}