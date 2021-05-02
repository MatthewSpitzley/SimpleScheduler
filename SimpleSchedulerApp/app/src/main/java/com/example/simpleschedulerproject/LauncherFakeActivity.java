package com.example.simpleschedulerproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.prefs.Preferences;
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        Boolean mainLauncher = sharedPreferences.getBoolean("MainScreen", true);
        if(!mainLauncher) {
            Intent i = new Intent(LauncherFakeActivity.this, MainActivity.class);
            startActivity(i);
        }
        else{
            Intent i = new Intent(LauncherFakeActivity.this, CalendarScreen.class);
            startActivity(i);
        }
        taskListBtn = findViewById(R.id.TaskListScreen);
        calendarBtn = findViewById(R.id.CalendarScreen);

        taskListBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(LauncherFakeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        calendarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(LauncherFakeActivity.this, CalendarScreen.class);
                startActivity(i);
            }
        });
    }
}