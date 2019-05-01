package com.example.ethan.studying;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment implements View.OnClickListener{
    private CircleImageView profileImage;
//    private Button Saveinformationbutton;
    private ProgressDialog loadingBar;
    private TextView viewUser, viewEmail, viewAge, viewRating;
    private Button changePW, updateEmail, signOut;
//    private Spinner dayMenu,monthMenu,yearMenu;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private StorageReference UserProfileImageRef;
    private DatabaseReference userDB;
    final static int Gallery_Pick = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDB = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        viewUser = view.findViewById(R.id.tvUsername);
        viewEmail = view.findViewById(R.id.tvEmail);
//        viewAge = view.findViewById(R.id.tvAge);
        viewRating = view.findViewById(R.id.tvRating);

        changePW = view.findViewById(R.id.changePWBtn);
        changePW.setOnClickListener(this);

        updateEmail = view.findViewById(R.id.updateEmail);
        updateEmail.setOnClickListener(this);

        signOut = view.findViewById(R.id.signOutBtn);
        signOut.setOnClickListener(this);

//        dayMenu = view.findViewById(R.id.day_menu);
//        monthMenu = view.findViewById(R.id.month_menu);
//        yearMenu = view.findViewById(R.id.year_menu);

//        ArrayList<String> days = new ArrayList<String>();
//        days.add("Select Day");
//        for(int i = 1;i< 32;i++) {
//            days.add(Integer.toString(i));
//        }
//        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_dropdown_item,days);
//        dayMenu.setAdapter(dayAdapter);
//
//
//        ArrayList<String> years = new ArrayList<String>();
//        years.add("Select Year");
//        for(int i = 1960;i< 2020;i++) {
//            years.add(Integer.toString(i));
//        }
//        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_dropdown_item,years);
//        yearMenu.setAdapter(yearAdapter);

        profileImage = (CircleImageView) view.findViewById(R.id.setup_profile_image);
        loadingBar = new ProgressDialog(getActivity());


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //test code for errors
                    if (dataSnapshot.hasChild("profileimage")) {

                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.studylogo).into(profileImage);
                    } else {
                        Toast.makeText(getActivity(), "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == Activity.RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getContext(),this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == Activity.RESULT_OK) {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Updating your profile image...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(user.getUid() + ".jpg");

                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Toast.makeText(getActivity(), "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = downUri.toString();
                            userDB.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent selfIntent = new Intent(getActivity(), MainActivity.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(getActivity(), "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(getActivity(), "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });

                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Error Occurred: Image can not be cropped. Try Again.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    //Use onStart to update
    //since onStart is called each time, activity is active
    @Override
    public void onStart() {
        super.onStart();

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Grab user object from database
                User currUser = dataSnapshot.getValue(User.class);
                viewUser.setText("Username: " + currUser.getUser());
                viewEmail.setText("Email Address: " + currUser.getEmail());

//                if (dataSnapshot.hasChild("age")) {
//                    viewAge.setText("Age: " + currUser.getAge());
//                } else {
//                    viewAge.setText("Age: NULL");
//                }

                if (dataSnapshot.hasChild("rating")) {
                    viewRating.setText("Average Rating: " + currUser.getRating());
                } else {
                    viewRating.setText("Average Rating: N/A");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view){

        if (view == updateEmail) {
            Intent i = new Intent(getActivity(),UpdateEmailActivity.class);
            startActivity(i);
        }
        if(view == changePW){
            Intent i = new Intent(getActivity(), ChangePWActivity.class);
            startActivity(i);
        }
        if (view == signOut) {
            Intent i = new Intent(getActivity(), LoginActivity.class);
            mAuth.signOut();
            startActivity(i);
        }
    }
}
