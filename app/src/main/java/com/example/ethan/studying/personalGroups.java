package com.example.ethan.studying;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class personalGroups extends AppCompatActivity {
    ListView listViewGroups;
    List<Groups> groups;
    private DatabaseReference groupsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_group_list);

        listViewGroups = findViewById(R.id.ListViewGroups);
        groups = new ArrayList<>();
        groupsDB = FirebaseDatabase.getInstance().getReference("Groups");

        listViewGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Groups group = groups.get(i);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupID()).child("members").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        try {
                            if(dataSnapshot.getValue() == null) {
                                showUpdateDialog(group.getGroupName(), group.getGroupID(), false);
                            } else {
                                showUpdateDialog(group.getGroupName(), group.getGroupID(), true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });

    }


    private void showUpdateDialog(String name,final String id, boolean isMember) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflator = getLayoutInflater();
        final View dialogView = inflator.inflate(R.layout.update_member_type, null);
        dialogBuilder.setView(dialogView);

        final Button yesBtn = dialogView.findViewById(R.id.yesBtn);
        final Button noBtn = dialogView.findViewById(R.id.noBtn);
        final TextView question = dialogView.findViewById(R.id.memberQuestion);
        if (isMember) {
            yesBtn.setVisibility(View.INVISIBLE);
            noBtn.setVisibility(View.INVISIBLE);
            question.setText("You are already a member!");
        } else {
            yesBtn.setVisibility(View.VISIBLE);
            noBtn.setVisibility(View.VISIBLE);
            question.setText("Do you want to join the group?");
        }


        dialogBuilder.setTitle("Opening Group " + name);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Groups").child(id).child("members").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("Group Member");
                alertDialog.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

        groupsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups.clear();

                for(DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Groups group = groupSnapshot.getValue(Groups.class);
                    groups.add(group);
                }
                groupList groupListAdapter = new groupList(personalGroups.this, groups);
                listViewGroups.setAdapter(groupListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}