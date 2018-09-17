package com.example.sakifaaktar.locationalarm;
import android.Manifest;
import android.app.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.StringTokenizer;

import static android.app.Notification.DEFAULT_SOUND;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    //button variables set
    Button addTaskButton, allTasksButton;

    //Program Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button variables initialization
        addTaskButton = (Button) findViewById(R.id.addTaskButton);
        allTasksButton = (Button) findViewById(R.id.allTasksButton);

        //New Task Activity
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
            }
        });

        //All Tasks Activity
        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllTasks.class);
                startActivity(intent);
            }
        });

        //location alarm
        if (!runtime_permissions()) {
            enable_buttons();
        }

    }


    private void enable_buttons() {
        Intent i = new Intent(getApplicationContext(), GPS_Service.class);
        startService(i);
    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_permissions();
            }
        }
    }

    //broadcast reciever all active task list retrieve
    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    DatabaseHandler mf = new DatabaseHandler(getApplicationContext());

                    String[] data = mf.fetch_active_task_status();

                    for (int i = 0; i < data.length; i++) {
                        String CurrentString = data[i];
                        StringTokenizer tokens = new StringTokenizer(CurrentString, ",");

                        //get latitude from database value
                        String db_latitude_temp = tokens.nextToken();
                        StringTokenizer lat_temp = new StringTokenizer(db_latitude_temp, "(");
                        String db_latitude_prev = lat_temp.nextToken();
                        String db_latitude = lat_temp.nextToken();


                        //get longitude from database value
                        String db_longitude_temp = tokens.nextToken();
                        StringTokenizer long_temp = new StringTokenizer(db_longitude_temp, ")");
                        String db_longitude = long_temp.nextToken();

                        Location locationA = new Location("point A");

                        locationA.setLatitude(Double.valueOf(db_latitude));
                        locationA.setLongitude(Double.valueOf(db_longitude));

                        Location locationB = new Location("point B");
                        intent.getExtras().get("coordinate_longitude").getClass().getName();


                        Double Current_Latitude = (Double) intent.getExtras().get("coordinate_latitude");
                        Double Current_Longitude = (Double) intent.getExtras().get("coordinate_longitude");
                        locationB.setLatitude(Current_Latitude);
                        locationB.setLongitude(Current_Longitude);

                        float distance = locationA.distanceTo(locationB); //distance between two places in meters

                        if (distance <= 100) {
                            Toast.makeText(MainActivity.this, "\n You Reached", Toast.LENGTH_SHORT).show();
                            /*
                            try {
                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                r.play();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/

/*
                            //Define Notification Manager
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                            //Define sound URI
                            //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            // Uri path = Uri.parse(R.raw.notification_sound);
                            Uri path = Uri.parse("android.resource://com.example.sakifa.locationalarm/" + R.raw.notification_sound);

                            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle("You've Arrived")
                                    .setContentText("You get your destination")
                                    .setAutoCancel(true)
                                    .setSound(path); //This sets the sound to play

                            //Display notification
                            notificationManager.notify(0, mBuilder.build());
*/
                            NotificationCompat.Builder builder =
                                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                                            .setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle("Notifications: You has a task near this place!!!")
                                            .setContentText("This is a test notification");


                            Intent notificationIntent = new Intent(context, MainActivity.class);

                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            builder.setContentIntent(contentIntent);
                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {500,500,500,500,500,500,500,500,500};

                            builder.setVibrate(pattern);
                            builder.setStyle(new NotificationCompat.InboxStyle());
                           // Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Uri path = Uri.parse("android.resource://com.example.sakifaaktar.locationalarm/" + R.raw.notification_sound);
                            builder.setSound(path);
// Add as notification
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(1, builder.build());
                        }

                    }
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

}
