package com.example.diappetes;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Graph extends AppCompatActivity {
    Button backButton,exportButton;
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
        exportButton = findViewById(R.id.ExportBtn);

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
                            int collectionSize = task.getResult().size();
                            if (collectionSize==0) {
                                displayProblemDialog();
                            }
                            else{
                            //Inspect every blood sugar entry
                                ArrayList<String> csvListDate = new ArrayList<String>();
                                ArrayList <String> csvListBS = new ArrayList<String>();

                                for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                    //Get x and y coordinates from firestore
                                    double y = documentSnapshot.getDouble("BS");
                                    String yStr = documentSnapshot.getDouble("BS").toString();

                                    Date x = documentSnapshot.getTimestamp("Time").toDate();
                                    String xStr = documentSnapshot.getTimestamp("Time").toString();

                                    Log.d("DB_GRAPH", "added value " + y);
                                    Log.d("DB_GRAPH", "added value " + x);

                                    csvListDate.add(xStr);
                                    csvListBS.add(yStr);


                                    //Add points to series
                                    addPointsSeries(series, x,y);

                                    //Add series to graph
                                    graph.addSeries(series);

                                    //Format graph
                                    formatGraph(graph,collectionSize);


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

                                    //Export data to CSV file
                                    exportButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            exportCVS(csvListDate,csvListBS);
                                        }
                                    });
                                }
                            }


                        } else {
                            Log.d("DB_GRAPH", "Error getting documents: ", task.getException());
                        }
                    }
                });





    }

    private void exportCVS(List<String> csvListDate, List<String> csvListBS) {
        // Declare path of document
//        if(ContextCompat.checkSelfPermission(Graph.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(Graph.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
//        }
//        else {
//
            FileOutputStream out = null;
            try {
                out = openFileOutput("CVSFile.csv",MODE_PRIVATE);
                out.write(("Date,Blood Sugar Level\n").getBytes());
                for (int i = 0; i < csvListBS.size(); i++) {
                    String dateStr = csvListDate.get(i);
                    String BSStr = csvListBS.get(i);
                    out.write((dateStr + "," + BSStr + "\n").getBytes());
                }
                Toast.makeText(Graph.this,"Saved to "+getFilesDir()+"/CVSFile.csv", Toast.LENGTH_LONG).show();
//
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (out != null)
                {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
//        }
    }


    private void displayProblemDialog(){
        //Prompt a Dialog Box to delete account
        AlertDialog.Builder builder = new AlertDialog.Builder(Graph.this);

        //Ask if user wants to delete account

        builder.setTitle("Not enough data!");
        builder.setMessage("It appears like you have not entered any sugar level entries in our " +
                "database. Please add your blood sugar levels and try again.");


        // Dialog Box will NOT disappear if user clicks outside of it
        builder.setCancelable(false);

        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), History.class));
            }
        });

        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }


    private void addPointsSeries(LineGraphSeries<DataPoint> series, Date x, double y) {

        //Append data point to series
        series.appendData(new DataPoint(x, y), true, 500);

        //Format series
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15);
        series.setColor(Color.WHITE);
        series.setThickness(10);
    }

    private void formatGraph(GraphView graph, int collectionSize) {
        Viewport viewPort = graph.getViewport();
        GridLabelRenderer labelRend = graph.getGridLabelRenderer();

        if (collectionSize != 1){
            //Dispay full range of plot
            viewPort.setMaxX(series.getHighestValueX());
            viewPort.setMinX(series.getLowestValueX());
        }
        else{
            //If only one entry is found in the database, center it
            viewPort.setMaxX(series.getHighestValueX()+1000);
            viewPort.setMinX(series.getLowestValueX()-1000);
        }
        viewPort.setMaxY(series.getHighestValueY()+2);
        viewPort.setMinY(series.getLowestValueY()-2);
        viewPort.setXAxisBoundsManual(true);
        labelRend.setNumHorizontalLabels(3);
        labelRend.setVerticalLabelsColor(Color.WHITE);
        labelRend.setHorizontalLabelsColor(Color.WHITE);
        labelRend.setPadding(50);
        labelRend.setVerticalAxisTitle("Blood Sugar levels (mmol/L)");
        labelRend.setVerticalAxisTitleColor(Color.WHITE);
        labelRend.setTextSize(30);
        labelRend.reloadStyles();

        //Correct x axis so that it displays dates
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
        viewPort.setBackgroundColor(Color.argb(20, 0, 0, 0));

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
    }
}

