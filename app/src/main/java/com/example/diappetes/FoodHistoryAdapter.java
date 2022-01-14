package com.example.diappetes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodHistoryAdapter extends RecyclerView.Adapter<FoodHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<FoodLogValues> logArray;


    public FoodHistoryAdapter(Context context, ArrayList<FoodLogValues> logArray) {
        this.context = context;
        this.logArray = logArray;
    }




    @NonNull
    @Override
    public FoodHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.food_recycler,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHistoryAdapter.MyViewHolder holder, int position) {

        FoodLogValues log = logArray.get(position);

        holder.Time.setText((log.Time).toString());
        holder.Calories.setText(log.Calories);
        holder.Carbs.setText(log.Carbs);
        holder.Meal.setText(log.Meal);
        holder.Sugars.setText(log.Sugars);



    }

    @Override
    public int getItemCount() {
        return logArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Calories, Time, Carbs, Meal, Sugars;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.timeView);
            Calories = itemView.findViewById(R.id.calView);
            Carbs = itemView.findViewById(R.id.carbView);
            Meal = itemView.findViewById(R.id.mealView);
            Sugars = itemView.findViewById(R.id.sugarView);

        }
    }
}