package com.example.ethan.studying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context mCtx;
    private ArrayList<Post> mPostList;


    public PostAdapter(Context mCtx, ArrayList<Post> list) {
        this.mCtx = mCtx;
        mPostList = list;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.post_item,parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        Post item = mPostList.get(position);

        holder.mPostName.setText(item.getTitle());
        holder.mDate.setText(item.getTime());
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.mAuthor.setText("Posted by " + dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView mPostName;
        public TextView mAuthor;
        public TextView mDate;

        public PostViewHolder(View itemView) {
            super(itemView);
            mPostName = itemView.findViewById(R.id.forumName);
            mAuthor = itemView.findViewById(R.id.authorPost);
            mDate = itemView.findViewById(R.id.postDate);
        }
    }
}