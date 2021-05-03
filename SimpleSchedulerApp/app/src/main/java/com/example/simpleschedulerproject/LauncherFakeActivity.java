package com.example.simpleschedulerproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

import androidx.preference.PreferenceManager;

public class LauncherFakeActivity extends AppCompatActivity {
    private Button taskListBtn;
    private Button calendarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_fake);

        //Based on the user preference, decides which activity to redirect to as the "real" launcher
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        Boolean mainLauncher = sharedPreferences.getBoolean("MainScreen", false);
        if(!mainLauncher) {
            Intent i = new Intent(LauncherFakeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else{
            Intent i = new Intent(LauncherFakeActivity.this, CalendarScreen.class);
            startActivity(i);
            finish();
        }

        /*
        * These buttons are in the event that the program somehow relaunches this activity
        * and the user needs to get back to the main program
        * whether it be the task list or calendar screen
        * This should not happen but exists as a 'just in case' scenario
        */
        taskListBtn = findViewById(R.id.TaskListScreen);
        calendarBtn = findViewById(R.id.CalendarScreen);

        //launch the task list activity
        taskListBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(LauncherFakeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        
        //launch the calendar screen
        calendarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(LauncherFakeActivity.this, CalendarScreen.class);
                startActivity(i);
                finish();
            }
        });
    }
}