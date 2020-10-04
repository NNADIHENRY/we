package com.HITech.HILearn.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * Created by prime on 11/12/18.
 */
//this class is what helps the fragments adapt tothe main activity the way the do.
public class TabsAccessorAdapter extends FragmentPagerAdapter
{

    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){

            case 0:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

//            case 1:
//                ContactsFragment contactsFragment = new ContactsFragment();
//                return  contactsFragment;


            default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return 1;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {


            case 0:
                return "Groups";

//            case 1:
//                return "Contacts";

            default:
                return null;
        }
    }
}
