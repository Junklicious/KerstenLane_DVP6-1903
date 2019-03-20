package com.lkersten.android.static_project.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lkersten.android.static_project.R;
import com.lkersten.android.static_project.model.Profile;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class BrowseFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_RC = 0x001010;

    public static BrowseFragment newInstance() {
        return new BrowseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null || getView() == null) {
            return;
        }

        //add button functionality
        getView().findViewById(R.id.browse_btn_yes).setOnClickListener(this);
        getView().findViewById(R.id.browse_btn_no).setOnClickListener(this);

        //find new players based on user info
        findNewPlayer();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.browse_btn_yes) {

        } else if (v.getId() == R.id.browse_btn_no) {

        }
    }

    private void findNewPlayer() {
        //retrieve current user profile for query info
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null || getView() == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //convert data to profile
                Profile userProfile = documentSnapshot.toObject(Profile.class);

                if (getView() == null || getActivity() == null || userProfile == null) {
                    return;
                }

                //set check profile info
                List<String> games = userProfile.getGames();
                int platform = userProfile.getPlatforms();

                //check if location data is enabled
                /*
                if (userProfile.isLocationEnabled()) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        getActivity().requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_RC);
                    }
                } */

                //get location if needed

                //query for new player based on user data
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                CollectionReference usersRef = db.collection("Users");

                Query query = usersRef.whereEqualTo("platforms", platform);

                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots == null) {
                            return;
                        }

                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                        for (int i = 0; i < documents.size(); i++) {
                            Profile player = documents.get(i).toObject(Profile.class);

                            //display player
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.browse_fragment_container, ProfileFragment.newInstance(player))
                                    .commit();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_RC && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            findNewPlayer();
        } else {
            //handle no location permission alert

        }
    }
}
