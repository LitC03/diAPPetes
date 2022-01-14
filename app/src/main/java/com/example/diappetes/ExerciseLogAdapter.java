package com.example.diappetes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseLogAdapter extends RecyclerView.Adapter<ExerciseLogAdapter.MyViewHolder> {

    Context context;
    ArrayList<ExerciseLogValues> logArray;


    public ExerciseLogAdapter(Context context, ArrayList<ExerciseLogValues> logArray) {
        this.context = context;
        this.logArray = logArray;
    }




    @NonNull
    @Override
    public ExerciseLogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.exercise_recycler,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseLogAdapter.MyViewHolder holder, int position) {

        ExerciseLogValues log = logArray.get(position);

        holder.Time.setText((log.Time).toString());
        holder.Duration.setText(log.Duration);
        holder.ExerciseType.setText(log.ExerciseType);


    }

    @Override
    public int getItemCount() {
        return logArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Time, Duration, ExerciseType;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.timeView);
            ExerciseType = itemView.findViewById(R.id.exerciseTypeView);
            Duration = itemView.findViewById(R.id.durationView);

        }
    }
}