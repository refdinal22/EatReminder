package com.example.gita.eatreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.gita.eatreminder.model.Reminder;
import com.example.gita.eatreminder.receiver.AlarmReceiver;
import com.example.gita.eatreminder.sqllite.ReminderDao;

import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity implements AddReminder.NoticeDialogListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Reminder> listReminder;
    private ReminderDao datasource; //sql lite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //sql lite
        datasource = new ReminderDao(this);
        datasource.open();

        //floating action
        FloatingActionButton add = findViewById(R.id.FAB);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //memanggil fragment
                AddReminder addList = AddReminder.newInstance("AddReminder");
                addList.show(getSupportFragmentManager(), "AddReminder");
            }
        });

        listReminder = new ArrayList<>();
        listReminder = datasource.getAllReminder();

        //RecycleView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ReminderAdapter(this,listReminder);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment, Reminder reminder) {
        int reminderId;
        long alarmStartTime;

        Reminder newReminder = datasource.createReminder(reminder);
        listReminder.add(newReminder);

        reminderId = newReminder.getId();
        alarmStartTime = newReminder.getStartTime();

        Intent intent = new Intent(ReminderActivity.this, AlarmReceiver.class);
        intent.putExtra("reminderId", reminderId);
        intent.putExtra("type", newReminder.getType());
        intent.putExtra("note", newReminder.getNote());

        // getBroadcast(context, requestCode, intent, flags)
        PendingIntent alarmIntent = PendingIntent.getBroadcast(ReminderActivity.this, reminderId,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmReminder = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmReminder.setExact(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
        alarmReminder.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime, AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        datasource.close();
    }

}
