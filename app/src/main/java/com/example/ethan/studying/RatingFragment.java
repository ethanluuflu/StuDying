package com.example.ethan.studying;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class RatingFragment extends Fragment {

    View v;
    private RecyclerView mGroupList;
    private ArrayList<Groups> groups;
    private DatabaseReference groupsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_rating, container, false);
        mGroupList = v.findViewById(R.id.group_list_rate);
        groups = new ArrayList<>();
        groupsDB = FirebaseDatabase.getInstance().getReference("Groups");

        return v;
    }

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
                    GroupListRateAdapter adapter = new GroupListRateAdapter(getContext(),groups);
                    mGroupList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mGroupList.setAdapter(adapter);
                    adapter.setOnItemClickListener(new GroupListRateAdapter.OnItemClickListener() {
                        @Override
                        public void onRateClick(int position) {
                            Groups group = groups.get(position);
                            Intent i = new Intent(getActivity(), RatingActivity.class);
                            i.putExtra("Group_ID", group.getGroupID());
                            startActivity(i);
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
