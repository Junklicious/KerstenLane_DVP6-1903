package com.lkersten.android.static_project.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lkersten.android.static_project.R;
import com.lkersten.android.static_project.model.Profile;

public class EditFragment extends Fragment {

    public static EditFragment newInstance() {
        return new EditFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set has options
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //pre populate fields
        loadCurrentUserData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_save) {
            //save user data from fields
            saveUserData();
        }

        return true;
    }

    private void loadCurrentUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null || getView() == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //convert data to profile
                Profile userProfile = documentSnapshot.toObject(Profile.class);

                if (getView() == null || userProfile == null) {
                    return;
                }

                //set UI from Database
                ((TextView)getView().findViewById(R.id.edit_text_username)).setText(userProfile.getUsername());
                ((TextView)getView().findViewById(R.id.edit_text_games)).setText(userProfile.getGames());
                ((Spinner)getView().findViewById(R.id.edit_spinner_platforms)).setSelection(userProfile.getPlatforms());
                ((TextView)getView().findViewById(R.id.edit_text_bio)).setText(userProfile.getBio());
            }
        });
    }

    private void saveUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null || getView() == null || getActivity() == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String username = ((TextView)getView().findViewById(R.id.edit_text_username)).getText().toString();
        String games = ((TextView)getView().findViewById(R.id.edit_text_games)).getText().toString();
        int platforms = ((Spinner)getView().findViewById(R.id.edit_spinner_platforms)).getSelectedItemPosition();
        String bio = ((TextView)getView().findViewById(R.id.edit_text_bio)).getText().toString();

        db.collection("Users").document(user.getUid()).set(new Profile(username, games, platforms, bio));

        getActivity().finish();
    }
}
