package com.example.diappetes;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
//
//        backButton = findViewById(R.id.backBtn);
        graph = findViewById(R.id.idGraphView);
        xView = findViewById(R.id.xValue);
        yView = findViewById(R.id.yValue);

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), LogBook.class));
//            }
//        });


        series = new LineGraphSeries<>();

        db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar")
                .orderBy("Time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList ideaArrayList = new ArrayList<>();
                            ideaArrayList.clear();

                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                double y = documentSnapshot.getDouble("BS");
                                Log.d("DB_GRAPH", "added value " + y);
                                Date x = documentSnapshot.getTimestamp("Time").toDate();
                                Log.d("DB_GRAPH", "added value " + x);
                                series.appendData(new DataPoint(x, y), true, 500);
                                series.setDrawDataPoints(true);
                                series.setDataPointsRadius(15);
                                series.setColor(Color.WHITE);
                                series.setThickness(10);

                                graph.addSeries(series);

                                graph.getViewport().setMaxX(series.getHighestValueX());
                                graph.getViewport().setMinX(series.getLowestValueX());
                                graph.getViewport().setXAxisBoundsManual(true);
                                graph.getGridLabelRenderer().setNumHorizontalLabels(3);
                                graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
                                graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);

                                graph.getGridLabelRenderer().setPadding(50);
                                graph.getGridLabelRenderer().setVerticalAxisTitle("Blood Sugar levels (mmol/L)");
                                graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
                                graph.getGridLabelRenderer().setTextSize(30);


                                graph.getGridLabelRenderer().reloadStyles();
                                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
                                graph.getViewport().setBackgroundColor(Color.argb(20, 0, 0, 0));

                                // set date label formatter
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

                                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                                    @Override
                                    public void onTap(Series series, DataPointInterface dataPoint) {
                                        double xDouble = dataPoint.getX();
                                        Date xDate = new Date((long) xDouble);
                                        String xStr = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(xDate);
                                        xView.setText(xStr);

                                        String yStr = Double.toString(dataPoint.getY());
                                        yView.setText(yStr);

//                                        final Toast toast = Toast.makeText(Graph.this, "Data Point pressed: ("+ xStr+" , "+dataPoint.getY()+")", Toast.LENGTH_LONG);
//                                        toast.show();
//                                        Handler handler = new Handler();
//                                        handler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                toast.cancel();
//                                            }
//                                        }, 2500);
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

