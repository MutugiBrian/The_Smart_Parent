package com.MwandoJrTechnologies.the_smart_parent.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.MwandoJrTechnologies.the_smart_parent.Chats.ChatActivity;
import com.MwandoJrTechnologies.the_smart_parent.NewsFeed.MainActivity;
import com.MwandoJrTechnologies.the_smart_parent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherParentsProfileActivity extends AppCompatActivity {

    private CircleImageView userProfilePicture;
    private TextView userStatus;
    private TextView fullName;
    private TextView phoneNumber;
    private TextView dateOfBirth;
    private TextView userGender;
    private TextView numberOfChildren;

    private Button openChatButton;

    private DatabaseReference otherParentProfileUserReference;
    // private FirebaseAuth mAuth;

    private String otherParentUserID;
    //  private String currentParentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_parents_profile);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);  //for the back button
        getSupportActionBar().setTitle("View Profile");

        //  mAuth = FirebaseAuth.getInstance();
        //   currentParentUserID = mAuth.getCurrentUser().getUid();


        //get the users id passed from main activity
        otherParentUserID = getIntent().getExtras().get("visit_user_id").toString();

        //get now a reference to the users node
        otherParentProfileUserReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users");

        InitializeFields();

        otherParentProfileUserReference
                .child(otherParentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            String myProfileImage = Objects.requireNonNull(dataSnapshot
                                    .child("profileImage")
                                    .getValue())
                                    .toString();
                            String myStatus = Objects.requireNonNull(dataSnapshot
                                    .child("status")
                                    .getValue())
                                    .toString();
                            String myFullName = Objects.requireNonNull(dataSnapshot
                                    .child("fullName")
                                    .getValue())
                                    .toString();
                            String myPhoneNumber = Objects.requireNonNull(dataSnapshot
                                    .child("phoneNumber")
                                    .getValue())
                                    .toString();
                            String myDateOfBirth = Objects.requireNonNull(dataSnapshot
                                    .child("dob")
                                    .getValue())
                                    .toString();
                            String myGender = Objects.requireNonNull(dataSnapshot
                                    .child("gender")
                                    .getValue())
                                    .toString();
                            String myNumberOfChildren = dataSnapshot
                                    .child("numberOfChildren")
                                    .getValue().toString();

                            Picasso.get()
                                    .load(myProfileImage)
                                    .placeholder(R.drawable.profile_image_placeholder)
                                    .into(userProfilePicture);
                            userStatus.setText(myStatus);
                            fullName.setText("NAME: " + myFullName);
                            phoneNumber.setText("Phone Number: " + myPhoneNumber);
                            dateOfBirth.setText("DOB: " + myDateOfBirth);
                            userGender.setText("Gender: " + myGender);
                            numberOfChildren.setText("Number of children: " + myNumberOfChildren);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        openChatButton.setOnClickListener(v -> {
            Intent chatIntent = new
                    Intent(OtherParentsProfileActivity.this, ChatActivity.class);
            chatIntent.putExtra("visit_user_id", otherParentUserID);
            startActivity(chatIntent);
        });
    }

    //initialize the variables
    private void InitializeFields() {

        userProfilePicture = findViewById(R.id.other_parent_profile_picture);
        userStatus = findViewById(R.id.text_view_other_parent_status);
        fullName = findViewById(R.id.text_view_other_parent_full_name);
        phoneNumber = findViewById(R.id.text_view_other_parent_phone_number);
        dateOfBirth = findViewById(R.id.text_view_other_parent_DOB);
        userGender = findViewById(R.id.text_view_other_parent_gender);
        numberOfChildren = findViewById(R.id.text_view_other_parent_no_of_children);
        openChatButton = findViewById(R.id.button_send_message);

    }

    //activate back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    //open main activity
    private void SendUserToMainActivity() {
        Intent mainActivityIntent = new
                Intent(OtherParentsProfileActivity.this, MainActivity.class);
        finish();
        startActivity(mainActivityIntent);
    }

}
