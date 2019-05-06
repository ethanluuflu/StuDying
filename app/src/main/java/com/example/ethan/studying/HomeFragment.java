package com.example.ethan.studying;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class HomeFragment extends Fragment implements View.OnClickListener{

    View v;
    private EditText searchGroup;
    private ImageButton searchBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        searchGroup = v.findViewById(R.id.searchGroup);
        searchBtn = v.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        String groupName = searchGroup.getText().toString().trim();
        Intent i = new Intent(getActivity(),tempGroupList.class);
        i.putExtra("Query", groupName);
        startActivity(i);
    }
}
