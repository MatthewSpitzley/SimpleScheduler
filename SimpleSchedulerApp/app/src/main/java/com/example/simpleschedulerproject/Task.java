package main.java.com.example.simpleschedulerproject;

import java.time.ZonedDateTime;

public class Task {
    private String name;
    private Category category;
    private ZonedDateTime time;
    private Recur recur;
    private ZonedDateTime email;
    private ZonedDateTime push;
    private boolean complete;

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
        return "Task{" +
                "name='" + name + '\'' +
                ", category=" + category +
                ", time=" + time +
                ", recur=" + recur +
                ", email=" + email +
                ", push=" + push +
                ", complete=" + complete +
                '}';
    }
}

enum Recur {
    Daily,
    Weekdays,
    Weekly,
    Monthly,
    Yearly
}
