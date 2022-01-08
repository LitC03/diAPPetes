package com.example.diappetes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

public class Graph extends AppCompatActivity {
    Button backButton;
    GraphView graph;
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

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), LogBook.class));
//            }
//        });


        series = new LineGraphSeries<>();
//
            db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d("DB_BLOODSUGAR", ""+task.getResult().size());
                        final int collectionSize = task.getResult().size();
                        for(int i=1;i<collectionSize+1;i++) {
                            final int finalI = i;
                            db.collection("Patients").document(global.getNhsNum())
                                .collection("BloodSugar").document("BS"+i)
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {



                                        double y = documentSnapshot.getDouble("BS");
                                        Log.d("DB_GRAPH", "added value "+ y);
                                        Date x = documentSnapshot.getTimestamp("Time").toDate();
                                        Log.d("DB_GRAPH", "added value "+ x);




                                        series.appendData(new DataPoint(x, y), true, 500);
                                        graph.addSeries(series);

                                        graph.getViewport().setMinX(graph.getViewport().getMinX(true));
                                        graph.getViewport().setMaxX(graph.getViewport().getMaxX(true));

//                                        graph.getViewport().setMaxX(series.getHighestValueX());
//                                        graph.getViewport().setMinX(series.getLowestValueX());
                                        graph.getViewport().setXAxisBoundsManual(true);
                                        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
                                        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
                                        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
                                        graph.getGridLabelRenderer().reloadStyles();
                                        graph.getGridLabelRenderer().setPadding(32);


                                        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                        graph.getViewport().setBackgroundColor(Color.GRAY);

                                        // set date label formatter
                                        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
                                        {
                                            @Override
                                            public String formatLabel(double value, boolean isValueX){
                                                if(isValueX) {
                                                    return sdf.format(new Date((long) value));
                                                }
                                                else {
                                                    return super.formatLabel(value, isValueX);
                                                }
                                            }

                                        });
                                         // only 4 because of the space

                                    }
                                });
                        }
                    }
                    else{
                        Log.d("DB_GRAPH", "Error getting documents: ", task.getException());
                    }
                }
            });

//        }


//        db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    Log.d("DB_BLOODSUGAR", ""+task.getResult().size());
//                    int collectionSize = task.getResult().size();
//                    for(int i=1;i<collectionSize+1;i++){
//                        final int finalI = i;
//                        final int finalI1 = i;
//                        db.collection("Patients").document(global.getNhsNum())
//                                .collection("BloodSugar").document("BS"+i)
//                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//
//
//                                        double y = documentSnapshot.getDouble("BS");
//                                        Log.d("DB_GRAPH", "added value "+ finalI1);
//                                        Date x = documentSnapshot.getTimestamp("Time").toDate();
//                                        Log.d("DB_GRAPH", "added value "+ x);
//
//                                        series.appendData(new DataPoint(finalI1, y), true, 500);
//                                    }
//                                });
//                    }
//                    graph.addSeries(series);
//                    graph.getViewport().setBackgroundColor(Color.WHITE);
//
//                    // set date label formatter
////                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(Graph.this));
////                    graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
//
//                    // set manual x bounds to have nice steps
////                    Calendar calendar = Calendar.getInstance();
////                    Date d1 = calendar.getTime();
////                    graph.getViewport().setMaxX(d1.getTime());
////                    graph.getViewport().setXAxisBoundsManual(true);
//
//                }
//                else{
//                    Log.d("DB_GRAPH", "Error getting documents: ", task.getException());
//                }
//            }
//        });
    }
}
