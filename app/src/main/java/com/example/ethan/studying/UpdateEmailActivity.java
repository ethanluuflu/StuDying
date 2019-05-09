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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//Update email activity that handles the changing of emails associated with the logged in account
public class UpdateEmailActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText oldEmail,newEmail,password;
    private Button update;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userDB;

    //Attachs variables to the layout components
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDB = FirebaseDatabase.getInstance().getReference("Users");
        oldEmail = findViewById(R.id.etOldEmail);
        newEmail = findViewById(R.id.etNewEmail);
        password = findViewById(R.id.etPassword);

        update = findViewById(R.id.updateBtn);
        update.setOnClickListener(this);
    }

    //When the user clicks the update button
    //Checks if the credentials are correct
    //If so, authenticate the operation and change the email associated with the account
    @Override
    public void onClick(View view) {
        if(view == update) {
            AuthCredential credential = EmailAuthProvider.getCredential(oldEmail.getText().toString().trim(), password.getText().toString().trim());
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(newEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateEmail(newEmail.getText().toString().trim());
                                    Toast.makeText(getApplicationContext(), "Success. Your email has been changed.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Change email attempt failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //update email in Firebase Database as updating string
    private void updateEmail(String email) {
        userDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("email").setValue(email);
    }
}