package com.example.ethan.studying;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class groupList extends ArrayAdapter<Groups> {

    private Activity context;
    private String type = "";
    private FirebaseUser user;
    List<Groups> groups;

    public groupList(Activity context, List<Groups> groups) {
        super(context,R.layout.activity_group_list, groups);
        this.context = context;
        this.groups = groups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_group_list, null, true);

        TextView groupName = (TextView) listViewItem.findViewById(R.id.tvGroupName);
        TextView groupSubject = listViewItem.findViewById(R.id.tvSubject);
        final TextView memberType = listViewItem.findViewById(R.id.tvMemberType);

        Groups group = groups.get(position);
        groupName.setText(group.getGroupName());
        groupSubject.setText("Subject: "+ group.getGroupSubject());
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupID()).
                 child("members").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if(dataSnapshot.getValue() == null) {
                        memberType.setText("Not a member");
                    } else {
                        type = dataSnapshot.getValue(String.class);
                        memberType.setText(type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        memberType.setText("Group Leader");
        return listViewItem;
    }
}
