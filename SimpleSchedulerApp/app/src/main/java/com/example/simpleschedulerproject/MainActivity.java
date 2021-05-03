package com.example.simpleschedulerproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

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
        addTaskBtn = findViewById(R.id.addTaskBtn);


        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddTask.class);
                startActivity(i);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, GoogleSignInActivity.class);
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
                    Intent i = new Intent(MainActivity.this, History.class);
                    startActivity(i);
                    finish();
                }
                //if the start is greater than the end
                else{
                    //swipe right
                    Intent i = new Intent(MainActivity.this, CalendarScreen.class);
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
        Cursor cursor = db.query(TaskContract.TaskEntry.TASK_TABLE,
                new String[]{TaskContract.TaskEntry.COLUMN_TASK_NAME},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);
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
/**
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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

                EditText timeEditText = findViewById(R.id.timeEditText);
                layout.addView(timeEditText);

                final EditText recurEditText = new EditText(this);
                recurEditText.setHint("Recurring Notifications");
                layout.addView(recurEditText);
                timeEditText = (EditText) findViewById(R.id.timeEditText);
                perform click event listener on edit text

                spinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.recur_choices, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);




                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                String cat = String.valueOf(catEditText.getText());
                                String recur = String.valueOf(recurEditText.getText());


                                timeEditText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Calendar mCurrentTime = Calendar.getInstance();
                                        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                                        int minute = mCurrentTime.get(Calendar.MINUTE);
                                        TimePickerDialog mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                                timeEditText.setText(selectedHour + ":" + selectedMinute);
                                            }
                                        }, hour, minute, false);//Yes 24 hour time
                                        mTimePicker.setTitle("Select Time");
                                        mTimePicker.show();

                                    }
                                });



                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, task);
                                db.insertWithOnConflict(TaskContract.TaskEntry.TASK_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                values.put(TaskContract.TaskEntry.COLUMN_TASK_CATEGORY, cat);
                                // db.insertWithOnConflict(TaskContract.TaskEntry.TASK_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                // values.put(TaskContract.TaskEntry.COLUMN_TASK_TIME, String.valueOf(timeEditText));
                                db.insertWithOnConflict(TaskContract.TaskEntry.TASK_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();

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
    */
    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TASK_TABLE,
                TaskContract.TaskEntry.COLUMN_TASK_NAME + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }
}