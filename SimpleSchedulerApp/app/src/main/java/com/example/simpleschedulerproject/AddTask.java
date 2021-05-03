package com.example.simpleschedulerproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddTask extends AppCompatActivity {
    private DBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add);
        EditText taskName = findViewById(R.id.task_name);
        taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = String.valueOf(taskName.getText());
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, task);
                db.insertWithOnConflict(TaskContract.TaskEntry.TASK_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
            }
        });

        Spinner CatSpinner = (Spinner) findViewById(R.id.catSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recur_choices, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        CatSpinner.setAdapter(adapter);




        // db.insertWithOnConflict(TaskContract.TaskEntry.TASK_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        // values.put(TaskContract.TaskEntry.COLUMN_TASK_TIME, String.valueOf(timeEditText));
        // db.insertWithOnConflict(TaskContract.TaskEntry.TASK_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        // db.close();
    }
}
