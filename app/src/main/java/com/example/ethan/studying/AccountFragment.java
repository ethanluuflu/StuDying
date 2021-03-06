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

/**
 * Fragment for My Account part of Navigation menu
 */
public class AccountFragment extends Fragment implements View.OnClickListener{
    private CircleImageView profileImage;
    private ProgressDialog loadingBar;
    private TextView viewUser, viewEmail, viewRating;
    private Button changePW, updateEmail, signOut;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private StorageReference UserProfileImageRef;
    private DatabaseReference userDB;
    final static int Gallery_Pick = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //Instantiates all the variables and attaches to their xml counterpart
        //Also attaches an onClick listener to the buttons
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDB = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        viewUser = view.findViewById(R.id.tvUsername);
        viewEmail = view.findViewById(R.id.tvEmail);
        viewRating = view.findViewById(R.id.tvRating);

        changePW = view.findViewById(R.id.changePWBtn);
        changePW.setOnClickListener(this);

        updateEmail = view.findViewById(R.id.updateEmail);
        updateEmail.setOnClickListener(this);

        signOut = view.findViewById(R.id.signOutBtn);
        signOut.setOnClickListener(this);

        profileImage = (CircleImageView) view.findViewById(R.id.setup_profile_image);
        loadingBar = new ProgressDialog(getActivity());

        //Allows the profile image to be clickable
        //Once clicked, it allows user to select a new image from their pictures
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        //Updates the circle image with the picture from Firebase Storage
        //Else, place a placeholder image in its place
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

    /**
     * Method that activates when User successfully selects an image to be the profile
     */
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

            //Shows the loading bar, to signal upload of picture to Firebase
            if (resultCode == Activity.RESULT_OK) {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we updating your profile image...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                //Destination to Firebase Storage
                final StorageReference filePath = UserProfileImageRef.child(user.getUid() + ".jpg");

                // On Success, send user back to home page
                // Create a link to the Firebase Storage Location under the User data
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

                            //Create a new node under the Current user.
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

        //Updates the username and email if there are changes to the data
        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Grab user object from database
                User currUser = dataSnapshot.getValue(User.class);
                viewUser.setText(currUser.getUser());
                viewEmail.setText(currUser.getEmail());

                if (dataSnapshot.hasChild("rating")) {
                    viewRating.setText("" + currUser.getRating());
                } else {
                    viewRating.setText("4");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Handles the button onClick. Each buttons redirects to a different Activity.
     * @param view
     */
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
