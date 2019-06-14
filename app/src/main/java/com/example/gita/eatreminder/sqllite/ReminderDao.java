package com.example.gita.eatreminder.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.gita.eatreminder.model.Reminder;

import java.util.ArrayList;

public class ReminderDao {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TYPE,
            MySQLiteHelper.COLUMN_NOTE,
            MySQLiteHelper.COLUMN_START,};

    public ReminderDao(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Reminder createReminder(Reminder newReminder) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_TYPE, newReminder.getType());
        values.put(MySQLiteHelper.COLUMN_NOTE, newReminder.getNote());
        values.put(MySQLiteHelper.COLUMN_START, newReminder.getStartTime());

        long insertId = database.insert(MySQLiteHelper.TABLE_REMINDER, null,
                values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_REMINDER,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        Reminder nReminder = cursorToReminder(cursor);

        cursor.close();

        return nReminder;
    }

    public void deleteReminder(Reminder reminder) {
        int id = reminder.getId();
        database.delete(MySQLiteHelper.TABLE_REMINDER, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Reminder> getAllReminder() {
        ArrayList<Reminder> reminders = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_REMINDER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Reminder reminder = cursorToReminder(cursor);
            reminders.add(reminder);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return reminders;
    }
    //
    private Reminder cursorToReminder(Cursor cursor) {
        Reminder newReminder = new Reminder();
        newReminder.setId(cursor.getInt(0));
        newReminder.setType(cursor.getString(1));
        newReminder.setNote(cursor.getString(2));
        newReminder.setStartTime(cursor.getLong(3));

        return newReminder;
    }
}
