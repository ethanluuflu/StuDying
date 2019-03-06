package com.example.ethan.studying;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    private Button profileBtn;
    private Button groupBtn;
    private Button accountBtn;
    private Button supportBtn;
    private Button aboutBtn;

//    private TextView emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        emailText = findViewById(R.id.txtViewEmail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FFDF00"));
        toolbar.setTitleTextColor(Color.parseColor("#303030"));

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#303030"));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        Intent extras = getIntent();
//        Bundle bundle = extras.getExtras();
//
//        if(bundle != null) {
//            String email = (String) bundle.get("email");
//            emailText.setText(email);
//        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_group:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GroupFragment()).commit();
                break;

            case R.id.nav_forum:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ForumFragment()).commit();
                break;

            case R.id.nav_rating:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RatingFragment()).commit();
                break;

            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountFragment()).commit();
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(i);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;

            case R.id.nav_support:
                Toast.makeText(this, "Contact us at help_studying@gmail.com", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_aboutus:
                Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
