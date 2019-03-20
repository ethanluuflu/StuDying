package com.example.ethan.studying;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment implements View.OnClickListener{
    private EditText etGroup;
    private Button createGroupBtn;
    private Button myGroupBtn;


    private DatabaseReference groupsDB;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = (View) inflater.inflate(R.layout.fragment_group, container, false);
        etGroup = view.findViewById(R.id.etGroupName);
        createGroupBtn = view.findViewById(R.id.createGroupBtn);
        createGroupBtn.setOnClickListener(this);
        myGroupBtn = view.findViewById(R.id.myGroupBtn);
        myGroupBtn.setOnClickListener(this);
        groupsDB = FirebaseDatabase.getInstance().getReference("Groups");
        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == createGroupBtn) {
            String groupName = etGroup.getText().toString().trim();
            String id = groupsDB.push().getKey();
            Groups group = new Groups(groupName,id,mAuth.getCurrentUser().getUid());

            groupsDB.child(id).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),"Group successfully created.",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Group failed to be created", Toast.LENGTH_LONG).show();
                    }
                }
            });

            groupsDB.child(id).child("members").child(mAuth.getCurrentUser().getUid()).setValue("Group Leader");

        }
        if (view == myGroupBtn) {
            Intent i = new Intent(getActivity(),tempGroupList.class);
            startActivity(i);
        }
    }
}
