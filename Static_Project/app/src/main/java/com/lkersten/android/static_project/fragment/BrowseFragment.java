package com.lkersten.android.static_project.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lkersten.android.static_project.R;

public class BrowseFragment extends Fragment implements View.OnClickListener {

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

        //add profile fragment to browse page
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.browse_fragment_container, ProfileFragment.newInstance())
                .commit();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.browse_btn_yes) {

        } else if (v.getId() == R.id.browse_btn_no) {

        }
    }
}
