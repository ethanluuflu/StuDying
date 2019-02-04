package com.example.ethan.studying;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView registerLink;
    private FirebaseAuth firebaseAuth;
    private EditText edEmail, edPassword;
    private Button loginBtn;
    private ProgressDialog progressDialog;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        registerLink = findViewById(R.id.registerLink);

        registerLink.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        edEmail = findViewById(R.id.userNameEditText);
        edPassword = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(this);


    }

    private void loginUser() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

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

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user is successfully registered
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login succeeded.",
                                    Toast.LENGTH_SHORT).show();
                            //startActivity(whatever this directs to);
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
    @Override
    public void onClick(View view){
        if(view == registerLink){
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.loginButton) {
            loginUser();
        }
    }
}
