package com.example.simpleschedulerproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;

public class CalendarScreen extends AppCompatActivity {

    CalendarView calendar;
    TextView dateView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_screen);

        calendar = (CalendarView)findViewById(R.id.calendar);
        dateView = (TextView)findViewById(R.id.date_view);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
                String date = dayOfMonth + "-" + (month+1) + "-" + year;
                dateView.setText(date);
            }
        });

        mTextView = (TextView) findViewById(R.id.text);

    }
}