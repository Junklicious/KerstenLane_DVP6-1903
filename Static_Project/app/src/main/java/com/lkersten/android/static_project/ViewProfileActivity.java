package com.lkersten.android.static_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lkersten.android.static_project.fragment.ChatFragment;
import com.lkersten.android.static_project.fragment.ProfileFragment;
import com.lkersten.android.static_project.model.Profile;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //display user from from intent
        Intent intent = getIntent();
        String userID = intent.getStringExtra(ChatFragment.EXTRA_USER_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Profile userToDisplay = documentSnapshot.toObject(Profile.class);
                if (userToDisplay != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, ProfileFragment.newInstance(userToDisplay))
                            .commit();
                }
            }
        });
    }
}
