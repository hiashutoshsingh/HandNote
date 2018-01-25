package com.bulletin.theinvincible.notemaking;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    DbHelper dbHelper;
    ListView listView;
ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listView);

dbHelper =new DbHelper(this, DbHelper.DB_NAME, null, DbHelper.DB_VERSION);

loadTaskList();


    }

    private void loadTaskList() {

        ArrayList<String> taskList =dbHelper.getTaskList();
        if (adapter==null)
        {
            adapter=new ArrayAdapter<String>(this,R.layout.abc_task_display,R.id.is_task_name,taskList);
            listView.setAdapter(adapter);

        }

        else {

            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {

            case R.id.action_add_task:
                final EditText editText =new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add Task")
                        .setMessage("What do you want to add ?")
                        .setView(editText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task=String.valueOf(editText.getText());
                                dbHelper.insertNewTask(task);
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
                return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void deleteTask(View view)
    {
        View parent =(View) view.getParent();
        TextView textView=(TextView)parent.findViewById(R.id.is_task_name);
        String task=String.valueOf(textView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();
    }
}
