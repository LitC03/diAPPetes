package com.example.diappetes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Graph extends AppCompatActivity {
    Button backButton;
    GraphView graph;
    TextView xView, yView;
    FirebaseFirestore db;
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        final Global global = (Global) getApplicationContext();
        db = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.BackBtn);
        graph = findViewById(R.id.idGraphView);
        xView = findViewById(R.id.xValue);
        yView = findViewById(R.id.yValue);

        //Create series where data point will be added
        series = new LineGraphSeries<>();

        //Button to go back to "History" activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), History.class));
            }
        });

        //Get patient's blood sugar entries from oldest to newest
        db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar")
                .orderBy("Time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            //Inspect every blood sugar entry
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                //Get x and y coordinates from firestore
                                double y = documentSnapshot.getDouble("BS");
                                Log.d("DB_GRAPH", "added value " + y);
                                Date x = documentSnapshot.getTimestamp("Time").toDate();
                                Log.d("DB_GRAPH", "added value " + x);

                                //Append data point to series
                                series.appendData(new DataPoint(x, y), true, 500);

                                //Format series
                                series.setDrawDataPoints(true);
                                series.setDataPointsRadius(15);
                                series.setColor(Color.WHITE);
                                series.setThickness(10);

                                //Add series to graph
                                graph.addSeries(series);

                                //Format graph
                                Viewport viewPort = graph.getViewport();
                                GridLabelRenderer labelRend = graph.getGridLabelRenderer();

                                viewPort.setMaxX(series.getHighestValueX());
                                viewPort.setMinX(series.getLowestValueX());
                                viewPort.setMaxY(series.getHighestValueY()+2);
                                viewPort.setXAxisBoundsManual(true);
                                labelRend.setNumHorizontalLabels(3);
                                labelRend.setVerticalLabelsColor(Color.WHITE);
                                labelRend.setHorizontalLabelsColor(Color.WHITE);
                                labelRend.setPadding(50);
                                labelRend.setVerticalAxisTitle("Blood Sugar levels (mmol/L)");
                                labelRend.setVerticalAxisTitleColor(Color.WHITE);
                                labelRend.setTextSize(30);

                                //Correct x axis so that it displays dates
                                graph.getGridLabelRenderer().reloadStyles();
                                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
                                graph.getViewport().setBackgroundColor(Color.argb(20, 0, 0, 0));

                                //Set date label formatter
                                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                                    @Override
                                    public String formatLabel(double value, boolean isValueX) {
                                        if (isValueX) {
                                            return sdf.format(new Date((long) value));
                                        } else {
                                            return super.formatLabel(value, isValueX);
                                        }
                                    }

                                });


                                //When data point is clicked, TextViews set to corresponding value
                                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                                    @Override
                                    public void onTap(Series series, DataPointInterface dataPoint) {
                                        double xDouble = dataPoint.getX();
                                        Date xDate = new Date((long) xDouble);
                                        String xStr = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(xDate);
                                        xView.setText(xStr);

                                        String yStr = Double.toString(dataPoint.getY());
                                        yView.setText(yStr);

                                    }
                                });
                            }


                        } else {
                            Log.d("DB_GRAPH", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

