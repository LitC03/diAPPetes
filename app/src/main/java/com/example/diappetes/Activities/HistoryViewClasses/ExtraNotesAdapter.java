package com.example.diappetes.Activities.HistoryViewClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diappetes.R;

import java.util.ArrayList;

public class ExtraNotesAdapter extends RecyclerView.Adapter<ExtraNotesAdapter.MyViewHolder> {

    Context context;
    ArrayList<ExtraNotesValues> logArray;

    //Constructor
    public ExtraNotesAdapter(Context context, ArrayList<ExtraNotesValues> logArray) {
        this.context = context;
        this.logArray = logArray;
    }




    @NonNull
    @Override
    public ExtraNotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Fetch view from exercise_recycler
        View v = LayoutInflater.from(context).inflate(R.layout.extranotes_recycler,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraNotesAdapter.MyViewHolder holder, int position) {

        ExtraNotesValues log = logArray.get(position);
        //Set all the holders
        holder.Time.setText((log.Time).toString());
        holder.Notes.setText(log.Notes);


    }

    @Override
    public int getItemCount() {
        return logArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //Initialise textview
        TextView Time, Notes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Find correct textviews in recycler
            Time = itemView.findViewById(R.id.timeView);
            Notes = itemView.findViewById(R.id.noteView);

        }
    }
}