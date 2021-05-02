package com.example.simpleschedulerproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import android.content.SharedPreferences;
import java.util.prefs.Preferences;
import java.util.Date;
import java.time.temporal.TemporalAccessor;
import android.widget.ImageButton;
import android.view.MotionEvent;

public class CalendarScreen extends AppCompatActivity {

    private CalendarView calendar;
    private TextView dateView;
    private Button noDate;
    private ImageButton settingsBtn;
    private Button taskListBtn;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_screen);

        Settings settings = new Settings();
        calendar = (CalendarView)findViewById(R.id.calendar);
        dateView = (TextView)findViewById(R.id.date_view);
        noDate = (Button)findViewById(R.id.noDate);
        settingsBtn = findViewById(R.id.settings);
        //taskListBtn = findViewById(R.id.taskList);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        int dateDisplayFormat = 0;
        try{
            dateDisplayFormat = Integer.valueOf(sharedPreferences.getString("DateChoices", ""));
        } catch(NumberFormatException e){
            e.printStackTrace();
        }
        //int dateDisplayFormat = Integer.valueOf(sharedPreferences.getString("DateChoices", ""));
        DateTimeFormatter df = DateTimeFormatter.ofPattern(settings.dateDisplay(dateDisplayFormat));
        LocalDateTime ldt = LocalDateTime.now().plusDays(0);
        dateView.setText(df.format(ldt));

        noDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                //startActivity(i);
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CalendarScreen.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        /*taskListBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CalendarScreen.this, MainActivity.class);
                startActivity(i);
            }
        });*/

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(calendar.getContext() /* Activity context */);
                int dateDisplayFormat = Integer.valueOf(sharedPreferences.getString("DateChoices", ""));
                //DateTimeFormatter df = DateTimeFormatter.ofPattern(settings.dateDisplay(dateDisplayFormat));
                String dateString = dayOfMonth + "/" + (month+1) + "/" + year;
                DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat targetFormat = new SimpleDateFormat(settings.dateDisplay(dateDisplayFormat));
                Date date = null;
                try {
                    date = originalFormat.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = targetFormat.format(date);
                dateView.setText(formattedDate);
            }
        });

        mTextView = (TextView) findViewById(R.id.text);

    }
    float xBegin;
    float xEnd;
    public boolean onTouchEvent(MotionEvent touchEvent){

        switch(touchEvent.getAction()){
            //from the moment the finger is on the screen
            case MotionEvent.ACTION_DOWN:
                xBegin = touchEvent.getRawX();
                break;
            //to the moment the finger is lifted off
            case MotionEvent.ACTION_UP:
                xEnd = touchEvent.getRawX();
                //if the start is less than the end
                if(xBegin < xEnd){
                    //swipe left
                    Intent i = new Intent(CalendarScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                //if the start is greater than the end
                else{
                    //swipe right
                    Intent i = new Intent(CalendarScreen.this, History.class);
                    startActivity(i);
                    finish();
            }
            break;
        }
        return false;
    }
}