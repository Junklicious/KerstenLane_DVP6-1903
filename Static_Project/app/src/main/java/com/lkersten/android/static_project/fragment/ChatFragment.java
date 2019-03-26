package com.lkersten.android.static_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class ChatFragment extends Fragment {

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null || getActivity().getIntent() == null) {
            return;
        }

        //extract intent data
        Intent chatIntent = getActivity().getIntent();
        String connectionID = chatIntent.getStringExtra(ConnectionsFragment.EXTRA_CONNECTION_ID);


    }
}
