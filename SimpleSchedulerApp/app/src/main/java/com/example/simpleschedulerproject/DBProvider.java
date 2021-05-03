package com.example.simpleschedulerproject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Nullable;

import org.jumpmind.symmetric.android.SQLiteOpenHelperRegistry;
import org.jumpmind.symmetric.android.SymmetricService;
import org.jumpmind.symmetric.common.ParameterConstants;

import java.util.Properties;

public class DBProvider extends ContentProvider  {

    private final String REGISTRATION_URL = "localhost";
    private final String NODE_ID = "android-001";
    private final String NODE_GROUP = "ss";
    public static final String DATABASE_NAME = "ss-db";

    // Handle to a new DatabaseHelper.
    private DBHelper mOpenHelper;

    /**
     * Initializes the provider by creating a new DatabaseHelper. onCreate() is called
     * automatically when Android creates the provider in response to a resolver request from a
     * client.
     */
    @Override
    public boolean onCreate() {

        // Creates a new helper object. Note that the database itself isn't opened until
        // something tries to access it, and it's only created if it doesn't already exist.
        mOpenHelper = new DBHelper(getContext());

        // Register the database helper, so it can be shared with the SymmetricService
        SQLiteOpenHelperRegistry.register(DATABASE_NAME, mOpenHelper);

        Intent intent = new Intent(getContext(), SymmetricService.class);

        intent.putExtra(SymmetricService.INTENTKEY_SQLITEOPENHELPER_REGISTRY_KEY, DATABASE_NAME);
        intent.putExtra(SymmetricService.INTENTKEY_REGISTRATION_URL, REGISTRATION_URL);
        intent.putExtra(SymmetricService.INTENTKEY_EXTERNAL_ID, NODE_ID);
        intent.putExtra(SymmetricService.INTENTKEY_NODE_GROUP_ID, NODE_GROUP);
        intent.putExtra(SymmetricService.INTENTKEY_START_IN_BACKGROUND, true);


        Properties properties = new Properties();
        properties.put(ParameterConstants.FILE_SYNC_ENABLE, "true");
        properties.put("start.file.sync.tracker.job", "true");
        properties.put("start.file.sync.push.job", "true");
        properties.put("start.file.sync.pull.job", "true");
        properties.put("job.file.sync.pull.period.time.ms", "10000");

        intent.putExtra(SymmetricService.INTENTKEY_PROPERTIES, properties);

        getContext().startService(intent);

        // Assumes that any failures will be reported by a thrown exception.
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    public String getType(Uri uri) {

        throw new IllegalArgumentException("Unknown URI " + uri);

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}