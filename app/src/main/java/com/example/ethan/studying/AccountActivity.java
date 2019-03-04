package com.example.ethan.studying;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView viewUser, viewEmail, viewAge, viewRating;
    private Button changePW, updateEmail, signOut;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDB = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        viewUser = findViewById(R.id.tvUsername);
        viewEmail = findViewById(R.id.tvEmail);
        viewAge = findViewById(R.id.tvAge);
        viewRating = findViewById(R.id.tvRating);

        changePW = findViewById(R.id.changePWBtn);
        changePW.setOnClickListener(this);

        updateEmail = findViewById(R.id.updateEmail);
        updateEmail.setOnClickListener(this);

        signOut = findViewById(R.id.signOutBtn);
        signOut.setOnClickListener(this);

    }

    //Use onStart to update
    //since onStart is called each time, activity is active
    @Override
    protected void onStart() {
        super.onStart();

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Grab user object from database
                User currUser = dataSnapshot.getValue(User.class);
                viewUser.setText("Username: " + currUser.getUser());
                viewEmail.setText("Email Address: " + currUser.getEmail());

                if (dataSnapshot.hasChild("age")) {
                    viewAge.setText("Age: " + currUser.getAge());
                } else {
                    viewAge.setText("Age: NULL");
                }

                if (dataSnapshot.hasChild("rating")) {
                    viewRating.setText("Average Rating: " + currUser.getRating());
                } else {
                    viewRating.setText("Average Rating: N/A");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view){

        if (view == updateEmail) {
            Intent i = new Intent(AccountActivity.this,UpdateEmailActivity.class);
            startActivity(i);
        }
        if(view == changePW){
            Intent i = new Intent(AccountActivity.this, ChangePWActivity.class);
            startActivity(i);
        }
        if (view == signOut) {
            Intent i = new Intent(AccountActivity.this, LoginActivity.class);
            finish();
            mAuth.signOut();
            startActivity(i);
        }
    }
}
