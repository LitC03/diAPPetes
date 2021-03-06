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

public class MedHistoryAdapter extends RecyclerView.Adapter<MedHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<MedLogValues> logArray;

    //Constructor
    public MedHistoryAdapter(Context context, ArrayList<MedLogValues> logArray) {
        this.context = context;
        this.logArray = logArray;
    }




    @NonNull
    @Override
    public MedHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Fetch view from med_recycler
        View v = LayoutInflater.from(context).inflate(R.layout.med_recycler,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedHistoryAdapter.MyViewHolder holder, int position) {

        MedLogValues log = logArray.get(position);
        //Set all the holders
        holder.Time.setText((log.Time).toString());
        holder.Dose.setText(log.Dose);
        holder.MedType.setText(log.MedType);


    }

    @Override
    public int getItemCount() {
        return logArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //Initialise textview
        TextView MedType, Dose, Time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Find correct textviews in recycler
            Time = itemView.findViewById(R.id.timeView);
            MedType = itemView.findViewById(R.id.medTypeView);
            Dose = itemView.findViewById(R.id.doseView);

        }
    }
}