package com.example.sakifaaktar.locationalarm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_TASKS = "tasks";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_LATLANG = "latlang";
    private static final String KEY_TASK_TEXT = "task_text";
    private static final String KEY_TIME_HOUR = "time_hour";
    private static final String KEY_TIME_MINUTE = "time_minute";
    private static final String KEY_DATE_YEAR = "date_year";
    private static final String KEY_DATE_MONTH = "date_month";
    private static final String KEY_DATE_DAY = "date_day";
    private static final String KEY_STATUS = "status";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE "+TABLE_TASKS+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_LOCATION+" TEXT ,"+KEY_LATLANG+ " Text ,"+KEY_TASK_TEXT+" TEXT, "+KEY_TIME_HOUR+" TEXT, "+KEY_TIME_MINUTE+" TEXT ,"+KEY_DATE_DAY+" TEXT , "+ KEY_DATE_MONTH+" TEXT ,"+KEY_DATE_YEAR+" TEXT ," +KEY_STATUS+" TEXT )";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }

    // Adding new Task
    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION, task.getLocation()); // Task location
        values.put(KEY_LATLANG, task.getLatlang()); // Task latitude longitude
        values.put(KEY_TASK_TEXT, task.getTaskText()); // Task text
        values.put(KEY_TIME_HOUR, task.getTimeHour()); // alarm time hour
        values.put(KEY_TIME_MINUTE, task.getTimeMinute()); // alarm time minute
        values.put(KEY_DATE_DAY, task.getDateDay()); // alarm date day
        values.put(KEY_DATE_MONTH, task.getDateMonth()); // alarm date month
        values.put(KEY_DATE_YEAR, task.getDateYear()); // alarm date year
        values.put(KEY_STATUS, 1); // task status

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Tasks

    String[] getAllTasks() {

        SQLiteDatabase sq = this.getReadableDatabase();

        String q = "SELECT * FROM "+TABLE_TASKS;

        Cursor c = sq.rawQuery(q, null);

        String[] showData = new String[c.getCount()];

        c.moveToFirst();

        if(c.moveToFirst()){
            int counter = 0 ;
            do {
                showData[counter] = "Location: "+c.getString(c.getColumnIndex(KEY_LOCATION+"")) +
                        "\nTask: "+ c.getString(c.getColumnIndex(KEY_TASK_TEXT+""))+
                        "\nStatus: "+c.getString(c.getColumnIndex(KEY_STATUS+""));

                counter = counter+1;

            } while(c.moveToNext());

        }

        return showData;
    }

    //single data retrieve
    String fetch_task(int id) {

        SQLiteDatabase sq = this.getReadableDatabase();

        String q = "SELECT * FROM "+TABLE_TASKS+" WHERE "+KEY_ID+" = "+id;

        Cursor c = sq.rawQuery(q, null);
        String s = "";

        c.moveToFirst();

        if(c.moveToFirst()) {
            s ="\nTask: "+ c.getString(c.getColumnIndex(KEY_TASK_TEXT+""))+
                    "\nStatus: "+c.getString(c.getColumnIndex(KEY_STATUS+""))+
                    "\nLatitude/Longitude: "+c.getString(c.getColumnIndex(KEY_LATLANG+""))+
                    "\nDate: "+c.getString(c.getColumnIndex(KEY_DATE_DAY+""))+"/"+c.getString(c.getColumnIndex(KEY_DATE_MONTH+""))+"/"+c.getString(c.getColumnIndex(KEY_DATE_YEAR+""));
        }

        return s;

    }
    //fetch status
    String fetch_task_status(int id) {

        SQLiteDatabase sq = this.getReadableDatabase();

        String qu = "SELECT "+KEY_STATUS+" FROM "+TABLE_TASKS+" WHERE "+KEY_ID+" = "+id;

        Cursor cu = sq.rawQuery(qu, null);
        String status = "";

        cu.moveToFirst();

        if(cu.moveToFirst()) {
            status =cu.getString(cu.getColumnIndex(KEY_STATUS+""));

        }

        return status;

    }

    //update data
    int update_task(int id, String status) {

        SQLiteDatabase sq = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, status);

        return sq.update(TABLE_TASKS, cv, KEY_ID+" = ? ", new String[]{id+""});

    }
    //fetch active status
    String[] fetch_active_task_status() {

        SQLiteDatabase sq = this.getReadableDatabase();

        String q = "SELECT * FROM "+TABLE_TASKS+" WHERE "+KEY_STATUS+" = "+1;

        Cursor c = sq.rawQuery(q, null);
/*
        // previous code
        SQLiteDatabase sq = this.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        String q = "SELECT * FROM "+TABLE_TASKS+" WHERE "+KEY_STATUS+" = "+1 +" AND "+KEY_DATE_DAY +" = '"+day+"' AND "+ KEY_DATE_MONTH+ " = '"+month+"' AND "+ KEY_DATE_YEAR+ " = '"+year+"'";

        Cursor c = sq.rawQuery(q, null);
*/

        String[] recieved_data = new String[c.getCount()];

        c.moveToFirst();

        if(c.moveToFirst()){
            int counter = 0 ;
            do {
                recieved_data[counter] = c.getString(c.getColumnIndex(KEY_LATLANG+""));
                counter = counter+1;

            } while(c.moveToNext());

        }

        return recieved_data;
    }



}

