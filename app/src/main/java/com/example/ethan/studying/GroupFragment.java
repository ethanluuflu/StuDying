package com.example.ethan.studying;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

/**
 * Fragment for Creating Groups
 */
public class GroupFragment extends Fragment implements View.OnClickListener{
    private EditText etGroupName, etGroupSubject, etGroupDescription;
    private Button createGroupBtn;

    private DatabaseReference groupsDB;
    private FirebaseAuth mAuth;

    //Creates the variables and attaches them to their designated layout counterparts
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = (View) inflater.inflate(R.layout.fragment_group, container, false);
        etGroupName = view.findViewById(R.id.etGroupName);
        etGroupSubject = view.findViewById(R.id.etGroupSubject);
        etGroupDescription = view.findViewById(R.id.etGroupDescription);
        createGroupBtn = view.findViewById(R.id.createGroupBtn);
        createGroupBtn.setOnClickListener(this);
        groupsDB = FirebaseDatabase.getInstance().getReference("Groups");
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onClick(View view) {
        //Creates the post and stores the data into Firebase Database
        if (view == createGroupBtn) {
            String groupName = etGroupName.getText().toString().trim();
            String groupSubject = etGroupSubject.getText().toString().trim();
            String groupDescription = etGroupDescription.getText().toString().trim();

            if(TextUtils.isEmpty(groupName) || TextUtils.isEmpty(groupSubject) || TextUtils.isEmpty(groupDescription)){
                Toast.makeText(getActivity(),"Please fill in all required fields",Toast.LENGTH_SHORT).show();            }
            else {
                String id = groupsDB.push().getKey();
                Groups group = new Groups(id,groupName,groupSubject,groupDescription);

                //Uploads new group object into Firebase
                groupsDB.child(id).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Group successfully created",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Group failed to be created", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                groupsDB.child(id).child("members").child(mAuth.getCurrentUser().getUid()).setValue("Group Leader");
            }
        }
    }
}
