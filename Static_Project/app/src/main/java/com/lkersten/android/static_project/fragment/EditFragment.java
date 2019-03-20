package com.lkersten.android.static_project.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.security.keystore.KeyPermanentlyInvalidatedException;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lkersten.android.static_project.R;
import com.lkersten.android.static_project.model.Profile;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class EditFragment extends Fragment {

    private static final int LOCATION_REQUEST_CODE = 0x0101;
    private static final int STORAGE_REQUEST_CODE = 0x0010;
    private static final int PICK_REQUEST_CODE = 0x01010;

    private String mUserID;

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

        if (getView() == null) {
            return;
        }

        //setup an on profile picture click listener
        getView().findViewById(R.id.edit_image_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() == null) {
                    return;
                }

                //check permission
                if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
                    return;
                }

                //pick profile image
                pickImage();
            }
        });

        //setup location enable switch to request location permission
        ((Switch)getView().findViewById(R.id.edit_switch_location)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //request permission
                        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                    }
                }
            }
        });
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
        mUserID = user.getUid();

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
                ((TextView)getView().findViewById(R.id.edit_text_games)).setText(userProfile.getGamesAsList());
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
        String gamesString = ((TextView)getView().findViewById(R.id.edit_text_games)).getText().toString();
        int platforms = ((Spinner)getView().findViewById(R.id.edit_spinner_platforms)).getSelectedItemPosition();
        String bio = ((TextView)getView().findViewById(R.id.edit_text_bio)).getText().toString();
        Boolean locationEnabled = ((Switch)getView().findViewById(R.id.edit_switch_location)).isChecked();

        //format games input
        String[] gamesArray = gamesString.split(",");
        for (int i = 0; i < gamesArray.length; i++) {
            gamesArray[i] = gamesArray[i].trim();
        }

        List<String> games = Arrays.asList(gamesArray);

        db.collection("Users").document(user.getUid()).set(new Profile(username, games, platforms, bio, locationEnabled));

        getActivity().finish();
    }

    private void pickImage() {
        //pick action
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //handle permission denied
            if (getView() == null) {
                return;
            }
            ((Switch)getView().findViewById(R.id.edit_switch_location)).setChecked(false);
        }

        if (requestCode == STORAGE_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //pick profile image
            pickImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //handle result from pick action
            Uri imageUri = data.getData();
            if (imageUri != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReference();

                if (mUserID != null) {
                    StorageReference profilePicRef = storageRef.child("images/"+mUserID+".jpg");
                    UploadTask uploadTask = profilePicRef.putFile(imageUri);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return storageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri downloadUri = task.getResult();
                            Log.i("WTF", "onComplete: " + downloadUri.toString());
                        }
                    });
                }
            }
        }
    }
}
