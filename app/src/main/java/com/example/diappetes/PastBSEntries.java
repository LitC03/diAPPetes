package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PastBSEntries extends AppCompatActivity {

    Button backButton;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseFirestore db;
    BSHistoryAdapter histAdapt;

    ArrayList<BSLogClass> logArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_history);

        recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backButton = (Button) findViewById(R.id.BacktoMainBtn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        logArray = new ArrayList<BSLogClass>();
        histAdapt = new BSHistoryAdapter(PastBSEntries.this,logArray);
        recyclerView.setAdapter(histAdapt);


        final Global global = (Global) getApplicationContext();

        Log.d("NHS:num", global.getNhsNum());

        db.collection("Patients").document(global.getNhsNum()).collection("BloodSugar").orderBy("Time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.e("Firestore error", e.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED ) {

                                logArray.add(dc.getDocument().toObject(BSLogClass.class));
                            }
                        }
                        histAdapt.notifyDataSetChanged();
                    }
                });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),History.class));
            }
        });

    }
}
