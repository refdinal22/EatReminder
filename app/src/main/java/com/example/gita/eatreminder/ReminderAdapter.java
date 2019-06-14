package com.example.gita.eatreminder;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gita.eatreminder.model.Reminder;
import com.example.gita.eatreminder.sqllite.ReminderDao;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private ArrayList<Reminder> listReminder;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text1;
        private TextView text2;
        private Button delete;

        public View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public ReminderAdapter(Context context,ArrayList<Reminder> listReminder) {
        this.context = context;
        this.listReminder = listReminder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.reminder, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.text1.setText(listReminder.get(i).getType());
        viewHolder.text2.setText(listReminder.get(i).getType());
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(i);
            }
        });
    }

    @Override
    public int getItemCount () {
        return listReminder.size();
    }

    private void delete(int position){
        ReminderDao datasource;
        datasource = new ReminderDao(context);

        //open database
        datasource.open();
        //memanggil method untuk delete
        datasource.deleteReminder(listReminder.get(position));
        //menutup database
        datasource.close();

        //delete di arraylist
        listReminder.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listReminder.size());
    }

}

