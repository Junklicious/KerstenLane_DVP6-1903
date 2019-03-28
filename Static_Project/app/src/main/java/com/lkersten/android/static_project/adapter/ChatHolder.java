package com.lkersten.android.static_project.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lkersten.android.static_project.R;
import com.lkersten.android.static_project.model.Chat;

public class ChatHolder extends RecyclerView.ViewHolder {

    private final TextView mUsernameText;
    private final TextView mMessage;
    private final int mBlack;
    private final int mAccent;

    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        mUsernameText = itemView.findViewById(R.id.message_name);
        mMessage = itemView.findViewById(R.id.message_text);
        mBlack = ContextCompat.getColor(itemView.getContext(), R.color.black);
        mAccent = ContextCompat.getColor(itemView.getContext(), R.color.colorAccent);
    }

    public void bind(Chat chat) {
        //setup view
        setUsername(chat.getUserID());
        setMessage(chat.getMessage());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setIsUser(user != null && chat.getUserID().equals(user.getUid()));
    }

    private void setUsername(String uID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username = (String)documentSnapshot.get("username");
                mUsernameText.setText(username);
            }
        });
    }

    private void setMessage(String message) {
        mMessage.setText(message);
    }

    private void setIsUser(boolean isUser) {
        if (isUser) {
            mMessage.setTextColor(mAccent);
            mMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            mUsernameText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            mMessage.setTextColor(mBlack);
            mMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mUsernameText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }
}
