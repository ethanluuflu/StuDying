package com.example.ethan.studying;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Group Member List class for MemberList button of the GroupForum
 */
public class GroupMemberList extends AppCompatActivity {

    private RecyclerView mMemberList;
    private ImageButton mDelete;
    private ImageButton mAdd;
    private MemberAdapter adapter;
    private ArrayList<String> memberIDList;
    private String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_list);

        //Changes the toolbar title to designate its page
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Group Members");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FFDF00"));
        toolbar.setTitleTextColor(Color.parseColor("#303030"));
        groupID = getIntent().getStringExtra("Group_ID");

        memberIDList = new ArrayList<>();

        //Initiates the variables and attach them to the layout components
        mMemberList = findViewById(R.id.membersView);
        mDelete = findViewById(R.id.deleteMember);
        mAdd = findViewById(R.id.addFriend);

        mMemberList.setHasFixedSize(true);
        mMemberList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MemberAdapter(this, memberIDList,groupID);
        mMemberList.setAdapter(adapter);

        //Add functionality to the buttons in member list
        adapter.setOnItemClickListener(new MemberAdapter.OnItemClickListener() {

            //Delete button allows group leader to remove members from group
            @Override
            public void onDeleteClick(int position) {
                FirebaseDatabase.getInstance().getReference("Groups").child(groupID).child("members").child(memberIDList.get(position)).removeValue();
                memberIDList.remove(position);
                adapter.notifyItemRemoved(position);
            }

            //Add button to add members as a friend
            //Friend function in Progress still.
            //Layout for friends is here
            @Override
            public void onAddClick(int position) {
                FirebaseDatabase.getInstance().getReference("Users").child(memberIDList.get(position)).child("user").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(getApplicationContext(),"You are now friends with " + dataSnapshot.getValue(String.class), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    //Initiates the list of members by pulling data from Firebase Database
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference("Groups").child(groupID).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memberIDList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String memberID = child.getKey();
                    memberIDList.add(memberID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
