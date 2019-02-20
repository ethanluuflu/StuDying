package com.example.ethan.studying;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerButton;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        registerButton = findViewById(R.id.registerBT);
        etName = findViewById(R.id.nameET);
        etEmail = findViewById(R.id.emailET);
        etPassword = findViewById(R.id.passwordET);
        etConfirmPassword = findViewById(R.id.confirmPasswordET);

        registerButton.setOnClickListener(this);
    }

    private void registerUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

//        if(!email.contains(".edu")){
//            //user does not input school email
//            Toast.makeText(this, "Please enter your school email address", Toast.LENGTH_SHORT).show();
//            //stop function execution
//            return;
//        }

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
            //stop function execution
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            //stop function execution
            return;
        }

        if(!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())){
            //passwords do not match
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            //stop function execution
            return;
        }

        //show progress dialog
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //create a user using email/password and store into Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user is successfully registered
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            Toast.makeText(RegisterActivity.this, "Authentication succeeded.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            //user failed to register
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //check if email already exist
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "This email already exist.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view){
        if(view == registerButton){
            registerUser();
        }
    }
}
