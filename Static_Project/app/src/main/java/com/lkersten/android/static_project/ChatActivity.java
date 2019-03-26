package com.lkersten.android.static_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lkersten.android.static_project.fragment.ChatFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ChatFragment.newInstance())
                .commit();
    }
}
