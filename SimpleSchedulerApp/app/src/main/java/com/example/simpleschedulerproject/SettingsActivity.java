package com.example.simpleschedulerproject;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.settings, new SettingsFragment()).commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //ensures the back button on works as intended
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //Starts the root_preference.xml so user can make preference changes
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference helpButton = findPreference(getString(R.string.help));
            helpButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("What would you like help with?");
                    // add a list
                    String[] options = {"Tasks", "Categories", "Task List Screen", "Category Screen", "Calendar Screen", "Settings Screen"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: // Tasks
                                        AlertDialog.Builder builderTask = new AlertDialog.Builder(getContext());
                                        builderTask.setTitle("Tasks");
                                        builderTask.setMessage("The tasks you make in this app can be made of different components such as " +
                                                "\n\tThe name of the task" +
                                                "\n\tThe category of the task" +
                                                "\n\tThe date and time of completion");
                                        // add a button
                                        builderTask.setNegativeButton("OK", null);
                                        // create and show the alert dialog
                                        AlertDialog dialogTask = builderTask.create();
                                        dialogTask.show();
                                        break;
                                case 1: // Categories
                                        AlertDialog.Builder builderCategories = new AlertDialog.Builder(getContext());
                                        builderCategories.setTitle("Categories");
                                        builderCategories.setMessage("The categories are an optional addition to tasks that are used for grouping or organizing many tasks of the same topic together" +
                                                "\nTo add a new category go to the task list and press the add category button at the top left");
                                        builderCategories.setPositiveButton("OK", null);
                                        AlertDialog dialogCategories = builderCategories.create();
                                        dialogCategories.show();
                                        break;
                                case 2: // Task List Screen
                                        AlertDialog.Builder builderTaskScreen = new AlertDialog.Builder(getContext());
                                        builderTaskScreen.setTitle("Tasks List");
                                        builderTaskScreen.setMessage("This screen shows the complete list of all tasks that are currently active, " +
                                                "\n When you want to add a new task, go to the top right and press the '+' button ");
                                        builderTaskScreen.setPositiveButton("OK", null);
                                        AlertDialog dialogTaskScreen = builderTaskScreen.create();
                                        dialogTaskScreen.show();
                                        break;
                                case 3: // Category Dropdown Menu
                                        AlertDialog.Builder builderCategoriesScreen = new AlertDialog.Builder(getContext());
                                        builderCategoriesScreen.setTitle("Category Add Menu");
                                        builderCategoriesScreen.setMessage("The category add menu shows a text box to add a new category and a back button to go back and a add button to add the category name to the list of categories");
                                        builderCategoriesScreen.setPositiveButton("OK", null);
                                        AlertDialog dialogCategoriesScreen = builderCategoriesScreen.create();
                                        dialogCategoriesScreen.show();
                                        break;
                                case 4: // Calendar Screen
                                        AlertDialog.Builder builderCalendarScreen = new AlertDialog.Builder(getContext());
                                        builderCalendarScreen.setTitle("Calendar Screen");
                                        builderCalendarScreen.setMessage("This screen displays the same information as the task list, except in a visual calendar form " +
                                                "\nIf you want to see the tasks for a certain day, select a day on the calendar, and every task for that day will be displayed underneath the calendar" +
                                                "\nFor tasks that do not have a date select the button 'Tasks with no date'");
                                        builderCalendarScreen.setPositiveButton("OK", null);
                                        AlertDialog dialogCalendarScreen = builderCalendarScreen.create();
                                        dialogCalendarScreen.show();
                                        break;
                                case 5: // Settings Screen
                                        AlertDialog.Builder builderSettingsScreen = new AlertDialog.Builder(getContext());
                                        builderSettingsScreen.setTitle("Settings Screen");
                                        builderSettingsScreen.setMessage("The settings are accessible from the Calendar and Task List screen" +
                                                "\nYou can change your preferred settings and how you would like to interact with the app here" +
                                                "\n\tHelp: Gives you messages based on what you need help with in order to better your understanding of how to use this app" +
                                                "\n\tDisplay Screen: Changes the first screen you see at startup" +
                                                "\n\tHour Time: Changes how you would like to view time in terms of hours" +
                                                "\n\tDate Display Format: Changes the format of dates to your preference" /*+
                                                "\n\tLate Notifications: Changes whether or not you want notifications sent when a task is late" +
                                                "\n\tEmail Notifications: Changes whether or not you have notifications sent to your email" +
                                                "\n\tPush Notifications: Changes whether or not you have notifications sent to your personal device"*/);
                                        builderSettingsScreen.setNegativeButton("OK", null);
                                        AlertDialog dialogSettingsScreen = builderSettingsScreen.create();
                                        dialogSettingsScreen.show();
                                        break;
                            }
                        }
                    });
                    builder.setNegativeButton("Back", null);

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
        }
    }
}