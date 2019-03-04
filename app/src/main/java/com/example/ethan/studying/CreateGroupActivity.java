package com.example.ethan.studying;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etGroup;
    private Button createGroupBtn;

    private DatabaseReference groupsDB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        etGroup = findViewById(R.id.etGroupName);
        createGroupBtn = findViewById(R.id.createGroupBtn);
        createGroupBtn.setOnClickListener(this);

        groupsDB = FirebaseDatabase.getInstance().getReference("Groups");
        mAuth = FirebaseAuth.getInstance();
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
                        Toast.makeText(getApplicationContext(),"Group successfully created.",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Group failed to be created", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}
