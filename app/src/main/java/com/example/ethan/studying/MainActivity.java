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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private ListView mResultList;
    List<Groups> groups;
    private DatabaseReference mGroupDatabase;

    private FirebaseListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGroupDatabase = FirebaseDatabase.getInstance().getReference().child("Groups");
        mSearchField = findViewById(R.id.searchField);
        mSearchBtn = findViewById(R.id.searchBtn);

        mResultList = findViewById(R.id.resultList);

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

        //close keyboard until you click on textbox
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mSearchField.getText().toString().trim();
                firebaseGroupSearch(searchText);
            }
        });

        mResultList = findViewById(R.id.resultList);
        groups = new ArrayList<>();
        mGroupDatabase = FirebaseDatabase.getInstance().getReference("Groups");

        mResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Groups group = groups.get(i);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupID()).child("members").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if(dataSnapshot.getValue() == null) {
                                showUpdateDialog(group.getGroupName(), group.getGroupID(), false);
                            } else {
                                showUpdateDialog(group.getGroupName(), group.getGroupID(), true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });
    }

    private void firebaseGroupSearch(String searchText){
        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();
        Query query = mGroupDatabase.orderByChild("groupName").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseListOptions options = new FirebaseListOptions.Builder<Groups>().setLayout(R.layout.list_item).setQuery(query, Groups.class).build();
        FirebaseListAdapter <Groups> myAdapter = new FirebaseListAdapter<Groups>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull final Groups model, int position) {
                TextView group_Name = v.findViewById(R.id.groupItem);
                group_Name.setText(model.getGroupName());
            }
        };

        myAdapter.startListening();
        mResultList.setAdapter(myAdapter);
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutUsFragment()).commit();
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

    private void showUpdateDialog(String name,final String id, boolean isMember) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflator = getLayoutInflater();
        final View dialogView = inflator.inflate(R.layout.update_member_type, null);
        dialogBuilder.setView(dialogView);

        final Button yesBtn = dialogView.findViewById(R.id.yesBtn);
        final Button noBtn = dialogView.findViewById(R.id.noBtn);
        final TextView question = dialogView.findViewById(R.id.memberQuestion);
        if (isMember) {
            yesBtn.setVisibility(View.INVISIBLE);
            noBtn.setVisibility(View.INVISIBLE);
            question.setText("You are already a member!");
        } else {
            yesBtn.setVisibility(View.VISIBLE);
            noBtn.setVisibility(View.VISIBLE);
            question.setText("Do you want to join the group?");
        }


        dialogBuilder.setTitle("Opening Group " + name);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Groups").child(id).child("members").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("Group Member");
                alertDialog.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

        mGroupDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups.clear();

                for(DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Groups group = groupSnapshot.getValue(Groups.class);
                    groups.add(group);
                }
                groupList groupListAdapter = new groupList(MainActivity.this, groups);
                mResultList.setBackgroundColor(Color.WHITE);
                mResultList.setAdapter(groupListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
