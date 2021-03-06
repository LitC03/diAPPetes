package com.example.diappetes.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diappetes.Global;
import com.example.diappetes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Welcome extends AppCompatActivity {

    // Create fields to associate ui components with
    Button loginButton, signupButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        auth = FirebaseAuth.getInstance();

        // Checks if the user wsa previously logged in on the device
        // If yes they get logged in automatically and sent to Main menu
        if (auth.getCurrentUser() != null) {
            Toast.makeText(Welcome.this, "Automatically Logging In", Toast.LENGTH_SHORT).show();
            final Global global = (Global) getApplicationContext();
            global.setUID(auth.getCurrentUser().getUid());
            setNHSNum(global.getUID(), global);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        // Associating the variables with ui components
        loginButton = findViewById(R.id.LoginBtn);
        signupButton = findViewById(R.id.SignUpBtn);


        // Internet Error in case of connection failure
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.internet_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().getAttributes().windowAnimations=
                    android.R.style.Animation_Dialog;
            Button TryAgain = dialog.findViewById(R.id.errorBtn);
            TryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });
            dialog.show();
        }

        // Button opens log in page
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser()==null) startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        // Button opens sign up page
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp1.class));
            }
        });
    }

    // NHS number to allow access to patient's document in the database
    private void setNHSNum(String UID, Global global) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Patients").whereEqualTo("UID", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().getDocuments().isEmpty()) {
                            Log.d("DBACCESS", "No documents found");
                        } else {
                            Log.d("DBACCESS", task.getResult().getDocuments().get(0).getId());
                            global.setNhsNum(task.getResult().getDocuments().get(0).getId());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });
    }
}
