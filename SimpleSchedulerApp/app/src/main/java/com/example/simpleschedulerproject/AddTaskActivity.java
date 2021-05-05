package com.example.simpleschedulerproject;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;


public class AddTaskActivity extends AppCompatActivity {
    private ListView taskListAdd;
    private TextView taskName;
    private Spinner taskCategory;
    private TimePicker taskTime;
    private DatePicker taskDate;
    private Spinner taskRecur;
    private Switch taskEmailNotification;
    private Switch taskPushNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskListAdd = findViewById(R.id.task_list);
        taskName = findViewById(R.id.task_name);
        taskCategory = findViewById(R.id.task_category);
        taskTime = findViewById(R.id.task_time);
        taskDate = findViewById(R.id.task_date);
        taskRecur = findViewById(R.id.task_recur);
        taskEmailNotification = findViewById(R.id.task_email_notification);
        taskPushNotification = findViewById(R.id.task_push_notification);
    }
}