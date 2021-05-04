package com.example.simpleschedulerproject;

import java.time.format.DateTimeFormatter;

public class Settings {
    //changes between 12 and 24 hour times
    public static String toggleTime(boolean x){
        String timeFormat = "hh:mm aa";
        if(!x){
            //12 hr
            timeFormat = "hh:mm aa";
        }
        else{
            //24 hr
            timeFormat = "HH:mm";
        }
        return timeFormat;
    }

   //allows the user to change the time before a task occurs to send a reminder notification
    public int notificationTiming(int x){
        int timing = 60;

        switch(x) {
            default:
                timing=0;
                break;
            case 0:
                timing = 0;
                break;
            case 1:
                timing = 15;
                break;
            case 2:
                timing = 30;
                break;
            case 3:
                timing = 45;
                break;
            case 4:
                timing = 60;
                break;
            case 5:
                timing = 90;
                break;
            case 6:
                timing = 120;
                break;
        }

        return timing;
    }

    //user changes how they prefer to see dates displayed
    public String dateDisplay(int x){
        String pattern;
        switch(x){
            default:
                pattern = "MM/dd/yyyy";
                break;
            case 0:
                //month number/day number/year number
                pattern = "MM/dd/yyyy";
                break;
            case 1:
                //day number/month number/year number
                pattern = "dd/MM/yyyy";
                break;
            case 2:
                //abbreviated month _ day number _ year number
                pattern = "MMM dd yyyy";
                break;
            case 3:
                //day number _ abbreviated month _ year number
                pattern = "dd MMM yyyy";
                break;
            case 4:
                //full month _ day number _ year number
                pattern = "MMMM dd yyyy";
                break;
            case 5:
                //day number _ full month _ year number
                pattern = "dd MMMM yyyy";
                break;
        }
        return pattern;
    }

    /*//user enables whether or not they want late notifications
    public void lateNotification(boolean x){

    }

    //user can choose whether or not to receive push and/or email notifications
    public void notificationEmail(boolean x){

    }

    public void notificationPush(boolean x){

    }*/
}
