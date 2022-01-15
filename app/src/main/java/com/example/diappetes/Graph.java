package com.example.diappetes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Graph extends AppCompatActivity {
    Button backButton,exportButton;
    private GraphView graph;
    TextView xView, yView;
    FirebaseFirestore db;
    private LineGraphSeries<DataPoint> series;
    private ArrayList<String> csvListDate;
    private ArrayList <String> csvListBS;
    private ArrayList <String> cvsListEaten;
    private ArrayList <String> cvsListTime;

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

                                //Initialise lists to use in csv exporting
                                csvListBS = new ArrayList<String>();
                                csvListDate = new ArrayList<String>();
                                cvsListTime = new ArrayList<String>();
                                cvsListEaten = new ArrayList<String>();


                                //Inspect every blood sugar entry
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                    //Get x and y coordinates from firestore
                                    double y = documentSnapshot.getDouble("BS");
                                    String yStr = documentSnapshot.getDouble("BS").toString();

                                    Date x = documentSnapshot.getTimestamp("Time").toDate();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                    String dateStr = dateFormat.format(x);
                                    String timeStr = timeFormat.format(x);


                                    //Get EatIn2h string to use in csv export
                                    String eaten2hStr = documentSnapshot.getString("EatenIn2h");

                                    Log.d("DB_GRAPH", "added value " + y);
                                    Log.d("DB_GRAPH", "added value " + x);

                                    csvListDate.add(dateStr);
                                    csvListBS.add(yStr);
                                    cvsListTime.add(timeStr);
                                    cvsListEaten.add(eaten2hStr);

                                    //Append data point to series
                                    series.appendData(new DataPoint(x, y), true, 500);

                                }

                                //format series
                                formatSeries(series);

                                //Add series to graph
                                graph.addSeries(series);

                                //Format graph
                                formatGraph(graph,collectionSize);
                            }

                        } else {
                            Log.d("DB_GRAPH", "Error getting documents: ", task.getException());
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

        //Export data to CSV file
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportCVS(csvListDate,csvListBS,cvsListEaten,cvsListTime);
            }
        });

    }

    private void exportCVS(List<String> csvListDate, List<String> csvListBS, ArrayList<String> cvsListEaten, ArrayList<String> cvsListTime) {

        //Create new CSV file and save in device
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE,"CSVExport");
        startActivityForResult(intent,1);
    }


    @Override protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode==1){
            if(resultCode==RESULT_OK){
                Uri uri = data.getData();
                try {
                    String dateStr,BSStr,eatenStr,timeStr;
                    //Write csv file
                    OutputStream out = getContentResolver().openOutputStream(uri);
                    out.write(("Date,Time,Blood Sugar (mmol/l), Eaten in last 2h\n").getBytes());
                    Log.d("DB_GRAPH","Size of list: "+csvListBS.size());
                    for (int i = 0; i < csvListBS.size(); i++) {

                        dateStr = csvListDate.get(i);
                        timeStr = cvsListTime.get(i);
                        BSStr = csvListBS.get(i);
                        eatenStr = cvsListEaten.get(i);


                        out.write((dateStr + "," + timeStr+ "," + BSStr +","+eatenStr + "\n").getBytes());
                    }
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayProblemDialog(){
        //Prompt a Dialog Box
        AlertDialog.Builder builder = new AlertDialog.Builder(Graph.this);

        //Inform user there isn't any data to graph
        builder.setTitle("Not enough data!");
        builder.setMessage("It appears like you have not entered any blood sugar entries in our " +
                "database. Please add your blood sugar levels and try again.");


        // Dialog Box will NOT disappear if user clicks outside of it
        builder.setCancelable(false);

        //Go back to history menu
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


    private void formatSeries(LineGraphSeries<DataPoint> series) {
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
        viewPort.setMinY(0);
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

