package com.example.gita.eatreminder.model;

public class Reminder {
    private int id;
    private String type;
    private String note;
    private long startTime;

    public Reminder() {
    }

    public Reminder(String type, String note, long startTime) {
        this.type = type;
        this.note = note;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
