package com.example.diappetes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Welcome extends AppCompatActivity {
    Button loginButton, signupButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Toast.makeText(Welcome.this, "Automatically Logging In", Toast.LENGTH_SHORT).show();
            final Global global = (Global) getApplicationContext();
            global.setUID(auth.getCurrentUser().getUid());
            setNHSNum(global.getUID(), global);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        loginButton = findViewById(R.id.LoginBtn);
        signupButton = findViewById(R.id.SignUpBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser()==null) startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp1.class));
            }
        });
    }


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
