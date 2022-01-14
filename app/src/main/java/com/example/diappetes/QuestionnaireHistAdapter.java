package com.example.diappetes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class QuestionnaireHistAdapter extends RecyclerView.Adapter<QuestionnaireHistAdapter.MyViewHolder> {

    Context context;
    ArrayList<QuestionnaireClassValues> logArray;


    public QuestionnaireHistAdapter(Context context, ArrayList<QuestionnaireClassValues> logArray) {
        this.context = context;
        this.logArray = logArray;
    }




    @NonNull
    @Override
    public QuestionnaireHistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.que_recycler,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionnaireHistAdapter.MyViewHolder holder, int position) {

        QuestionnaireClassValues log = logArray.get(position);

        holder.Time.setText((log.Time).toString());
        holder.ExtensiveHunger.setText(String.valueOf(log.ExtensiveHunger));
        holder.ExtensiveThirst.setText(String.valueOf(log.ExtensiveThirst));
        holder.ExtensiveUrination.setText(String.valueOf(log.ExtensiveUrination));
        holder.WeightLoss.setText(String.valueOf(log.WeightLoss));
        holder.TinglingSensation.setText(String.valueOf(log.TinglingSensation));
        holder.VisionChanges.setText(String.valueOf(log.VisionChanges));
        holder.NotesOnSymptoms.setText(log.NotesOnSymptoms);


    }

    @Override
    public int getItemCount() {
        return logArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ExtensiveHunger, ExtensiveThirst, ExtensiveUrination, Time;
        TextView TinglingSensation, VisionChanges, WeightLoss, NotesOnSymptoms;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.timeView);
            ExtensiveHunger = itemView.findViewById(R.id.hungerView);
            ExtensiveThirst = itemView.findViewById(R.id.thirstView);
            ExtensiveUrination = itemView.findViewById(R.id.urinationView);
            TinglingSensation = itemView.findViewById(R.id.tinglingView);
            VisionChanges = itemView.findViewById(R.id.visionView);
            WeightLoss = itemView.findViewById(R.id.weightView);
            NotesOnSymptoms = itemView.findViewById(R.id.otherView);

        }
    }
}