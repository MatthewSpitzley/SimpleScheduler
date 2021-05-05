package com.example.simpleschedulerproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "com.example.simpleschedulerproject.db";
    public static final int DB_VERSION = 1;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoryTableStatement = "CREATE TABLE " + CATEGORY_TABLE + " ( " + COLUMN_CATEGORY_NAME + " TEXT PRIMARY KEY );";
        String createTaskTableStatement = "CREATE TABLE " + TASK_TABLE + " ( " + COLUMN_TASK_NAME + " TEXT PRIMARY KEY, " + COLUMN_TASK_CATEGORY + " TEXT, " + COLUMN_TASK_TIME + " TEXT, " + COLUMN_TASK_RECUR + " TEXT, " + COLUMN_TASK_EMAIL_NOTIFICATION + " TEXT, " + COLUMN_TASK_PUSH_NOTIFICATION + " TEXT, " + COLUMN_TASK_COMPLETE + " INTEGER, FOREIGN KEY (" + COLUMN_TASK_CATEGORY + ") REFERENCES " + CATEGORY_TABLE + " (" + COLUMN_CATEGORY_NAME + ") );";

        db.execSQL(createCategoryTableStatement);
        db.execSQL(createTaskTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        onCreate(db);
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

    public boolean addTask(TaskClass task) {
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

    public boolean deleteCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + CATEGORY_TABLE + " WHERE " + COLUMN_CATEGORY_NAME + " = " + category.getName();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteTask(TaskClass task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TASK_TABLE + " WHERE " + COLUMN_TASK_NAME + " = " + task.getName();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    public void fetchCategoriesParse(RequestQueue mQueue, DBHelper mHelper) {
        String url = "http://localhost/fetchCategories.php";

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("categories");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject category = jsonArray.getJSONObject(i);

                                String category_name = category.getString("category");

                                mHelper.addCategory(new Category(category_name));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        mQueue.add(request);
    }

    public void fetchTasksParse(RequestQueue mQueue, DBHelper mHelper) {
        String url = "https://localhost/fetchTasks.php";

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject task = response.getJSONObject(i);

                                String task_name = task.getString("task_name");
                                Category category = new Category(task.getString("category"));
                                ZonedDateTime time = ZonedDateTime.parse(task.getString("time"));
                                Recur recur = Recur.valueOf(task.getString("recur"));
                                ZonedDateTime email = ZonedDateTime.parse(task.getString("email_notification"));
                                ZonedDateTime push = ZonedDateTime.parse(task.getString("push_notification"));
                                boolean complete = task.getInt("completed") == 1;

                                mHelper.addTask(new TaskClass(task_name, category, time, recur, email, push, complete));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }

    private void syncCategoriesParse(RequestQueue mQueue) {
        ArrayList<Category> categories = getCategoryList();

        String jsonData = "{";
        for(int i = 0; i < categories.size(); i++) {
            if(i != categories.size() - 1){
                jsonData += "\"category\":\"" + categories.get(i).getName() + "\",";
            }
            else {
                jsonData += "\"category\":" + categories.get(i).getName() + "\"";
            }
        }
        jsonData += "}";

        syncCategoriesHelper(jsonData, mQueue);
    }

    private void syncCategoriesHelper(String jsonData, RequestQueue mQueue) {
        String url = "http://localhost/syncCategories.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonData == null ? null : jsonData.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

        };

        mQueue.add(stringRequest);
    }

    private void syncTasksParse(RequestQueue mQueue) {
        ArrayList<TaskClass> tasks = getTaskList();

        String jsonData = "{";
        for(int i = 0; i < tasks.size(); i++) {
            if(i != tasks.size() - 1) {
                jsonData += "\"task_name\":\"" + tasks.get(i).getName() + "\"," +
                        "\"category\":\"" + tasks.get(i).getCategory().getName() + "\"," +
                        "\"time\":\"" + tasks.get(i).getTime().toString() + "\"," +
                        "\"recur\":\"" + tasks.get(i).getRecur().toString() + "\"," +
                        "\"email_notification\":\"" + tasks.get(i).getEmail().toString() + "\"," +
                        "\"push_notification\":\"" + tasks.get(i).getPush().toString() + "\"," +
                        "\"completed\":\"" + tasks.get(i).isComplete() + "\",";
            }
            else {
                jsonData += "\"task_name\":\"" + tasks.get(i).getName() + "\"," +
                        "\"category\":\"" + tasks.get(i).getCategory().getName() + "\"," +
                        "\"time\":\"" + tasks.get(i).getTime().toString() + "\"," +
                        "\"recur\":\"" + tasks.get(i).getRecur().toString() + "\"," +
                        "\"email_notification\":\"" + tasks.get(i).getEmail().toString() + "\"," +
                        "\"push_notification\":\"" + tasks.get(i).getPush().toString() + "\"," +
                        "\"completed\":\"" + tasks.get(i).isComplete() + "\"";
            }
        }
        jsonData += "}";

        syncTasksHelper(jsonData, mQueue);
    }

    private void syncTasksHelper(String jsonData, RequestQueue mQueue) {
        String url = "http://localhost/syncTasks.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonData == null ? null : jsonData.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

        };

        mQueue.add(stringRequest);
    }

    public ArrayList<Category> getCategoryList() {
        ArrayList<Category> returnList = new ArrayList<>();

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

    //get all task list
    public ArrayList<TaskClass> getTaskList() {
        ArrayList<TaskClass> returnList = new ArrayList<>();

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
                boolean complete = cursor.getInt(6) == 1;

                TaskClass newTask = new TaskClass(name, category, time, recur, email, push, complete);
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

    //get task list of given day
    public ArrayList<String> getTaskList(ZonedDateTime calendarDate) {
        ArrayList<String> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TASK_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                ZonedDateTime time = ZonedDateTime.parse(cursor.getString(2));
                if(time.getDayOfMonth() == calendarDate.getDayOfMonth() && time.getMonth() == calendarDate.getMonth() && time.getYear() == calendarDate.getYear()){
                    String name = cursor.getString(0);

                    returnList.add(name);
                }
                else{
                    //Failure. Task was not in the same calendar month/day/year
                }
            } while (cursor.moveToNext());
        }
        else {
            //Failure. Nothing is added to returnList.
        }

        cursor.close();
        db.close();
        return returnList;
    }

    //get task list of no dates
    public ArrayList<String> getTaskList(String noDate) {
        ArrayList<String> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TASK_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                ZonedDateTime time = ZonedDateTime.parse(cursor.getString(2));
                if(time == null){
                    String name = cursor.getString(0);

                    returnList.add(name);
                }
                else{
                    //Failure. Task has a date
                }

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
