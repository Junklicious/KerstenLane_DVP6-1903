package com.lkersten.android.static_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lkersten.android.static_project.ChatActivity;
import com.lkersten.android.static_project.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectionsFragment extends ListFragment {

    public static final String EXTRA_CONNECTION_ID = "EXTRA_USER_ID";

    private ArrayList<String> mConnectionIDs;
    private ArrayList<String> mConnectionNames;
    private Map<String, String> mChatIDs;

    public static ConnectionsFragment newInstance() {
        return new ConnectionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connections, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        //grab connections from firebase
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Connections").whereArrayContains("Users", user.getUid())
                .whereEqualTo("Enabled", true);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null) {
                    return;
                }
                //get array of documents
                mConnectionIDs = new ArrayList<>();
                mConnectionNames = new ArrayList<>();

                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (final DocumentSnapshot document : documents) {
                    List<String> users = (List<String>) document.get("Users");
                    if (users == null || users.size() < 2) {
                        return;
                    }
                    users.remove(user.getUid());
                    String matchedID = users.get(0);
                    mChatIDs.put(matchedID, document.getId());
                    db.collection("Users").document(matchedID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            mConnectionIDs.add(documentSnapshot.getId());
                            mConnectionNames.add((String)documentSnapshot.get("username"));

                            //setup adapter
                            if (getContext() != null) {
                                setListAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mConnectionNames));
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position >= mConnectionIDs.size()) {
            return;
        }

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(EXTRA_CONNECTION_ID, mChatIDs.get(mConnectionIDs.get(position)));
        startActivity(intent);
    }
}
