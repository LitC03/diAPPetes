package com.example.diappetes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BSHistoryAdapter extends RecyclerView.Adapter<BSHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<BSLogValues> logArray;


    public BSHistoryAdapter(Context context, ArrayList<BSLogValues> logArray) {
        this.context = context;
        this.logArray = logArray;
    }




    @NonNull
    @Override
    public BSHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.bs_recycler,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BSHistoryAdapter.MyViewHolder holder, int position) {

        BSLogValues log = logArray.get(position);

        holder.Time.setText((log.Time).toString());
        holder.BS.setText((log.BS).toString());
        holder.EatenIn2h.setText(log.EatenIn2h.toString());


    }

    @Override
    public int getItemCount() {
        return logArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Time, BS, EatenIn2h;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.timeView);
            BS = itemView.findViewById(R.id.bloodSugarView);
            EatenIn2h = itemView.findViewById(R.id.eatBoolView);

        }
    }
}
