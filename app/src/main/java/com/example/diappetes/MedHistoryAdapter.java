package com.example.diappetes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedHistoryAdapter extends RecyclerView.Adapter<MedHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<MedLogClass> logArray;


    public MedHistoryAdapter(Context context, ArrayList<MedLogClass> logArray) {
        this.context = context;
        this.logArray = logArray;
    }




    @NonNull
    @Override
    public MedHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.med_recycler,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedHistoryAdapter.MyViewHolder holder, int position) {

        MedLogClass log = logArray.get(position);

        holder.Time.setText((log.Time).toString());
        holder.Dose.setText(log.Dose);
        holder.MedType.setText(log.MedType);


    }

    @Override
    public int getItemCount() {
        return logArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView MedType, Dose, Time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.timeView);
            MedType = itemView.findViewById(R.id.medTypeView);
            Dose = itemView.findViewById(R.id.doseView);

        }
    }
}