package com.example.ethan.studying;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button profileBtn;
    private Button groupBtn;
    private Button accountBtn;
    private Button supportBtn;
    private Button aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileBtn = findViewById(R.id.profileButton);
        groupBtn = findViewById(R.id.groupButton);
        accountBtn = findViewById(R.id.accountButton);
        supportBtn = findViewById(R.id.supportButton);
        aboutBtn = findViewById(R.id.aboutButton);

        profileBtn.setOnClickListener(this);
        groupBtn.setOnClickListener(this);
        accountBtn.setOnClickListener(this);
        supportBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view == profileBtn){

        }
        if(view == groupBtn) {
            Intent i = new Intent(MainActivity.this, GroupActivity.class);
            startActivity(i);
        }
        if(view == accountBtn) {
            Intent i = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(i);
        }
        if(view == supportBtn) {
            Intent i = new Intent(MainActivity.this, SupportActivity.class);
            startActivity(i);
        }
        if(view == aboutBtn) {
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
        }
    }

}
