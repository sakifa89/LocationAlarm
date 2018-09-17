package com.example.sakifaaktar.locationalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SingleTask extends AppCompatActivity {

    TextView tv;
    ToggleButton statusToggle;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);
        tv = (TextView) findViewById(R.id.showSingleTask);
        statusToggle = (ToggleButton) findViewById(R.id.statusToggle);


        final int rec_position = getIntent().getIntExtra("MyKEY", 999);
        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        String text = db.fetch_task(rec_position + 1);
        tv.setText(text);
        if(Integer.valueOf(db.fetch_task_status((rec_position+1)))==0){
            statusToggle.setChecked(false);
        }else{
            statusToggle.setChecked(true);
        }

        statusToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (statusToggle.isChecked()) {
                    status = String.valueOf(1);
                    db.update_task((rec_position + 1), status);
                    Toast.makeText(SingleTask.this, "Alarm On", Toast.LENGTH_SHORT).show();
                    AppRefresh.refreshApp(SingleTask.this);
                } else {
                    status = String.valueOf(0);
                    db.update_task((rec_position + 1), status);
                    Toast.makeText(SingleTask.this, "Alarm Off", Toast.LENGTH_SHORT).show();
                    AppRefresh.refreshApp(SingleTask.this);
                }
            }
        });
    }

    //refresh previous activity on back button press
    @Override
    public  void onBackPressed(){
        Intent i = new Intent(SingleTask.this,AllTasks.class);

        startActivity(i);
    }


}