package com.example.ethan.studying;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Chat function for each group.
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    User userx;
    List<Message> messages;
    String groupID;
    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    /**
     * Initializes the variables and set them equal to their layout counterparts
     * Sets the proper layout for activity
     */
    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Group Chat");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FFDF00"));
        toolbar.setTitleTextColor(Color.parseColor("#303030"));
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userx = new User();
        groupID = getIntent().getStringExtra("Group_ID");
        rvMessage = (RecyclerView)findViewById(R.id.rvMessage);
        etMessage = (EditText)findViewById(R.id.etMessage);
        imgButton = (ImageButton)findViewById(R.id.btnSend);
        imgButton.setOnClickListener(this);
        messages = new ArrayList<>();

    }

    /**
     * Sends the message that the user typed on button click
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(etMessage.getText().toString())) {
            DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("Messages").child(groupID).push();
            String id = messageRef.getKey();
            Message message = new Message(etMessage.getText().toString(),id, FirebaseAuth.getInstance().getCurrentUser().getUid());
            messageRef.setValue(message);
            etMessage.setText("");
        }
        else {
            Toast.makeText(this, "You cannot send blank message", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Updates the messages to be more like a realtime chat system
     * Pulls the messages from the database
     * and display them onto the screen via layout
     */
    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();

        messagedb = database.getReference("Messages").child(groupID);

        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messages.add(message);
                displayMessages(messages);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);

                List<Message> newMessages = new ArrayList<Message>();

                for(Message m: messages) {
                    if(m.getMessageID().equals(message.getMessageID())) {
                        newMessages.add(message);
                    }
                    else {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                //message.setKey(dataSnapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();
                for(Message m:messages) {
                    if(!m.getMessageID().equals(message.getMessageID())) {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();
    }

    //Constructs the layout for a standard message
    private void displayMessages(List<Message> messages) {
        rvMessage.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        messageAdapter = new MessageAdapter(ChatActivity.this, messages, messagedb);
        rvMessage.setAdapter(messageAdapter);
    }

}
