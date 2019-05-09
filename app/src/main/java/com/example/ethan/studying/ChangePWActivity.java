package com.example.ethan.studying;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

/**
 * Activity for Changing Password
 */
public class ChangePWActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText newPassword,oldPassword,email;
    private Button updatePW;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initiates the proper variables to their layout counterparts
        setContentView(R.layout.activity_change_pw);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = findViewById(R.id.etEmail);
        oldPassword = findViewById(R.id.etOldPW);
        newPassword = findViewById(R.id.etNewPW);
        updatePW = findViewById(R.id.changeBtn);
        mAuth = FirebaseAuth.getInstance();

        updatePW.setOnClickListener(this);
    }

    //Main method for changing password
    private void changePassword() {

        String newPW = newPassword.getText().toString().trim();

        //Text fields must not be empty, else send message
        if (TextUtils.isEmpty(newPW)) {
            //email is empty
            Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show();
            //stop function execution
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email.getText().toString().trim(), oldPassword.getText().toString().trim());

        //Firebase method to update the password associated with the username
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Success. Your password has been changed.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Change password attempt failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * When user clicks the button, execute changePassword method
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == updatePW) {
            changePassword();
        }
    }
}