package com.example.simpleschedulerproject;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.simpleschedulerproject.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TASK_TABLE = "TASK_TABLE";


        public static final String COLUMN_TASK_NAME = "COLUMN_TASK_NAME";
        public static final String COLUMN_TASK_CATEGORY = "COLUMN_TASK_CATEGORY";
        public static final String COLUMN_TASK_TIME = "COLUMN_TASK_TIME";
        public static final String COLUMN_TASK_RECUR = "COLUMN_TASK_RECUR";
        public static final String COLUMN_TASK_EMAIL_NOTIFICATION = "COLUMN_TASK_EMAIL_NOTIFICATION";
        public static final String COLUMN_TASK_PUSH_NOTIFICATION = "COLUMN_TASK_PUSH_NOTIFICATION";
        public static final String COLUMN_TASK_COMPLETE = "COLUMN_TASK_COMPLETE";

    }
}