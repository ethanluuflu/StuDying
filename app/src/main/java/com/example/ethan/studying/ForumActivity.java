package com.example.ethan.studying;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Forum Activity for each Group
 */
public class ForumActivity extends AppCompatActivity {

    private RecyclerView mForumList;
    private TextView mGroupName, mGroupDescription;
    private Button memberList;
    private ArrayList<Post> postList;
    private PostAdapter adapter;
    private Dialog popAddPost;
    private Button createPostBtn, chatButton;
    private EditText addPostTitle, addPostContent;
    private String groupID;

    //Sets the layout for the Forum
    //Updates the TextView with information regarding the designated group
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Group Forum");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FFDF00"));
        toolbar.setTitleTextColor(Color.parseColor("#303030"));

        groupID = getIntent().getStringExtra("Group_ID");
        mGroupName = findViewById(R.id.groupName);
        mGroupDescription = findViewById(R.id.groupDescription);
        FirebaseDatabase.getInstance().getReference("Groups").child(groupID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Groups group = dataSnapshot.getValue(Groups.class);
                mGroupName.setText(group.getGroupName());
                mGroupDescription.setText(group.getGroupDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postList = new ArrayList<>();

        mForumList = (RecyclerView) findViewById(R.id.groupForum);


        // Add a floating action click handler for creating new entries.
        FloatingActionButton fab = (FloatingActionButton)
                findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
                Toast.makeText(getApplicationContext(), "Add Forum Post",Toast.LENGTH_LONG).show();
            }
        });

        //Sends necessary information for MemberList activity
        memberList = findViewById(R.id.membersList);
        memberList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GroupMemberList.class);
                i.putExtra("Group_ID", groupID);
                startActivity(i);
            }
        });

        //Launches chat button on click. Sends group info to activity
        chatButton = findViewById(R.id.chatBtn);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                i.putExtra("Group_ID", groupID);
                startActivity(i);
            }
        });
        iniPopup();
    }

    //Sets up the layout for adding a new post.
    //Starts up when user clicks the floating action button
    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAddPost.getWindow().setLayout(android.widget.Toolbar.LayoutParams.MATCH_PARENT, android.widget.Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        addPostTitle = popAddPost.findViewById(R.id.postName);
        addPostContent = popAddPost.findViewById(R.id.postContent);
        createPostBtn = popAddPost.findViewById(R.id.postBtn);

        //Sets layout to be visible once the user clicks the Add Post button.
        //Adds post to Firebase Database for storage
        createPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //something here
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference postRef = database.getReference("Posts").child(groupID).push();
                String postID = postRef.getKey();
                final Post post = new Post(postID, addPostTitle.getText().toString(),addPostContent.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                postRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Post Successfully Created.", Toast.LENGTH_SHORT).show();
                        //insertPost(post);
                        addPostTitle.setText("");
                        addPostContent.setText("");
                        popAddPost.dismiss();
                    }
                });
            }
        });
    }

    //Insert the Post onto the List and update Layout
    public void insertPost(Post post) {
        postList.add(post);
        adapter.notifyItemInserted(0);
    }

    //On start, pulls all the forum posts for the group
    //and displays them onto list
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference("Posts").child(groupID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Post post = child.getValue(Post.class);
                    postList.add(0,post);
                }
                mForumList.setHasFixedSize(true);
                mForumList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                adapter = new PostAdapter(getApplicationContext(), postList);
                mForumList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}