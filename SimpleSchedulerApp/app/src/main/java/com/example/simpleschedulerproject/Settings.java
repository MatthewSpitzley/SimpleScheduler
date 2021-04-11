package com.example.simpleschedulerproject;

import com.example.simpleschedulerproject.R;
import android.app.Activity;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.format.DateTimeFormatter;

public class Settings {
    //opens a text box that displays a message telling the user how to navigate the application
    public void showHelp(Activity settings){
        TextView box = new TextView(settings.getApplicationContext());
        box.setText("In both the task list and calendar screen, you will be able to see your various tasks and when they will be due. \n" +
                "Each task is separated based on its category. \n" +
                "The tasks that have already been marked as complete can be found in the History Screen, which is found by swiping left from the main screen. \n" +
                "To create a new task, select the + button on the bottom right, and fill out any necessary fields. \n" +
                "To access the Category List, select the drop down in the top left. \n" +
                "\t To create a new category, continue from the Category List and select the option 'New Category' \n" +
                "To access the Settings screen select the button in the top right");
    }

    //changes between 12 and 24 hour times
    public DateTimeFormatter toggleTime(boolean x){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("hh:mm aa");
        if(!x){
            //12 hr
            df = DateTimeFormatter.ofPattern("hh:mm aa");
        }
        else{
            //24 hr
            df = DateTimeFormatter.ofPattern("HH:mm");
        }
        return df;
    }

    //changes the main screen to either the task list or calendar screen
    public void switchScreen(){
        //if true (default) do nothing, if false as the main activity launches make it redirect to the calendar screen on startup
    }

   //allows the user to change the time before a task occurs to send a reminder notification
    public int notificationTiming(int x){
        return 60;
    }

    //user changes how they prefer to see dates displayed
    public DateTimeFormatter dateDisplay(int x){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("hh:mm aa");
        switch(x){
            case 0:
                //month number/day number/year number
                df = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                break;
            case 1:
                //day number/month number/year number
                df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                break;
            case 2:
                //abbreviated month _ day number _ year number
                df = DateTimeFormatter.ofPattern("MMM dd yyyy");
                break;
            case 3:
                //day number _ abbreviated month _ year number
                df = DateTimeFormatter.ofPattern("dd MMM yyyy");
                break;
            case 4:
                //full month _ day number _ year number
                df = DateTimeFormatter.ofPattern("MMMM dd yyyy");
                break;
            case 5:
                //day number _ full month _ year number
                df = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                break;
        }
        return df;
    }

    //user enables whether or not they want late notifications
    public void lateNotification(){

    }

    //user can choose whether or not to receive push and/or email notifications
    public void notificationEmail(){

    }

    public void notificationPush(){

    }

    //user can sign in through google
    public void googleSignIn(){

    }
}
