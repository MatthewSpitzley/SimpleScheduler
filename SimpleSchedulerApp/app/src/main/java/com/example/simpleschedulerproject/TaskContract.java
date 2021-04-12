package com.example.simpleschedulerproject;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.simpleschedulerproject.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TASK_TABLE = "tasks";

        public static final String COLUMN_TASK_NAME = "title";
        public static final String COLUMN_TASK_CATEGORY = "category";
        public static final String COLUMN_TASK_TIME = "time";
        public static final String COLUMN_TASK_RECUR = "recur";
        public static final String COLUMN_TASK_EMAIL_NOTIFICATION = "notification";
        public static final String COLUMN_TASK_PUSH_NOTIFICATION = "push";
        public static final String COLUMN_TASK_COMPLETE = "complete";
        public static final String CATEGORY_TABLE = "cTable";
        public static final String COLUMN_CATEGORY_NAME = "cName";
    }
}