package com.example.ethan.studying;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Member;
import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity{
    private TextView mMembersLeft;
    private TextView mMemberName;
    private EditText mComment;
    private RatingBar mRating;
    private Button mSubmit;
    private ArrayList<String> userIDList;
    private String groupID;


    private int MembersLeft = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //put getActivity in front for fragment.

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Rating Group Members");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FFDF00"));
        toolbar.setTitleTextColor(Color.parseColor("#303030"));

        userIDList = new ArrayList<>();
        groupID = getIntent().getStringExtra("Group_ID");

        mMembersLeft = findViewById(R.id.members_left);
        mMemberName = findViewById(R.id.membersTitle);
        mComment = findViewById(R.id.memberComment);
        mRating = findViewById(R.id.memberRating);
        mSubmit = findViewById(R.id.submitBtn);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRating.getRating() == 0F) {
                    Toast.makeText(RatingActivity.this, "Enter your rating.", Toast.LENGTH_LONG).show();
                } else {
                    MembersLeft--;
                    mMembersLeft.setText(Integer.toString(MembersLeft));
                    mRating.setRating(0F);
                    mComment.setText("");
                    if (MembersLeft != 0) {
                        FirebaseDatabase.getInstance().getReference("Users").child(userIDList.get(MembersLeft - 1)).child("user").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mMemberName.setText("Group Member: " + dataSnapshot.getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),"You rated all the members of the group.", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference("Groups").child(groupID).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MembersLeft = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String memberID = child.getKey();
                    if (!memberID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        userIDList.add(memberID);
                        MembersLeft++;
                        mMembersLeft.setText(Integer.toString(MembersLeft));
                        FirebaseDatabase.getInstance().getReference("Users").child(memberID).child("user").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mMemberName.setText("Group Member: " + dataSnapshot.getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                if (MembersLeft == 0) {
                    finish();
                    Toast.makeText(getApplicationContext(),"You need more members to rate.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
