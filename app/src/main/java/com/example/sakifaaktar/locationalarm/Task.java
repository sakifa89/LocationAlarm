package com.example.sakifaaktar.locationalarm;




public class Task {
    //private variables
    private  int id;
    private String location;
    private String latlang;
    private String task_text;
    private String time_hour;
    private String time_minute;
    private String date_day;
    private String date_month;
    private String date_year;
    private String status;


    // constructor
    /*
    public Task(String location,String task_text, String time, String date,String status){
        this.location = location;
        this.task_text = task_text;
        this.time = time;
        this.date = date;
        this.status= status;
    }
*/
    // constructor
    public Task(String location,String latlang,String task_text,String time_hour,String time_minute,String date_day,String date_month,String date_year){
        this.location = location;
        this.latlang = latlang;
        this.task_text = task_text;
        this.time_hour = time_hour;
        this.time_minute = time_minute;
        this.date_day = date_day;
        this.date_month = date_month;
        this.date_year = date_year;
    }
    // getting ID
    public int getID(){
        return id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting Location
    public String getLocation(){
        return location;
    }

    // setting Location
    public void setLocation(String location){
        this.location = location;
    }
    // getting latitude longitude
    public String getLatlang(){
        return latlang;
    }

    // setting latitude longitude
    public void setLatlang(String location){
        this.latlang = latlang;
    }

    // getting task name
    public String getTaskText(){
        return task_text;
    }

    // setting task name
    public void setTaskText(String task_text){
        this.task_text = task_text;
    }

    // getting time hour
    public String getTimeHour(){
        return time_hour;
    }

    // setting time
    public void setTimeHour(String time_hour){
        this.time_hour = time_hour;
    }
    // getting time minute
    public String getTimeMinute(){
        return time_minute;
    }

    // setting minute
    public void setTimeMinute(String time_minute){
        this.time_minute = time_minute;
    }


    // getting Date day
    public String getDateDay(){
        return date_day;
    }

    // setting Date day
    public void setDateDay(String date_day){
        this.date_day = date_day;
    }
    // getting Date month
    public String getDateMonth(){
        return date_month;
    }

    // setting Date month
    public void setDateMonth(String date_month){
        this.date_month = date_month;
    }
    // getting Date year
    public String getDateYear(){
        return date_year;
    }

    // setting Date year
    public void setDateYear(String date_year){
        this.date_year = date_year;
    }

    // getting Status
    public String getStatus(){
        return status;
    }

    // setting Date
    public void setStatus(String status){
        this.status = status;
    }
}
