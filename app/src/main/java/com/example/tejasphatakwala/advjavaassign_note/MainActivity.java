package com.example.tejasphatakwala.advjavaassign_note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    SearchView searchBar;
    ListView myListView;
    DBHealper db;

    String OrderBy = "";
    boolean orderDesc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myListView = (ListView) findViewById(R.id.listView);
        searchBar = (SearchView) findViewById(R.id.searchView);
        db = new DBHealper(this);

        Note[] pp = db.getAllNotes(OrderBy);
        System.out.println(pp.length + " retrived");
        if (pp.length > 0) {
            myListView.setAdapter(new CustomAdapter(this, pp));
        } else {
            Date dt = new Date();
            int hours = dt.getHours();
            int minutes = dt.getMinutes();
            int seconds = dt.getSeconds();
            String curTime = hours + ":" + minutes + ":" + seconds;

            pp = db.getAllNotes(OrderBy);
            myListView.setAdapter(new CustomAdapter(this, pp));

            myListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            myListView.setStackFromBottom(true);
        }


        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                DBHealper d = new DBHealper(getApplicationContext());

                Note[] ddddd = d.getNotesByCondition(newText,OrderBy);
                System.out.println(ddddd.length + " retrived");
                myListView.setAdapter(new CustomAdapter(getApplicationContext(), ddddd));

                myListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                myListView.setStackFromBottom(true);

                return false;
            }
        });

        Button add = (Button) findViewById(R.id.AddButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);

                startActivity(intent);
            }
        });

        Button orderCreated = (Button) findViewById(R.id.OrderBtnCreatedDate);

        orderCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderBy = " Order by DateCreated Desc";

                db = new DBHealper(getApplicationContext());

                Note[] pp = db.getAllNotes(OrderBy);
                System.out.println(pp.length + " retrived");
                if (pp.length > 0) {
                    myListView.setAdapter(new CustomAdapter(getApplicationContext(), pp));
                }
            }
        });

        final  Button titleOrder = (Button) findViewById(R.id.titleOrder);

        titleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderDesc == true) {
                    OrderBy = " Order by Title Asc";
                    titleOrder.setText("Title-Desc");
                    orderDesc = false;
                }
                else
                {
                    OrderBy = " Order by Title DESC";
                    orderDesc = true;
                    titleOrder.setText("Title-ASC");
                }

                db = new DBHealper(getApplicationContext());

                Note[] pp = db.getAllNotes(OrderBy);
                System.out.println(pp.length + " retrived");
                if (pp.length > 0) {
                    myListView.setAdapter(new CustomAdapter(getApplicationContext(), pp));
                }
            }
        });
    }

}
