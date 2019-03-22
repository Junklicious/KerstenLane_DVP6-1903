package com.lkersten.android.static_project.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lkersten.android.static_project.R;

public class EmptyStateFragment extends Fragment {

    private String message;

    public static EmptyStateFragment newInstance(String message) {
        EmptyStateFragment fragment = new EmptyStateFragment();
        fragment.message = message;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emptystate, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() == null) {
            return;
        }

        //set message
        ((TextView)getView().findViewById(R.id.empty_text)).setText(message);
    }
}
