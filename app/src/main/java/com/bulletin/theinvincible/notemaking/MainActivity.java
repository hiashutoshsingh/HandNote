package com.bulletin.theinvincible.notemaking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton fabButton;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    String notesReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabButton = (FloatingActionButton) findViewById(R.id.fabButton);
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(MainActivity.this,EditNotes.class);
                intent.putExtra(Strings.INTENT_NOTES_RECEIVED,arrayList.get(i).toString());


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Strings.INTENT_REQUEST_CODE) {
            notesReceived = data.getStringExtra(Strings.INTENT_NOTES_STRING);
            arrayList.add(notesReceived);
            arrayAdapter.notifyDataSetChanged();
        }

    }

    public void openAddNotes(View view) {

        Intent intent = new Intent(this, AddNotesActivity.class);
        startActivityForResult(intent, Strings.INTENT_REQUEST_CODE);

    }
}
