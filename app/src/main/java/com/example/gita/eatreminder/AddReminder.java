package com.example.gita.eatreminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gita.eatreminder.model.Reminder;

import java.util.Calendar;

public class AddReminder extends DialogFragment {
    private static final String TAG = "AddReminder";

    private EditText editText;
    private int currentPosition;
    private TimePicker timePicker;

    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (NoticeDialogListener) context;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment fragment, Reminder reminder);
    }

    static AddReminder newInstance(String title){
        AddReminder add = new AddReminder();
        Bundle args = new Bundle();
        args.putString("title", title);
        add.setArguments(args);
        return add;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_reminder, null);
        builder.setView(view);

        editText = view.findViewById(R.id.editText);
        timePicker = view.findViewById(R.id.timePicker);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String note = editText.getText().toString();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                // Create time.
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();

                if (!TextUtils.isEmpty(note)) {
                    Reminder newReminder = new Reminder("Food" ,note, alarmStartTime);
                    Toast.makeText(getActivity(), "Add New Device Success", Toast.LENGTH_SHORT).show();

                    listener.onDialogPositiveClick(AddReminder.this, newReminder);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}

