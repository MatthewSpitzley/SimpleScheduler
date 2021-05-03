package com.example.simpleschedulerproject;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.content.DialogInterface;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void showAlertDialogButtonClicked(View view) {

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            //Preference signInButton = findPreference(getString(R.string.GoogleSignIn));
            Preference helpButton = findPreference(getString(R.string.help));
            /*signInButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    return true;
                }
            });*/
            helpButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("What would you like help with?");
                    // add a list
                    String[] options = {"Tasks", "Categories", "Task List Screen", "Category Screen", "Calendar Screen", "History Screen", "Settings Screen"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: // Tasks
                                        AlertDialog.Builder builderTask = new AlertDialog.Builder(getContext());
                                        builderTask.setTitle("Tasks");
                                        builderTask.setMessage("The tasks you make in this app can be made of different components such as ");
                                        // add a button
                                        builderTask.setNegativeButton("OK", null);
                                        // create and show the alert dialog
                                        AlertDialog dialogTask = builderTask.create();
                                        dialogTask.show();
                                        break;
                                case 1: // Categories
                                        AlertDialog.Builder builderCategories = new AlertDialog.Builder(getContext());
                                        builderCategories.setTitle("Categories");
                                        builderCategories.setMessage("The categories are an optional addition to tasks that are used for grouping or organizing many tasks of the same topic together " +
                                                "\n When creating a new category, the only field that needs to be entered is the name field " +
                                                "\n To add a new category, open the category drop down menu and select 'New Category'");
                                        builderCategories.setPositiveButton("OK", null);
                                        AlertDialog dialogCategories = builderCategories.create();
                                        dialogCategories.show();
                                        break;
                                case 2: // Task List Screen
                                        AlertDialog.Builder builderTaskScreen = new AlertDialog.Builder(getContext());
                                        builderTaskScreen.setTitle("Tasks List");
                                        builderTaskScreen.setMessage("This screen shows the complete list of all tasks that are currently active, and is sorted by the date first and then alphabetically " +
                                                "\n When you want to add a new task, go to the bottom right and select the button that looks like a +");
                                        builderTaskScreen.setPositiveButton("OK", null);
                                        AlertDialog dialogTaskScreen = builderTaskScreen.create();
                                        dialogTaskScreen.show();
                                        break;
                                case 3: // Category Screen
                                        AlertDialog.Builder builderCategoriesScreen = new AlertDialog.Builder(getContext());
                                        builderCategoriesScreen.setTitle("Category List");
                                        builderCategoriesScreen.setMessage("The category list shows the complete list of categories, sorted alphabetically " +
                                                "\n To add a new category, open the category drop down menu and select 'New Category' ");
                                        builderCategoriesScreen.setPositiveButton("OK", null);
                                        AlertDialog dialogCategoriesScreen = builderCategoriesScreen.create();
                                        dialogCategoriesScreen.show();
                                        break;
                                case 4: // Calendar Screen
                                        AlertDialog.Builder builderCalendarScreen = new AlertDialog.Builder(getContext());
                                        builderCalendarScreen.setTitle("Calendar Screen");
                                        builderCalendarScreen.setMessage("This screen displays the same information as the task list, except in a visual calendar form " +
                                                "\nIf you want to see the tasks for a certain day, select a day on the calendar, and every task for that day will be displayed underneath the calendar" +
                                                "\nThe components of the tasks that will be displayed (if applicable) are the: task name, category, and time to be completed");
                                        builderCalendarScreen.setPositiveButton("OK", null);
                                        AlertDialog dialogCalendarScreen = builderCalendarScreen.create();
                                        dialogCalendarScreen.show();
                                        break;
                                case 5: // History Screen
                                        AlertDialog.Builder builderHistoryScreen = new AlertDialog.Builder(getContext());
                                        builderHistoryScreen.setTitle("History Screen");
                                        builderHistoryScreen.setMessage("This screen shows all previously completed tasks " +
                                                "\nFor each task displayed, it will show (if applicable): " +
                                                "\n\t the tasks name, " +
                                                "\n\t the category of the task, " +
                                                "\n\t the time the task was set to be completed, " +
                                                "\n\t and the date the task was set to be completed");
                                        builderHistoryScreen.setNegativeButton("OK", null);
                                        AlertDialog dialogHistoryScreen = builderHistoryScreen.create();
                                        dialogHistoryScreen.show();
                                        break;
                                case 6: // Settings Screen
                                        AlertDialog.Builder builderSettingsScreen = new AlertDialog.Builder(getContext());
                                        builderSettingsScreen.setTitle("Settings Screen");
                                        builderSettingsScreen.setMessage("This is my message.");
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