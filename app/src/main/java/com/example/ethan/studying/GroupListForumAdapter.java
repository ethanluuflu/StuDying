package com.example.ethan.studying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupListForumAdapter extends RecyclerView.Adapter<GroupListForumAdapter.GroupListForumViewHolder> {

    private Context mCtx;
    private ArrayList<Groups> groups;
    private OnItemClickListener mListener;
    private FirebaseUser user;

    public interface OnItemClickListener {
        void onForumClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(GroupListForumAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public GroupListForumAdapter(Context mCtx, ArrayList<Groups> groups) {
        this.mCtx = mCtx;
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupListForumAdapter.GroupListForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.group_item_forum,parent, false);
        return new GroupListForumAdapter.GroupListForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupListForumAdapter.GroupListForumViewHolder holder, int position) {
        Groups group = groups.get(position);
        holder.groupName.setText(group.getGroupName());
        holder.groupSubject.setText("Subject: " + group.getGroupSubject());
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupID()).
                child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.memberCount.setText("Member Count: " + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupListForumViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public TextView groupSubject;
        public TextView memberCount;
        public ImageButton mForum;
        public ImageButton mDelete;

        public GroupListForumViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupSubject = itemView.findViewById(R.id.groupSubject);
            memberCount = itemView.findViewById(R.id.memberCount);
            mForum = itemView.findViewById(R.id.forumBtn);
            mDelete = itemView.findViewById(R.id.leaveBtn);

            mForum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onForumClick(position);
                        }
                    }
                }
            });

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}