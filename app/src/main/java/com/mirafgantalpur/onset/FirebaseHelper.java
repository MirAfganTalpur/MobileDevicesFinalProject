package com.mirafgantalpur.onset;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {

    private Context context;
    private boolean doesUserExistAlready = true;
    private boolean doesEmailExistAlreaady = true;

    FirebaseHelper(Context context) {
        this.context = context;
    }

    public boolean doesUserExist(String username) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").orderByChild("username").equalTo(username.toLowerCase()).addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.e("test", "dataSnapshot value = " + dataSnapshot.getValue());
                        doesUserExistAlready = dataSnapshot.exists();
                        if (dataSnapshot.exists()) {

                            // User Exists
                            // Do your stuff here if user already exists


                        } else {

                            // User Not Yet Exists
                            // Do your stuff here if user not yet exists
                        }
                    }
                    @Override
                    public void onCancelled (DatabaseError databaseError){

                    }
                }

        );
        return doesUserExistAlready;
    }

    public boolean doesEmailExist (String email){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").orderByChild("email").equalTo(email.toLowerCase()).addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.e("test", "dataSnapshot value = " + dataSnapshot.getValue());
                        doesEmailExistAlreaady = dataSnapshot.exists();
                        if (dataSnapshot.exists()) {

                            // User Exists
                            // Do your stuff here if user already exists


                        } else {

                            // User Not Yet Exists
                            // Do your stuff here if user not yet exists
                        }
                    }
                    @Override
                    public void onCancelled (DatabaseError databaseError){

                    }
                }

        );
        return doesEmailExistAlreaady;
    }



}
