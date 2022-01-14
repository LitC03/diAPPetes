package com.example.diappetes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExtraNotesAdapter extends RecyclerView.Adapter<ExtraNotesAdapter.MyViewHolder> {

    Context context;
    ArrayList<ExtraNotesValues> logArray;


    public ExtraNotesAdapter(Context context, ArrayList<ExtraNotesValues> logArray) {
        this.context = context;
        this.logArray = logArray;
    }




    @NonNull
    @Override
    public ExtraNotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.extranotes_recycler,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraNotesAdapter.MyViewHolder holder, int position) {

        ExtraNotesValues log = logArray.get(position);

        holder.Time.setText((log.Time).toString());
        holder.Notes.setText(log.Notes);


    }

    @Override
    public int getItemCount() {
        return logArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Time, Notes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.timeView);
            Notes = itemView.findViewById(R.id.noteView);

        }
    }
}