package com.example.simpleschedulerproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.view.MotionEvent;
import android.widget.TimePicker;
import android.widget.Toast;

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
    private TimePickerDialog tPicker;
    private DatePickerDialog dPicker;
    private DBHelper mHelper;
    private ListView mTaskList;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_screen);

        calendar = (CalendarView)findViewById(R.id.calendar);
        dateView = (TextView)findViewById(R.id.date_view);
        noDate = (Button)findViewById(R.id.noDate);
        settingsBtn = findViewById(R.id.settings);
        signInBtn = findViewById(R.id.sign_in_button);
        mHelper = new DBHelper(this);
        mTaskList = (ListView) findViewById(R.id.task_list);


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
                updateUI("No Date");
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
                String formattedMonth = ""+(month+1);
                if(month < 10){

                    formattedMonth = "0" + (month+1);
                }
                String formattedDayOfMonth = ""+dayOfMonth;
                if(dayOfMonth < 10){

                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                String dateString = formattedMonth + "/" + formattedDayOfMonth + "/" + year;
                DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
                DateFormat targetFormat = new SimpleDateFormat(settings.dateDisplay(dateDisplayFormat));
                Date date = null;
                try {
                    date = originalFormat.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = targetFormat.format(date);
                dateView.setText(formattedDate);

                String time = "10:10";
                //dateString
                String timeDate = dateString + " " + time + ":00 CST";

                DateTimeFormatter zdtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z");
                ZonedDateTime zdt = ZonedDateTime.parse(timeDate, zdtFormatter);
                updateUI(zdt);
            }
        });
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TASK_TABLE, new String[]{DBHelper.COLUMN_TASK_NAME}, null, null, null, null, null);
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

    private void updateUI(ZonedDateTime zdt){
        mAdapter.clear();
        //ArrayList taskList = mHelper.getTaskList(zdt);
        //ArrayList<TaskClass> taskList = mHelper.getTaskList();
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i<mHelper.getTaskList().size(); i++){
            //time.getDayOfMonth() == calendarDate.getDayOfMonth() && time.getMonth() == calendarDate.getMonth() && time.getYear() == calendarDate.getYear()
            if(mHelper.getTaskList().get(i).getTime().getDayOfMonth()==zdt.getDayOfMonth() && mHelper.getTaskList().get(i).getTime().getMonth()==zdt.getMonth() && mHelper.getTaskList().get(i).getTime().getYear()==zdt.getYear()){
                result.add(mHelper.getTaskList().get(i).getName());
            }
        }
        mAdapter.addAll(result);
    }

    private void updateUI(String noDate){
        mAdapter.clear();

        ArrayList taskList = mHelper.getTaskList(noDate);
        mAdapter.addAll(taskList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    String timeFormatted = "";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                        AlertDialog.Builder catOptionsDialog = new AlertDialog.Builder(CalendarScreen.this);
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
                        tPicker = new TimePickerDialog(CalendarScreen.this,
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
                String dateEditDefault = String.valueOf(dateEditText.getText());
                dateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        dPicker = new DatePickerDialog(CalendarScreen.this, new DatePickerDialog.OnDateSetListener() {
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
                /*final EditText recurEditText = new EditText(this);
                recurEditText.setHint("Recurrence");
                recurEditText.setInputType(InputType.TYPE_NULL);
                recurEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] recurOpt = { "DAILY", "WEEKDAYS", "WEEKLY", "MONTHLY", "YEARLY" };
                        AlertDialog.Builder recurOptionsDialog = new AlertDialog.Builder(CalendarScreen.this);
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
                                }
                            }
                        });
                        AlertDialog dialogCat = recurOptionsDialog.create();
                        dialogCat.show();
                    }
                });
                layout.addView(recurEditText);*/
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
                                String timeDate = null;
                                ZonedDateTime dateTime = null;
                                if(true) {
                                    Toast.makeText(CalendarScreen.this, date, Toast.LENGTH_SHORT).show();
                                    timeDate = new String(date + " " + time + ":00 CST");

                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z");
                                    dateTime = ZonedDateTime.parse(timeDate, formatter);
                                }
                                else{
                                    date = null;
                                }

                                /*String recur = String.valueOf(recurEditText.getText());
                                Recur recurEnum = Recur.NONE;
                                if(recur.equalsIgnoreCase(""))
                                    recurEnum = Recur.NONE;
                                if(recur.equalsIgnoreCase("DAILY"))
                                    recurEnum = Recur.DAILY;
                                if(recur.equalsIgnoreCase("WEEKDAYS"))
                                    recurEnum = Recur.WEEKDAYS;
                                if(recur.equalsIgnoreCase("WEEKLY"))
                                    recurEnum = Recur.WEEKLY;
                                if(recur.equalsIgnoreCase("MONTHLY"))
                                    recurEnum = Recur.WEEKLY;
                                if(recur.equalsIgnoreCase("YEARLY"))
                                    recurEnum = Recur.YEARLY;*/

                                //String email = String.valueOf(emailET.getText());
                                ZonedDateTime dateTimeEmail = dateTime;

                                //String push = String.valueOf(pushET.getText());
                                ZonedDateTime dateTimePush = dateTime;

                                TaskClass mTask = new TaskClass(task, category, dateTime, Recur.DAILY, dateTimeEmail, dateTimePush, false);
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
        updateUI();
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
                    Intent i = new Intent(CalendarScreen.this, TaskList.class);
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