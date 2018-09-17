package com.example.sakifaaktar.locationalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

public class AllTasks extends AppCompatActivity {

    //variables
    TextView task_list;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        lv = (ListView) findViewById(R.id.lview);
        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());


        String[] data = db.getAllTasks();

        lv.setAdapter(new ArrayAdapter(this,R.layout.list_view,R.id.showList,data));


        //adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),SingleTask.class);
                i.putExtra("MyKEY",position);
                startActivity(i);
            }
        });
    }

    @Override
    public  void onBackPressed(){
        Intent i = new Intent(AllTasks.this,MainActivity.class);

        startActivity(i);
    }

}
