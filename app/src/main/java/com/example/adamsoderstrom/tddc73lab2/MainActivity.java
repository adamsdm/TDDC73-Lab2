package com.example.adamsoderstrom.tddc73lab2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    EditText pathTextView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    View marked;
    Boolean manualInput = false;

    int currGroup = -1;
    int currChild = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // get the path text
        pathTextView = (EditText) findViewById(R.id.pathField);



        // Event listeners
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        pathTextView.addTextChangedListener(textWatcher);

        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                pathTextView.removeTextChangedListener(textWatcher);

                String path = '/' + listDataHeader.get(groupPosition) + '/' + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                pathTextView.setText(path.toLowerCase());
                pathTextView.addTextChangedListener(textWatcher);

                currGroup = groupPosition;
                currChild = childPosition;

                Log.d(TAG, "group: "+currGroup+ ", child: " + currChild);

                return false;
            }
        });


        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                fixMarking();
            }

        });
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                fixMarking();
            }
        });
    }

    private void fixMarking(){
        int groupNr = -1;
        int childNr = -1;



        Log.d(TAG, "No Nodes: " + expListView.getChildCount());
        for (int i = 0; i < expListView.getChildCount(); i++) {


            LinearLayout child = (LinearLayout) expListView.getChildAt(i);
            TextView text = (TextView) child.getChildAt(0);
            child.setBackgroundColor(Color.TRANSPARENT);

            if(text.getId() == R.id.lblListHeader){
                groupNr++;
                childNr = -1;
            } else {
                childNr++;
            }

            //Log.d(TAG, "GroupNr: " + groupNr + ", ChildNr: " + childNr);


        }
    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Light");
        listDataHeader.add("Medium");
        listDataHeader.add("Dark");

        // Adding child data
        List<String> h1 = new ArrayList<String>();
        h1.add("Green");
        h1.add("Green");
        h1.add("Green");
        h1.add("Blue");


        List<String> h2 = new ArrayList<String>();
        h2.add("Green");
        h2.add("Yellow");
        h2.add("Red");
        h2.add("Blue");


        List<String> h3= new ArrayList<String>();
        h3.add("Green");
        h3.add("Yellow");
        h3.add("Red");
        h3.add("Blue");

        listDataChild.put(listDataHeader.get(0), h1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), h2);
        listDataChild.put(listDataHeader.get(2), h3);
    }
}