package com.example.ethan.studying;


import android.content.Intent;
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

public class ForgotPWActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    private Button reset, cancel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);
        email = findViewById(R.id.etEmail);
        reset = findViewById(R.id.resetBtn);
        cancel = findViewById(R.id.cancelResetBtn);
        reset.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if (view == reset) {
            mAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Password Reset Email has been sent.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter a valid email", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        if (view == cancel) {
            finish();
            Intent i = new Intent(ForgotPWActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
}
