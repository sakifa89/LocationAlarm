package com.example.sakifaaktar.locationalarm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class AddTask extends AppCompatActivity {

    //variables
    TextView showLocation,task_text;
    EditText time, date,enterCurrentLocation,latlang;
    ImageView mapViewButton,location,timepicker;
    Button submit;

    //calander variables
    int c_day,c_month,c_year;
    static final int DIALOG_ID_DATE = 0;

    //time picker variables
    static final int DIALOG_ID_TIME = 1;
    int t_minute,t_hour;


    //placepicker variables
    private final int REQUEST_CODE_PLACEPICKER = 1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        //initialization variables values
        //location
        showLocation = (TextView) findViewById(R.id.showLocation);
        mapViewButton = (ImageView) findViewById(R.id.mapViewButton);
        enterCurrentLocation = (EditText) findViewById(R.id.showLocation);
        //Task text
        task_text = (TextView) findViewById(R.id.getTask);

        //date picker
        final Calendar cal = Calendar.getInstance();
        c_year = cal.get(Calendar.YEAR);
        c_month = cal.get(Calendar.MONTH);
        c_day = cal.get(Calendar.DAY_OF_MONTH);

        //submit variable
        submit = (Button) findViewById(R.id.submit);


        //Placepicker Map View Initialzation
        mapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlacePickerActivity();
            }
        });

        //date picker
        showDialogOnBtnClick();

        //time picker
        showTimePickerDialog();

        //submit data
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String TaskName = task_text.getText().toString();
                    String LocationAddress  = enterCurrentLocation.getText().toString();
                    String Latlang  = latlang.getText().toString();
                    String Time_Hour  = String.valueOf(t_hour);
                    String Time_Minute  = String.valueOf(t_minute);
                    String Date_Date  = String.valueOf(c_day);
                    String Date_Month  = String.valueOf(c_month);
                    String Date_Year  = String.valueOf(c_year);

                    //Task newLocation = new Task(LocationAddress);
                    Task newTask = new Task(LocationAddress,Latlang, TaskName,Time_Hour,Time_Minute,Date_Date,Date_Month,Date_Year);

                    db.addTask(newTask);

                    Toast.makeText(AddTask.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    //TODO: handle exception
                    e.printStackTrace();
                    Toast.makeText(AddTask.this, "Something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    /*
        Show time picker dialog methos start
    */
    public void showTimePickerDialog(){
        timepicker = (ImageView) findViewById(R.id.clock);
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID_TIME);
            }
        });
    }

    protected TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    t_hour = hourOfDay;
                    t_minute = minute;

                    String new_hour = String.valueOf(t_hour);
                    String new_minute = String.valueOf(t_minute);
                    String am_or_pm = "AM";

                    if(t_hour >12){
                        new_hour = String.valueOf(t_hour-12);
                        am_or_pm = "PM";
                    }
                    if(t_minute<10){
                        new_minute = "0"+ String.valueOf(t_minute);
                    }
                    time = (EditText) findViewById(R.id.getTime);
                    time.setText(new_hour +" : "+new_minute+ " " +am_or_pm);
                }
            };
    /*
        Show time picker dialog methos end
    */

    /*
        Show date picker dialog methos start
    */
    public void showDialogOnBtnClick(){
        //date picker
        location = (ImageView) findViewById(R.id.calender);
        date =(EditText) findViewById(R.id.getDate);

        //pick a date
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID_DATE);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID_DATE){
            return new DatePickerDialog(this,datePickerListener, c_year,c_month,c_day);
        }
        if(id ==DIALOG_ID_TIME){
            return new TimePickerDialog(AddTask.this, timePickerListener, t_hour,t_minute,false);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    c_year = year;
                    c_month = monthOfYear+1;
                    c_day = dayOfMonth;
                    date.setText(c_day+"/"+c_month+"/"+c_year);
                }
            };
    /*
        Show date picker dialog methos end
    */


    /*
        placepicker Data Input
    */
    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        // this would only work if you have your Google Places API working

        try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        }
    }

    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = PlacePicker.getPlace(data, this);

        String name = placeSelected.getLatLng().toString();
        String address = placeSelected.getAddress().toString();

        enterCurrentLocation = (EditText) findViewById(R.id.showLocation);
        latlang = (EditText) findViewById(R.id.latlang);

        enterCurrentLocation.setText(address);
        latlang.setText(name);
        //enterCurrentLocation.setText(name + ", " + address);
    }
    /*
        placepicker code end
    */



    //clear all values
    public void clearText(){

        task_text.setText("");
    }
}
