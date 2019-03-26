package com.lkersten.android.static_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lkersten.android.static_project.BrowseActivity;
import com.lkersten.android.static_project.ConnectionsActivity;
import com.lkersten.android.static_project.EditActivity;
import com.lkersten.android.static_project.ProfileActivity;
import com.lkersten.android.static_project.R;
import com.lkersten.android.static_project.model.Profile;
import com.loopj.android.image.SmartImageView;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() == null) {
            return;
        }

        //setup buttons to open each page of the app
        getView().findViewById(R.id.home_btn_browse).setOnClickListener(this);
        getView().findViewById(R.id.home_btn_chat).setOnClickListener(this);
        getView().findViewById(R.id.home_btn_profile).setOnClickListener(this);
        getView().findViewById(R.id.home_btn_logout).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        //get user profile for username and image
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        //get database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //get profile based on userID
        db.collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //convert data to profile
                Profile userProfile = documentSnapshot.toObject(Profile.class);

                //show user edit page so they can create a profile
                if (userProfile == null) {
                    startActivity(new Intent(getActivity(), EditActivity.class));
                    return;
                }

                if (getView() == null || getContext() == null) {
                    return;
                }

                //set UI
                ((TextView) getView().findViewById(R.id.home_text_username)).setText(userProfile.getUsername());

                if (userProfile.getImageUrl() != null && !userProfile.getImageUrl().isEmpty()) {
                    ((SmartImageView)getView().findViewById(R.id.home_image_profile)).setImageUrl(userProfile.getImageUrl());
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        FirebaseAuth Auth = FirebaseAuth.getInstance();
        if (Auth.getCurrentUser() == null) {
            // request that the user sign in before they use the app
            Toast.makeText(getContext(), "Please Sign-in before proceeding", Toast.LENGTH_SHORT).show();
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    123);
            return;
        }

        //intent to be used for navigation buttons
        Intent intent = null;

        //handle button actions based on view ID
        switch (v.getId()) {
            case R.id.home_btn_browse:
                intent = new Intent(getActivity(), BrowseActivity.class);
                startActivity(intent);
                break;
            case R.id.home_btn_chat:
                intent = new Intent(getActivity(), ConnectionsActivity.class);
                startActivity(intent);
                break;
            case R.id.home_btn_profile:
                intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.home_btn_logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder().build(),
                        123);
                break;
        }

    }
}
