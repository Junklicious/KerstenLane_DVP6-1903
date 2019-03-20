package com.lkersten.android.static_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.lkersten.android.static_project.BrowseActivity;
import com.lkersten.android.static_project.ProfileActivity;
import com.lkersten.android.static_project.R;

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
    public void onClick(View v) {

        FirebaseAuth Auth = FirebaseAuth.getInstance();
        if (Auth.getCurrentUser() == null) {
            // request that the user sign in before they use the app
            Toast.makeText(getContext(), "Please Sign-in before proceeding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Intent to player connections page", Toast.LENGTH_SHORT).show();
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
