package com.lkersten.android.static_project.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lkersten.android.static_project.R;
import com.lkersten.android.static_project.model.Profile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowseFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_RC = 0x001010;

    private String mDisplayedUserID;
    private FirebaseUser mUser;

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

        //retrieve current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //save member
            mUser = user;
        }

        //add button functionality
        getView().findViewById(R.id.browse_btn_yes).setOnClickListener(this);
        getView().findViewById(R.id.browse_btn_no).setOnClickListener(this);

        //find new players based on user info
        findNewPlayer();
    }

    @Override
    public void onClick(View v) {
        //check that a user is displayed
        if (mDisplayedUserID == null || mDisplayedUserID.isEmpty() || getActivity() == null) {
            return;
        }
        //show loading state message
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.browse_fragment_container, EmptyStateFragment.newInstance("Loading"))
                .commit();

        //check which action was taken
        if (v.getId() == R.id.browse_btn_yes) {
            //add user to blacklist
            checkMatch(mDisplayedUserID);
        } else if (v.getId() == R.id.browse_btn_no) {
            //add user to blacklist
            addToBlackList(mDisplayedUserID);
        }

        //find new player
        findNewPlayer();
    }

    private void findNewPlayer() {
        //check for user
        if (mUser == null || getView() == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //convert data to profile
                final Profile userProfile = documentSnapshot.toObject(Profile.class);

                if (getView() == null || getActivity() == null || userProfile == null) {
                    return;
                }

                //dialog for selecting which game to search based on

                //set check profile info
                List<String> games = userProfile.getGames();
                int platform = userProfile.getPlatforms();
                Location tempLocation = null;

                //check if location data is enabled
                if (userProfile.getLocationEnabled()) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //grab location info
                        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                        tempLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    } else {
                        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_RC);
                        return;
                    }
                }

                //final location for use in filter
                final Location userLocation = tempLocation;

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

                            if (player != null && !userProfile.getBlackList().contains(documents.get(i).getId())) {
                                if (userProfile.getLocationEnabled()) {
                                    if (compareLocation(userLocation, player.getLocation())) {
                                        //display player
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.browse_fragment_container, ProfileFragment.newInstance(player))
                                                .commit();
                                        //grab id of user
                                        mDisplayedUserID = documents.get(i).getId();
                                        //release for loop
                                        break;
                                    }
                                } else {

                                    //display player
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.browse_fragment_container, ProfileFragment.newInstance(player))
                                            .commit();
                                    //grab id of user
                                    mDisplayedUserID = documents.get(i).getId();
                                    //release for loop
                                    break;
                                }
                            }

                            if (i == documents.size() - 1) {
                                //place emptystate
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.browse_fragment_container, EmptyStateFragment.newInstance("No users match your preferences"))
                                        .commit();
                            }
                        }
                    }
                });
            }
        });
    }

    private boolean compareLocation(Location userLocation, GeoPoint matchLocation) {
        //save userLocation for future use in comparisons
        if (mUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(mUser.getUid())
                    .update("location", new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude()));
        }

        if (userLocation == null) {
            return true;
        }

        if (matchLocation == null) {
            return false;
        }

        //convert matchLocation to actual location
        Location compareLocation = new Location("");
        compareLocation.setLatitude(matchLocation.getLatitude());
        compareLocation.setLongitude(matchLocation.getLongitude());

        return userLocation.distanceTo(compareLocation) <= 10000;
    }

    private void addToBlackList(String userToBlacklist) {
        //check for current user
        if (mUser == null) {
            return;
        }

        //get firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(mUser.getUid()).update("blackList", FieldValue.arrayUnion(userToBlacklist));
    }

    private void checkMatch(final String matchedUserID) {
        //add user to blacklist
        addToBlackList(matchedUserID);

        //firebase query to check for chat
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference connectionsRef = db.collection("Connections");
        Query query = connectionsRef.whereEqualTo("Users", Arrays.asList(matchedUserID, mUser.getUid()));

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null || queryDocumentSnapshots.getDocuments().size() == 0) {
                    //Create Chat
                    Map<String, Object> connection = new HashMap<>();
                    connection.put("Users", Arrays.asList(mUser.getUid(), matchedUserID));
                    connection.put("Enabled", false);
                    db.collection("Connections").document().set(connection);

                } else if (queryDocumentSnapshots.getDocuments().size() == 1) {
                    //Enable Chat
                    DocumentSnapshot chatConnection = queryDocumentSnapshots.getDocuments().get(0);
                    db.collection("Connections").document(chatConnection.getId()).update("Enabled", true);
                }
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
            Toast.makeText(getContext(), "Enable Location", Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }
}
