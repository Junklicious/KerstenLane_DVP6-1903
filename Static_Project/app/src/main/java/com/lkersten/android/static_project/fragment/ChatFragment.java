package com.lkersten.android.static_project.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lkersten.android.static_project.R;
import com.lkersten.android.static_project.ViewProfileActivity;
import com.lkersten.android.static_project.adapter.ChatHolder;
import com.lkersten.android.static_project.model.Chat;

import java.util.List;

public class ChatFragment extends Fragment {

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    private FirebaseUser mUser;
    private RecyclerView mRecyclerView;
    private CollectionReference mChatRef;
    private Query mChatQuery;
    private FirestoreRecyclerAdapter mAdapter;
    private String mConnectionID;
    private String mMatchedUserID;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set has options menu
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null || getActivity().getIntent() == null || getView() == null) {
            return;
        }

        //get current user
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //extract intent data
        Intent chatIntent = getActivity().getIntent();
        mConnectionID = chatIntent.getStringExtra(ConnectionsFragment.EXTRA_CONNECTION_ID);

        //get matched user ID
        getMatchedUserID();

        //setup chat info
        mChatRef = FirebaseFirestore.getInstance().collection("Connections").document(mConnectionID).collection("Chat");
        mChatQuery = mChatRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(50);

        //setup recyclerView
        mRecyclerView = getView().findViewById(R.id.chat_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(manager);

        //setup adapter and attach to recyclerView
        setupAdapter();
        attachRecyclerViewAdapter();

        //setup send button
        final EditText messageText = getView().findViewById(R.id.chat_text);
        getView().findViewById(R.id.chat_btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage(new Chat(mUser.getUid(), messageText.getText().toString()));
                messageText.setText("");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //start listening
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //stop listening
        mAdapter.stopListening();
    }

    private void attachRecyclerViewAdapter() {
        //scroll to bottom
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    //setup adapter
    private void setupAdapter() {
        FirestoreRecyclerOptions<Chat> options =
                new FirestoreRecyclerOptions.Builder<Chat>()
                        .setQuery(mChatQuery, Chat.class)
                        .build();

        mAdapter = new FirestoreRecyclerAdapter<Chat, ChatHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //create view holder
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message, viewGroup, false);
                return new ChatHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }
        };
    }

    private void addMessage(Chat chat) {
        if (mChatRef != null) {
            mChatRef.add(chat);
        }
    }

    private void getMatchedUserID() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Connections").document(mConnectionID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //TODO: get match ID
                List<String> users = (List<String>) documentSnapshot.get("Users");
                if (users == null || users.size() < 2) {
                    return;
                }
                users.remove(mUser.getUid());
                mMatchedUserID = users.get(0);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //create options menu
        inflater.inflate(R.menu.menu_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //switch on menu item id
        switch (item.getItemId()) {
            case R.id.action_view_profile:
                Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
                intent.putExtra(EXTRA_USER_ID, mMatchedUserID);
                startActivity(intent);
                break;
            case R.id.action_delete:
                if (getContext() == null) {
                    return false;
                }
                //create delete dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.delete_dialog_title);
                builder.setMessage(R.string.delete_dialog_message);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //disable connection
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Connections").document(mConnectionID).update("Enabled", false)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //dismiss view
                                if (getActivity() != null) {
                                    getActivity().finish();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
                break;
        }

        return true;
    }
}
