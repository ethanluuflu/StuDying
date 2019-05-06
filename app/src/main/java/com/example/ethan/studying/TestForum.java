package com.example.ethan.studying;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TestForum extends AppCompatActivity implements View.OnClickListener{
    Dialog popAddPost;
    private Button addPostBtn, createPostBtn;
    private EditText addPostTitle, addPostContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_forum);
        addPostBtn = findViewById(R.id.addPostBtn);
        addPostBtn.setOnClickListener(this);
        iniPopup();
    }


    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        addPostTitle = popAddPost.findViewById(R.id.postName);
        addPostContent = popAddPost.findViewById(R.id.postContent);
        createPostBtn = popAddPost.findViewById(R.id.postBtn);

        createPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //something here
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference postRef = database.getReference("Posts").push();
                String postID = postRef.getKey();
                Post post = new Post(postID, addPostTitle.getText().toString(),addPostContent.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                postRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Post Successfully Created.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view == addPostBtn) {
            popAddPost.show();
        }
    }
}
