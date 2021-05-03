package com.example.simpleschedulerproject;

import java.time.ZonedDateTime;
import java.util.prefs.Preferences;

public class Task {
    private String name;
    private Category category;
    private ZonedDateTime time;
    private Recur recur;
    private ZonedDateTime email;
    private ZonedDateTime push;
    private boolean complete;
    private Settings settings;

    public Task(String name, Category category, ZonedDateTime time, Recur recur, ZonedDateTime email, ZonedDateTime push, boolean complete) {
        this.name = name;
        this.category = category;
        this.time = time;
        this.recur = recur;
        this.email = email;
        this.push = push;
        this.complete = complete;
    }

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Recur getRecur() {
        return recur;
    }

    public void setRecur(Recur recur) {
        this.recur = recur;
    }

    public ZonedDateTime getEmail() {
        return email;
    }

    public void setEmail(ZonedDateTime email) {
        this.email = email;
    }

    public ZonedDateTime getPush() {
        return push;
    }

    public void setPush(ZonedDateTime push) {
        this.push = push;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        int x = 0;
        return name +
                ": category =" + category.toString() +
           //    ", time =" + time.format(settings.dateDisplay(Preferences.userRoot().getInt("DateChoices", x))) +
                ", recur =" + recur.toString() +
                ", email =" + email.toString() +
                ", push =" + push.toString() +
                ", complete =" + complete +
                '.';
    }
}

enum Recur {
    DAILY("Daily"),
    WEEKDAYS("Weekdays"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly");

    private final String recur;

    Recur(String recur) {
        this.recur = recur;
    }

    @Override
    public String toString() {
        return recur;
    }
}
