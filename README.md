#  Location Alarm.

## App Specification 
Location based alarm using GPS is an attempt to add an alarm facility for mobiles, based on the location of the device. The location based alarm will give alert when one reaches on the desired destination. It is a GPS based alarm, if you set an alarm, it will make a sound and notification once it's detected you are within the user defined range from the destination. The user needs to save the current location using google map; the alarm will ring when the user is near to the location. This application consists of various systems such as set alarm, set time and date, set location. This system allows user to search the location, save the searching location, add task  details and generate alarm on arrival of that location. With the help of Google maps we can directly search the location on maps and save it as destination location.

## Platforms 
Android ( version 7.1.1) , minimum SDK 15 & highest SDK 25.


## Features and Funtionality :
1. Program Initialization (MainActivity.java)
a. Main Activity onCreate method
b. Initialize 2 buttons
            i. addTaskButton = (Button) findViewById(R.id.addTaskButton);
             
             ii. allTasksButton = (Button) findViewById(R.id.allTasksButton);

2. Add tasks
a. Add task button On Click Event  (MainActivity.java)
addTaskButton.setOnClickListener(new View.OnClickListener() {

});
b. Add Task Activity Start/Initialization (AddTask.java)
c. PlacePicker 
i. Placepicker map view 
```java
//Placepicker Map View Initialzation
mapViewButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startPlacePickerActivity();
    }
});
```

ii. Placepicker data insert
/*
    	placepicker Data Input
*/
startPlacePickerActivity() method pick placepicker data and changes entercurrentlocation edit text view.



3. Save data in database( DatabaseHandler.java)
     i. creating sqlite table name “db” using public void onCreate(SQLiteDatabase        db).
     ii. To show all task, call getAllTasks().
     
    iii.In activity_all_task.xml ,there show single task details & to fetch this details String fetch_task(int id) is called.

iv. To check the task status( on/off) String fetch_task_status(int id) is used.
In SingleTask.java this fetch_task_status is used by toggleButton.
           if(Integer.valueOf(db.fetch_task_status((rec_position+1)))==0){
                 statusToggle.setChecked(false);
            }else{
                 statusToggle.setChecked(true);
                 }
   v.After checking status,if we want to change this status, then it will update data by calling int update_task(int id, String status).

4. when database is updated AppRefresh.java is used to refresh this.

5. BroadcastReciver ( Mainactivity.java) 
       i. To find which tasks are active , we use
       ```java
          DatabaseHandler mf = new DatabaseHandler(getApplicationContext());
                    String[] data = mf.fetch_active_task_status();
                    
       ```
ii.Get latitude from database value:
```java
        String db_latitude_temp = tokens.nextToken();
        StringTokenizer lat_temp = new StringTokenizer(db_latitude_temp, "(");
        String db_latitude_prev = lat_temp.nextToken();
        String db_latitude = lat_temp.nextToken();

iii. Get longitude from database value:
```java
       String db_longitude_temp = tokens.nextToken();
       StringTokenizer long_temp = new StringTokenizer(db_longitude_temp, ")");
       String db_longitude = long_temp.nextToken();
```


iv.To get current latitude & longitude (GPS_Service.java)
```java
     public void onLocationChanged(Location location) {
    Intent i = new Intent("location_update");
    i.putExtra("coordinate_latitude",location.getLatitude());
    i.putExtra("coordinate_longitude",location.getLongitude());
    sendBroadcast(i);
   }
```

v. In Mainactivity.java we use this current latitude and longitude 
```java
Double Current_Latitude = (Double) intent.getExtras().get("coordinate_latitude");
Double Current_Longitude = (Double) intent.getExtras().get("coordinate_longitude");
locationB.setLatitude(Current_Latitude);
locationB.setLongitude(Current_Longitude);
```
vi.save database location in locationA and gps location in location
```java
Location locationA = new Location("point A");
Location locationB = new Location("point B");
```
vii.now compare both distance(around 100 meters)
```java
 float distance = locationA.distanceTo(locationB);

if (distance <= 100) {
    Toast.makeText(MainActivity.this, "\n You Reached", Toast.LENGTH_SHORT).show();
```


### 6.To get notification:

```java
NotificationCompat.Builder builder =
        (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Notifications: You has a task near this place!!!")
                .setContentText("This is a test notification");
                


Intent notificationIntent = new Intent(context, MainActivity.class);
```

### 7. Notification sound:


```java
Uri path = Uri.parse("android.resource://com.example.sakifaaktar.locationalarm/" + R.raw.notification_sound);
builder.setSound(path);
```


## API lists:
Google map api
Datepicker
Timepicker.

Services:
Broadcast receiver
GPS service
Notification manager.
