package com.example.ethan.studying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter class for Member List
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private Context mCtx;
    private ArrayList<String> memberID;
    private String groupID;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MemberAdapter(Context mCtx, ArrayList<String> memberID, String groupID) {
        this.mCtx = mCtx;
        this.memberID = memberID;
        this.groupID = groupID;
    }

    @Override
    public MemberAdapter.MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.member_item,parent, false);
        return new MemberAdapter.MemberViewHolder(view);
    }

    //Grabs member information from the ArrayList and populate the layout with the data
    //With the memberID in the arraylist, pull data from Firebase Database
    @Override
    public void onBindViewHolder(final MemberAdapter.MemberViewHolder holder, int position) {
        String item = memberID.get(position);
        FirebaseDatabase.getInstance().getReference("Users").child(item).child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.mMemberName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Groups").child(groupID).
                child("members").child(item).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String type = dataSnapshot.getValue(String.class);
                holder.mMemberStatus.setText(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Group Leader gets option to delete members
        FirebaseDatabase.getInstance().getReference("Groups").child(groupID).child("members")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String type = dataSnapshot.getValue(String.class);
                    if (!type.equals("Group Leader")) {
                        holder.mDelete.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (item.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.mDelete.setVisibility(View.GONE);
            holder.mAdd.setVisibility(View.INVISIBLE);
        }

        //If user has a profile image, load the profile image next to their name.
        FirebaseDatabase.getInstance().getReference("Users").child(item).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //test code for errors
                    if (dataSnapshot.hasChild("profileimage")) {

                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.mipmap.ic_launcher_round).into(holder.profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return memberID.size();
    }

    //Class to attach variables to the layout components
    class MemberViewHolder extends RecyclerView.ViewHolder {
        public TextView mMemberName;
        public TextView mMemberStatus;
        public ImageButton mDelete;
        public ImageButton mAdd;
        public CircleImageView profileImage;

        public MemberViewHolder(View itemView) {
            super(itemView);
            mMemberName = itemView.findViewById(R.id.memberName);
            mMemberStatus = itemView.findViewById(R.id.memberStatus);
            mDelete = itemView.findViewById(R.id.deleteMember);
            mAdd = itemView.findViewById(R.id.addFriend);
            profileImage = itemView.findViewById(R.id.member_profile_image);

            //Delete button allows member to be dropped from group
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });

            //Add button allows members to be added as friend
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onAddClick(position);
                        }
                    }
                }
            });
        }
    }
}