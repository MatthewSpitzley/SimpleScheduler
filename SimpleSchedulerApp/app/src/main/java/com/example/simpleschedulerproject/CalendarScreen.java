package com.example.simpleschedulerproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Date;
import android.view.MotionEvent;

public class CalendarScreen extends AppCompatActivity {

    private CalendarView calendar;
    private TextView dateView;
    private Button noDate;
    private ImageButton settingsBtn;
    private Button signInBtn;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDayOfMonth;
    private Settings settings = new Settings();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_screen);

        calendar = (CalendarView)findViewById(R.id.calendar);
        dateView = (TextView)findViewById(R.id.date_view);
        noDate = (Button)findViewById(R.id.noDate);
        settingsBtn = findViewById(R.id.settings);
        signInBtn = findViewById(R.id.sign_in_button);

        //get the shared preferences and change the text for the displayed date according to user preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        int dateDisplayFormat = 0;
        try{
            dateDisplayFormat = Integer.valueOf(sharedPreferences.getString("DateChoices", ""));
        } catch(NumberFormatException e){
            e.printStackTrace();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(settings.dateDisplay(dateDisplayFormat));
        LocalDateTime ldt = LocalDateTime.now().plusDays(0);
        selectedYear = ldt.getYear();
        selectedMonth = ldt.getMonthValue();
        selectedDayOfMonth = ldt.getDayOfMonth();
        dateView.setText(df.format(ldt));

        //Similar to onSelectedDayChange, except for tasks that have no date
        noDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dateView.setText("No Date");
            }
        });

        //When the user selects the settings button this will open the root_preferences.xml
        settingsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CalendarScreen.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        //When the user selects the sign in button this will open the GoogleSignIn.xml
        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CalendarScreen.this, GoogleSignInActivity.class);
                startActivity(i);
            }
        });

        //When the user selects a new date on the calendar it will update the UI accordingly
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
                //update the selected day for other methods to use
                selectedYear = year;
                selectedMonth = month+1;
                selectedDayOfMonth = dayOfMonth;

                //Based on root_preferences change the output of the date at the top
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(calendar.getContext() /* Activity context */);
                int dateDisplayFormat = Integer.valueOf(sharedPreferences.getString("DateChoices", ""));
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
    }

    /*
     * When the activity is resumed:
     *      update any new tasks
     *      or any change in formatting from the root preferences
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(calendar.getContext() /* Activity context */);
        int dateDisplayFormat = Integer.valueOf(sharedPreferences.getString("DateChoices", ""));
        String dateString = selectedDayOfMonth + "/" + (selectedMonth) + "/" + selectedYear;
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat targetFormat = new SimpleDateFormat(settings.dateDisplay(dateDisplayFormat));
        Date date = null;
        try {
            date = originalFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        if(dateView.getText()!="No Date"){
            dateView.setText(formattedDate);
        }
    }

    //Change the current activity based on the beginning and ending location of the finger
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