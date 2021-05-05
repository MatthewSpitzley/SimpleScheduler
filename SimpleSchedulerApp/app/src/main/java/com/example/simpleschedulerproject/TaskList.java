package com.example.simpleschedulerproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.Context;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskList extends AppCompatActivity {
    private static final String TAG = "TaskList";

    private ImageButton settingsBtn;
    private Button signInBtn, addTaskBtn;
    private DBHelper mHelper;
    private ListView mTaskList;
    private ArrayAdapter<String> mAdapter;
    private Settings settings;
    private TimePickerDialog tPicker;
    private DatePickerDialog dPicker;
    private TextView tView;
    private Button addCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DBHelper(this);
        mTaskList = (ListView) findViewById(R.id.task_list);

        settingsBtn = findViewById(R.id.settingsButton);
        signInBtn = findViewById(R.id.sign_in_button);
        addCategory = findViewById(R.id.add_category);


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
        addCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(TaskList.this, AddCategoryActivity.class);
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
        SQLiteDatabase db = mHelper.getReadableDatabase();

        /*if(GoogleSignIn.getLastSignedInAccount(this) != null) {
            mQueue = Volley.newRequestQueue(this);
            mHelper.fetchTasksParse(this, mQueue, mHelper);
        }*/

        ArrayList<String> taskList = new ArrayList<>();
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

    String timeFormatted = "";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent i = new Intent(TaskList.this,AddCategoryActivity.class);
        startActivity(i);
        finish();*/
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        //Boolean emailNotificationSetting = sharedPreferences.getBoolean("NotificationEmail", false);
        //Boolean pushNotificationSetting = sharedPreferences.getBoolean("NotificationPush", false);
        Boolean hourFormatSetting = sharedPreferences.getBoolean("HourFormat", false);

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
                catEditText.setInputType(InputType.TYPE_NULL);
                catEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Category> cList = mHelper.getCategoryList();
                        String[] catOpt = new String[cList.size()];
                        for(int i = 0; i < cList.size(); i++) {
                            catOpt[i] = String.valueOf(cList.get(i));
                        }
                        AlertDialog.Builder catOptionsDialog = new AlertDialog.Builder(TaskList.this);
                        catOptionsDialog.setTitle("Choose a category.");
                        catOptionsDialog.setItems(catOpt, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface catOptionsDialog, int which) {
                                                catEditText.setText(catOpt[which]);
                                                }
                                        });
                        AlertDialog dialogCat = catOptionsDialog.create();
                        dialogCat.show();
                    }
                });
                layout.addView(catEditText);
                final EditText timeEditText = new EditText(this);
                timeEditText.setHint("Completion Time");
                timeEditText.setInputType(InputType.TYPE_NULL);
                timeEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);

                        // time picker dialog
                        tPicker = new TimePickerDialog(TaskList.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker tp, int hour, int minutes) {
                                        String formattedHour = "" + hour;
                                        String formattedMinutes = "" + minutes;
                                        if(hour < 10)
                                            formattedHour = "0" + hour;
                                        if(minutes < 10)
                                            formattedMinutes = "0" + minutes;
                                        timeFormatted = formattedHour + ":" + formattedMinutes;
                                        if(hourFormatSetting){
                                            timeEditText.setText(timeFormatted);
                                        }
                                        else{
                                            DateFormat originalFormat = new SimpleDateFormat("HH:mm");
                                            DateFormat targetFormat = new SimpleDateFormat(settings.toggleTime(hourFormatSetting));
                                            Date date = null;
                                            try {
                                                date = originalFormat.parse(timeFormatted);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String updateFormattedTime = targetFormat.format(date);
                                            timeEditText.setText(updateFormattedTime);
                                        }

                                    }
                                }, hour, minutes, hourFormatSetting);
                        tPicker.show();
                    }
                });
                layout.addView(timeEditText);
                final EditText dateEditText = new EditText(this);
                dateEditText.setHint("Completion Date");
                dateEditText.setInputType(InputType.TYPE_NULL);
                dateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        dPicker = new DatePickerDialog(TaskList.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;

                                if(month < 10){

                                    formattedMonth = "0" + month;
                                }
                                if(dayOfMonth < 10){

                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }
                                dateEditText.setText(formattedMonth + "/" + formattedDayOfMonth + "/" + year);
                                // dateEditText.setText(month + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                        dPicker.show();
                    }
                });
                layout.addView(dateEditText);
                final EditText recurEditText = new EditText(this);
                recurEditText.setHint("Recurrence");
                recurEditText.setInputType(InputType.TYPE_NULL);
                recurEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] recurOpt = { "DAILY", "WEEKDAYS", "WEEKLY", "MONTHLY", "YEARLY", "NONE" };
                        AlertDialog.Builder recurOptionsDialog = new AlertDialog.Builder(TaskList.this);
                        recurOptionsDialog.setTitle("How often do you want notifications?");
                        recurOptionsDialog.setItems(recurOpt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface recurOptionsDialog, int which) {
                                switch(which) {
                                    case 0: recurEditText.setText("DAILY");
                                        break;
                                    case 1: recurEditText.setText("WEEKDAYS");
                                        break;
                                    case 2: recurEditText.setText("WEEKLY");
                                        break;
                                    case 3: recurEditText.setText("MONTHLY");
                                        break;
                                    case 4: recurEditText.setText("YEARLY");
                                        break;
                                    case 5: recurEditText.setText("NONE");
                                }
                            }
                        });
                        AlertDialog dialogCat = recurOptionsDialog.create();
                        dialogCat.show();
                    }
                });
                layout.addView(recurEditText);
                /*final EditText emailET = new EditText(this);
                if(!emailNotificationSetting){
                    emailET.setText("No");
                }
                else{
                    emailET.setHint("Email Notifications?");
                    layout.addView(emailET);
                }
                final EditText pushET = new EditText(this);
                if(!pushNotificationSetting){
                    pushET.setText("No");
                }
                else{
                    pushET.setHint("Push Notifications?");
                    layout.addView(pushET);
                }*/
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());

                                String cat = String.valueOf(catEditText.getText());
                                Category category = new Category(cat);


                                String time = String.valueOf(timeFormatted);
                                String date = String.valueOf(dateEditText.getText());
                                String timeDate = new String(date + " " + time + ":00 CST");

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z");
                                ZonedDateTime dateTime = ZonedDateTime.parse(timeDate, formatter);

                                String recur = String.valueOf(recurEditText.getText());
                                Recur recurEnum = Recur.NONE;
                                if(recur.equalsIgnoreCase("DAILY"))
                                    recurEnum = Recur.DAILY;
                                if(recur.equalsIgnoreCase("WEEKDAYS"))
                                    recurEnum = Recur.WEEKDAYS;
                                if(recur.equalsIgnoreCase("WEEKLY"))
                                    recurEnum = Recur.WEEKLY;
                                if(recur.equalsIgnoreCase("MONTHLY"))
                                    recurEnum = Recur.MONTHLY;
                                if(recur.equalsIgnoreCase("YEARLY"))
                                    recurEnum = Recur.YEARLY;

                                //String email = String.valueOf(emailET.getText());
                                ZonedDateTime dateTimeEmail = dateTime;

                                //String push = String.valueOf(pushET.getText());
                                ZonedDateTime dateTimePush = dateTime;

                                TaskClass mTask = new TaskClass(task, category, dateTime, recurEnum, dateTimeEmail, dateTimePush, false);
                                mHelper.addTask(mTask);
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog AskOption(View view)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Complete")
                .setMessage("Mark this task as complete?")
                //.setIcon(R.drawable.delete)

                .setPositiveButton("Complete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //changing completion status and removing from task list
                        View parent = (View) view.getParent();
                        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
						/*ArrayList<TaskClass> tList = mHelper.getTaskList();                        TaskClass histTask;
                        String mTask = String.valueOf(taskTextView.getText());
                        for(int i = 0; i < tList.size(); i++) {
                            if(mTask == tList.get(i).getName()) {
                                histTask = new TaskClass(tList.get(i).getName(), tList.get(i).getCategory(),
                                        tList.get(i).getTime(), tList.get(i).getRecur(), tList.get(i).getEmail(),
                                        tList.get(i).getPush(), true);
                                mHelper.addTask(histTask);
                            }
                            else
                                continue;
                        }*/
                        String task = String.valueOf(taskTextView.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        db.delete(DBHelper.TASK_TABLE,
                                DBHelper.COLUMN_TASK_NAME + " = ?",
                                new String[]{task});
                        //mHelper.addTask()
                        db.close();
                        updateUI();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }
    public void deleteTask(View view) {
        AlertDialog dialogCheck = AskOption(view);
        dialogCheck.show();
        updateUI();
    }
}