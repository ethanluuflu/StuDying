package com.example.ethan.studying;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountFragment extends Fragment implements View.OnClickListener{

    private TextView viewUser, viewEmail, viewAge, viewRating;
    private Button changePW, updateEmail, signOut;
    private Spinner dayMenu,monthMenu,yearMenu;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDB = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        viewUser = view.findViewById(R.id.tvUsername);
        viewEmail = view.findViewById(R.id.tvEmail);
        viewAge = view.findViewById(R.id.tvAge);
        viewRating = view.findViewById(R.id.tvRating);

        changePW = view.findViewById(R.id.changePWBtn);
        changePW.setOnClickListener(this);

        updateEmail = view.findViewById(R.id.updateEmail);
        updateEmail.setOnClickListener(this);

        signOut = view.findViewById(R.id.signOutBtn);
        signOut.setOnClickListener(this);

        dayMenu = view.findViewById(R.id.day_menu);
        monthMenu = view.findViewById(R.id.month_menu);
        yearMenu = view.findViewById(R.id.year_menu);

        ArrayList<String> days = new ArrayList<String>();
        days.add("Select Day");
        for(int i = 1;i< 32;i++) {
            days.add(Integer.toString(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_dropdown_item,days);
        dayMenu.setAdapter(dayAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_dropdown_item, R.array.month_array);
        monthMenu.setAdapter(monthAdapter);

        ArrayList<String> years = new ArrayList<String>();
        years.add("Select Year");
        for(int i = 1960;i< 2020;i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_dropdown_item,years);
        yearMenu.setAdapter(yearAdapter);
        return view;
    }
    //Use onStart to update
    //since onStart is called each time, activity is active
    @Override
    public void onStart() {
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
            Intent i = new Intent(getActivity(),UpdateEmailActivity.class);
            startActivity(i);
        }
        if(view == changePW){
            Intent i = new Intent(getActivity(), ChangePWActivity.class);
            startActivity(i);
        }
        if (view == signOut) {
            Intent i = new Intent(getActivity(), LoginActivity.class);
            mAuth.signOut();
            startActivity(i);
        }
    }
}
