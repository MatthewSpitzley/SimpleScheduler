package com.example.simpleschedulerproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String CATEGORY_TABLE = "CATEGORY_TABLE";
    public static final String COLUMN_CATEGORY_NAME = "COLUMN_CATEGORY_NAME";
    public static final String TASK_TABLE = "TASK_TABLE";
    public static final String COLUMN_TASK_NAME = "COLUMN_TASK_NAME";
    public static final String COLUMN_TASK_CATEGORY = "COLUMN_TASK_CATEGORY";
    public static final String COLUMN_TASK_TIME = "COLUMN_TASK_TIME";
    public static final String COLUMN_TASK_RECUR = "COLUMN_TASK_RECUR";
    public static final String COLUMN_TASK_EMAIL_NOTIFICATION = "COLUMN_TASK_EMAIL_NOTIFICATION";
    public static final String COLUMN_TASK_PUSH_NOTIFICATION = "COLUMN_TASK_PUSH_NOTIFICATION";
    public static final String COLUMN_TASK_COMPLETE = "COLUMN_TASK_COMPLETE";

    public DBHelper(Context context) {
        super(context, "scheduler.db", null, 1);
    }

    /**
     * Called when the SQLite DB is first created.
     * @param db database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoryTableStatement = "CREATE TABLE " + CATEGORY_TABLE + " ( " + COLUMN_CATEGORY_NAME + " TEXT PRIMARY KEY );";
        String createTaskTableStatement = "CREATE TABLE " + TASK_TABLE + " ( " + COLUMN_TASK_NAME + " TEXT PRIMARY KEY, " + COLUMN_TASK_CATEGORY + " TEXT, " + COLUMN_TASK_TIME + " TEXT, " + COLUMN_TASK_RECUR + " TEXT, " + COLUMN_TASK_EMAIL_NOTIFICATION + " TEXT, " + COLUMN_TASK_PUSH_NOTIFICATION + " TEXT, " + COLUMN_TASK_COMPLETE + " INTEGER, FOREIGN KEY (" + COLUMN_TASK_CATEGORY + ") REFERENCES " + CATEGORY_TABLE + " (" + COLUMN_CATEGORY_NAME + ") );";

        db.execSQL(createCategoryTableStatement);
        db.execSQL(createTaskTableStatement);
    }

    /**
     * Called when the version number changes.
     * @param db database
     * @param oldVersion old database version
     * @param newVersion new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CATEGORY_NAME, category.getName());

        long insert = db.insert(CATEGORY_TABLE, null, cv);
        if (insert == -1)
            return false;
        else
            return true;
    }

    public boolean addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK_NAME, task.getName());
        cv.put(COLUMN_TASK_CATEGORY, task.getCategory().getName());
        cv.put(COLUMN_TASK_TIME, task.getTime().toString());
        cv.put(COLUMN_TASK_RECUR, task.getRecur().toString());
        cv.put(COLUMN_TASK_EMAIL_NOTIFICATION, task.getEmail().toString());
        cv.put(COLUMN_TASK_PUSH_NOTIFICATION, task.getPush().toString());
        cv.put(COLUMN_TASK_COMPLETE, task.isComplete());

        long insert = db.insert(TASK_TABLE, null, cv);
        if (insert == -1)
            return false;
        else
            return true;
    }

    public List<Category> getCategoryList() {
        List<Category> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + CATEGORY_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);

                Category newCategory = new Category(name);
                returnList.add(newCategory);
            } while (cursor.moveToNext());
        }
        else {
            //Failure. Nothing is added to returnList.
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public List<Task> getTaskList() {
        List<Task> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TASK_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                Category category = new Category(cursor.getString(1));
                ZonedDateTime time = ZonedDateTime.parse(cursor.getString(2));
                Recur recur = Recur.valueOf(cursor.getString(3));
                ZonedDateTime email = ZonedDateTime.parse(cursor.getString(4));
                ZonedDateTime push = ZonedDateTime.parse(cursor.getString(5));
                boolean complete = cursor.getInt(6) == 1 ? true: false;

                Task newTask = new Task(name, category, time, recur, email, push, complete);
                returnList.add(newTask);
            } while (cursor.moveToNext());
        }
        else {
            //Failure. Nothing is added to returnList.
        }

        cursor.close();
        db.close();
        return returnList;
    }
}
