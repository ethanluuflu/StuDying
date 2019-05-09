package com.example.ethan.studying;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Login Activity class that handles all the login operations
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView registerLink, forgotLink;
    private FirebaseAuth firebaseAuth;
    private EditText edEmail, edPassword;
    private Button loginBtn;
    private ProgressDialog progressDialog;

    private static final String TAG = "LoginActivity";

    //Initiates variable and attaches them to layout components
    //Attachs onClick listener to buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        registerLink = findViewById(R.id.registerLink);

        registerLink.setOnClickListener(this);

        forgotLink = findViewById(R.id.forgotLink);
        forgotLink.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        edEmail = findViewById(R.id.userNameEditText);
        edPassword = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(this);
    }

    //Login function
    private void loginUser() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        //Textfields must be filled
        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            //stop function execution
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            //stop function execution
            return;
        }

        progressDialog.setMessage("Logging in User...");
        progressDialog.show();

        //Log into account with the credentials and authorize the login attempt
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user is successfully logged in
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login succeeded.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    //Redirects user to their respective tasks
    @Override
    public void onClick(View view){
        if(view == registerLink){
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.loginButton) {
            loginUser();
        }
        if(view == forgotLink) {
            Intent i = new Intent(LoginActivity.this, ForgotPWActivity.class);
            startActivity(i);
        }
    }

    //If user is already logged in, redirect to home page
    @Override
    protected void onStart() {
        super.onStart();

        //Grabs current logged in user
        //If there is a currently logged in user, redirect to home page.
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Close StuDying+")
                .setMessage("Are you sure you want to close this program?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
