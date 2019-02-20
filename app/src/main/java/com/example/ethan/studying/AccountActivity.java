package com.example.ethan.studying;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Button changePW, updateEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        changePW = findViewById(R.id.changePWBtn);
        changePW.setOnClickListener(this);

        updateEmail = findViewById(R.id.updateEmail);
        updateEmail.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){

        if (view == updateEmail) {
            Intent i = new Intent(AccountActivity.this,UpdateEmailActivity.class);
            startActivity(i);
        }
        if(view == changePW){
            Intent i = new Intent(AccountActivity.this, ChangePWActivity.class);
            startActivity(i);
        }
    }
}
