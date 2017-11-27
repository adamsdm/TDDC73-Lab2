package com.example.adamsoderstrom.tddc73lab2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
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

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    EditText pathTextView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    View marked;
    Boolean manualInput = false;
    Context c = this;
    Boolean manualClose = true;

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
                String input = editable.toString();
                String[] parts = input.split("/");


                if(parts.length < 2)
                    return;

                String group = parts[1].toLowerCase();

                Boolean groupFound = false;
                for(int i=0; i<listDataHeader.size(); i++){
                    if(group.equals(listDataHeader.get(i).toLowerCase())){


                        if(!expListView.isGroupExpanded(i)) {
                            closeAllGroups();
                            expListView.expandGroup(i);
                        }

                        groupFound = true;
                        break;
                    } else if(listDataHeader.get(i).toLowerCase().contains(group)){
                        pathTextView.setBackgroundColor(Color.WHITE);
                        groupFound = true;
                    }
                }

                if(!groupFound){
                    pathTextView.setBackgroundColor(Color.RED);
                    deselectAll();
                    return;
                }

                if(parts.length<3)
                    return;

                boolean nodeFound = false;
                String groupString = "";
                for(int i=0; i<expListView.getChildCount(); i++){
                    LinearLayout node = (LinearLayout) expListView.getChildAt(i);
                    TextView text = (TextView) node.getChildAt(0);

                    if(text.getId() == R.id.lblListHeader){
                        groupString = text.getText().toString().toLowerCase();
                    } else {
                        String nodePath = "/"+groupString+"/"+text.getText().toString().toLowerCase();


                        if(input.equals(nodePath)){
                            deselectAll();
                            node.setBackgroundColor(Color.GREEN);
                            nodeFound = true;
                        } else if(nodePath.contains(input)){
                            nodeFound = true;
                        }

                    }
                }

                if(nodeFound){
                    pathTextView.setBackgroundColor(Color.WHITE);
                } else {
                    pathTextView.setBackgroundColor(Color.RED);
                    deselectAll();
                }

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

                deselectAll();
                v.setBackgroundColor(Color.GREEN);


                return false;
            }
        });

        ExpandableListView.OnGroupExpandListener groupExpandListener = new ExpandableListView.OnGroupExpandListener() {

            public void onGroupExpand(int i) {
                if(manualClose) {
                    Log.d(TAG, "onGroupExpand: DESELECTING");
                    deselectAll();
                }
            }

        };

        ExpandableListView.OnGroupCollapseListener groupCollapseListener = new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                if(manualClose) {
                    Log.d(TAG, "onGroupCollapse: DESELECTING");
                    deselectAll();
                }
            }

        };

        expListView.setOnGroupExpandListener(groupExpandListener);
        expListView.setOnGroupCollapseListener(groupCollapseListener);
    }

    private void closeAllGroups(){
        manualClose = false;
        for(int i=0; i<expListView.getChildCount();i++){
            expListView.collapseGroup(i);
        }
        manualClose = true;
    }

    private void deselectAll(){

        for(int i=0; i<expListView.getChildCount(); i++){
            LinearLayout child = (LinearLayout) expListView.getChildAt(i);

            child.setBackgroundColor(Color.TRANSPARENT);
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