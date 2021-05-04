package com.example.simpleschedulerproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.Context;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaskList extends AppCompatActivity {
    private static final String TAG = "TaskList";

    private ImageButton settingsBtn;
    private Button signInBtn, addTaskBtn;
    private DBHelper mHelper;
    private ListView mTaskList;
    private ArrayAdapter<String> mAdapter;
    private Settings settings;
    private TimePicker mTimePicker;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DBHelper(this);
        mTaskList = (ListView) findViewById(R.id.task_list);

        settingsBtn = findViewById(R.id.settingsButton);
        signInBtn = findViewById(R.id.sign_in_button);

        settingsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(TaskList.this, SettingsActivity.class);
                startActivity(i);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(TaskList.this, GoogleSignInActivity.class);
                startActivity(i);
            }
        });

        updateUI();
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
                    Intent i = new Intent(TaskList.this, History.class);
                    startActivity(i);
                    finish();
                }
                //if the start is greater than the end
                else{
                    //swipe right
                    Intent i = new Intent(TaskList.this, CalendarScreen.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
        return false;
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TASK_TABLE,
                new String[]{DBHelper.COLUMN_TASK_NAME},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(DBHelper.COLUMN_TASK_NAME);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_task,
                    R.id.task_title,
                    taskList);
            mTaskList.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        Boolean emailNoti = sharedPreferences.getBoolean("NotificationEmail", false);
        Boolean pushNoti = sharedPreferences.getBoolean("NotificationPush", false);
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                Context context = this;
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                taskEditText.setHint("Task");
                layout.addView(taskEditText);
                final EditText catEditText = new EditText(this);
                catEditText.setHint("Category");
                layout.addView(catEditText);
                final EditText timeEditText = new EditText(this);
                timeEditText.setHint("Due date/time");
                layout.addView(timeEditText);
                final EditText recurEditText = new EditText(this);
                recurEditText.setHint("Recurrence");
                final EditText emailET = new EditText(this);
                if(!emailNoti){
                    emailET.setText("No");
                }
                else{
                    emailET.setHint("Email Notifications?");
                    layout.addView(emailET);
                }
                final EditText pushET = new EditText(this);
                if(!pushNoti){
                    pushET.setText("No");
                }
                else{
                    pushET.setHint("Push Notifications?");
                    layout.addView(pushET);
                }
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());

                                String cat = String.valueOf(catEditText.getText());
                                Category category = new Category(cat);

                                String time = String.valueOf(timeEditText.getText());
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm:ss z");
                                ZonedDateTime dateTime = ZonedDateTime.parse(time, formatter);

                                String recur = String.valueOf(recurEditText.getText());
                                Recur recurEnum;
                                if(recur.equalsIgnoreCase("DAILY"))
                                    recurEnum = Recur.DAILY;
                                else if(recur.equalsIgnoreCase("WEEKDAYS"))
                                    recurEnum = Recur.WEEKDAYS;
                                else if(recur.equalsIgnoreCase("WEEKLY"))
                                    recurEnum = Recur.WEEKLY;
                                else if(recur.equalsIgnoreCase("MONTHLY"))
                                    recurEnum = Recur.WEEKLY;
                                else if(recur.equalsIgnoreCase("YEARLY"))
                                    recurEnum = Recur.YEARLY;
                                else
                                    recurEnum = Recur.DAILY;
                                String email = String.valueOf(emailET.getText());
                                ZonedDateTime dateTimeEmail = dateTime;

                                String push = String.valueOf(pushET.getText());
                                ZonedDateTime dateTimePush = dateTime;

                                TaskClass mTask = new TaskClass(task, category, dateTime, recurEnum, dateTimeEmail, dateTimePush, false);
                                mHelper.addTask(mTask);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(DBHelper.TASK_TABLE,
                DBHelper.COLUMN_TASK_NAME + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }
}