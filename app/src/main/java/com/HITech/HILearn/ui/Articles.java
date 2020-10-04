package com.HITech.HILearn.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.HITech.HILearn.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Articles extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();
                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getString(R.string.sweeth1));
        listDataHeader.add(getString(R.string.sweeth2));
        listDataHeader.add(getString(R.string.sweeth3));
        listDataHeader.add(getString(R.string.sweeth4));
        listDataHeader.add(getString(R.string.sweeth5));
        listDataHeader.add(getString(R.string.sweeth6));
        listDataHeader.add(getString(R.string.sweeth7));
        listDataHeader.add(getString(R.string.sweeth8));
        listDataHeader.add(getString(R.string.sweeth9));

        // Adding child data
        List<String> introduction = new ArrayList<String>();
        introduction.add(getString(R.string.sweetb1));
        List<String> towhom = new ArrayList<String>();
        towhom.add(getString(R.string.sweetb2));
        List<String> components = new ArrayList<String>();
        components.add(getString(R.string.sweetb3));
        List<String> yourself = new ArrayList<String>();
        yourself.add(getString(R.string.sweetb4));
        List<String> Checks_For_Babies = new ArrayList<String>();
        Checks_For_Babies.add(getString(R.string.sweetb5));

        List<String> importance = new ArrayList<String>();
        importance.add(getString(R.string.sweetb6));

        List<String> exercise = new ArrayList<String>();
        exercise.add(getString(R.string.sweetb7));

        List<String> blood_spot = new ArrayList<String>();
        blood_spot.add(getString(R.string.sweetb8));
        List<String> ethiopia = new ArrayList<String>();
        ethiopia.add(getString(R.string.sweetb9));


        listDataChild.put(listDataHeader.get(0), introduction); // Header, Child data
        listDataChild.put(listDataHeader.get(1), towhom);
        listDataChild.put(listDataHeader.get(2), components);
        listDataChild.put(listDataHeader.get(3), yourself);
        listDataChild.put(listDataHeader.get(4), Checks_For_Babies);
        listDataChild.put(listDataHeader.get(5), importance);
        listDataChild.put(listDataHeader.get(6), exercise);
        listDataChild.put(listDataHeader.get(7), blood_spot);
        listDataChild.put(listDataHeader.get(8), ethiopia);


    }
}