package com.lkersten.android.static_project.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lkersten.android.static_project.R;
import com.lkersten.android.static_project.model.Profile;

public class ProfileFragment extends Fragment {

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //load user profile data on initial viewing
        //loadUserProfile();
    }

    @Override
    public void onResume() {
        super.onResume();

        //load user profile data when returning from edit
        loadUserProfile();
    }

    private void loadUserProfile() {
        //populate info based on user profile from firebase
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

                if (getView() == null || getContext() == null || userProfile == null) {
                    return;
                }

                //grab array of platforms from resources
                String[] platforms = getContext().getResources().getStringArray(R.array.platforms_list);

                //set UI
                ((TextView)getView().findViewById(R.id.profile_text_username)).setText(userProfile.getUsername());
                ((TextView)getView().findViewById(R.id.profile_text_games)).setText(userProfile.getGames());
                ((TextView)getView().findViewById(R.id.profile_text_platforms)).setText(platforms[userProfile.getPlatforms()]);
                ((TextView)getView().findViewById(R.id.profile_text_bio)).setText(userProfile.getBio());
            }
        });
    }
}
