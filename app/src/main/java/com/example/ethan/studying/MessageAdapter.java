package com.example.ethan.studying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder>{
    Context context;
    List<Message> messages;
    DatabaseReference messageDb;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messageDb) {
        this.context = context;
        this.messageDb = messageDb;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_message,parent,false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapterViewHolder holder, int position) {
        final Message message = messages.get(position);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.tvTitle.setText(dataSnapshot.getValue(String.class) + ": " + message.getMessage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(message.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.tvTitle.setGravity(Gravity.START);

        }
        else {
            holder.ibDelete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageButton ibDelete;

        public MessageAdapterViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            ibDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageDb.child(messages.get(getAdapterPosition()).getMessageID()).removeValue();
                }
            });
        }
    }
}