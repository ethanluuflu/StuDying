package com.example.ethan.studying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Adapter class for Group List displayed before rating members
 */
public class GroupListRateAdapter extends RecyclerView.Adapter<GroupListRateAdapter.GroupListRateViewHolder> {

    private Context mCtx;
    private ArrayList<Groups> groups;
    private FirebaseUser user;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onRateClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public GroupListRateAdapter(Context mCtx, ArrayList<Groups> groups) {
        this.mCtx = mCtx;
        this.groups = groups;
    }

    //Initializes the proper layout
    @NonNull
    @Override
    public GroupListRateAdapter.GroupListRateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.group_item_rate,parent, false);
        return new GroupListRateAdapter.GroupListRateViewHolder(view);
    }

    //Attaches the proper data of the group to their respective layout locations
    //Pulls data from Firebase Database
    @Override
    public void onBindViewHolder(@NonNull final GroupListRateAdapter.GroupListRateViewHolder holder, int position) {
        Groups group = groups.get(position);
        holder.groupName.setText(group.getGroupName());
        holder.groupSubject.setText("Subject: " + group.getGroupSubject());

        //Checks if user is a member of the group by locating the user in the database
        //If user is under the group, then member
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupID()).
                child("members").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String type = dataSnapshot.getValue(String.class);
                    holder.memberStatus.setText(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    //Class to connect variables to layout components
    class GroupListRateViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public TextView groupSubject;
        public TextView memberStatus;
        public Button mRate;

        public GroupListRateViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupSubject = itemView.findViewById(R.id.groupSubject);
            memberStatus = itemView.findViewById(R.id.memberStatus);
            mRate = itemView.findViewById(R.id.rateBtn);

            //OnClick method for Rate button
            mRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onRateClick(position);
                        }
                    }
                }
            });
        }
    }
}
