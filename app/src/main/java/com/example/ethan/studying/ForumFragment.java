package com.example.ethan.studying;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Page before going to Forum
 */
public class ForumFragment extends Fragment {

    View v;
    private RecyclerView mGroupList;
    private ArrayList<Groups> groups;
    private DatabaseReference groupsDB;
    private GroupListForumAdapter adapter;

    public ForumFragment() {
    }

    //Sets proper layout for Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_forum, container, false);
        mGroupList = v.findViewById(R.id.group_list_forum);
        groups = new ArrayList<>();
        groupsDB = FirebaseDatabase.getInstance().getReference("Groups");

        return v;
    }

    //Sets up list of groups that User is apart of
    //Displays them on the page with the option to leave group or
    //go to the forum for the selected group
    @Override
    public void onStart() {
        super.onStart();
        groupsDB = FirebaseDatabase.getInstance().getReference("Groups");
        groupsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups.clear();

                for(DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Groups group = groupSnapshot.getValue(Groups.class);
                    if (groupSnapshot.child("members").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        groups.add(group);
                    }
                    adapter = new GroupListForumAdapter(getContext(),groups);
                    mGroupList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mGroupList.setAdapter(adapter);
                    adapter.setOnItemClickListener(new GroupListForumAdapter.OnItemClickListener() {
                        @Override
                        public void onForumClick(int position) {
                            Groups group = groups.get(position);
                            Intent i = new Intent(getActivity(), ForumActivity.class);
                            i.putExtra("Group_ID", group.getGroupID());
                            startActivity(i);
                        }

                        @Override
                        public void onDeleteClick(int position) {
                            Groups group = groups.get(position);
                            FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupID()).child("members").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                            groups.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
