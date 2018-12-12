package com.mirafgantalpur.onset;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.Locator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FirebaseHelper {

    static void addLocation(String username, Location location) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> locationInfo = new HashMap<>();
        locationInfo.put(location.getUuid().toString(), location);
        ref.child("users").child(username).child("locations").updateChildren(locationInfo);
        if (!location.isOnlyForMe()) {
            location.setAuthorUsername(username);
            ref.child("sharedLocations").updateChildren(locationInfo);
        }
    }


    static void updateLocation(String username, Location location) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> locationInfo = new HashMap<>();
        locationInfo.put(location.getUuid().toString(), location);
        ref.child("users").child(username).child("locations").updateChildren(locationInfo);
        if (location.isOnlyForMe()) {
            location.setAuthorUsername("");
            deleteLocationFromShared(location.getUuid().toString());
        } else {
            location.setAuthorUsername(username);
            ref.child("sharedLocations").updateChildren(locationInfo);
        }
    }

    static void deleteLocationFromShared(String uuid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("sharedLocations").child(uuid).removeValue();
        ref.child("users").child("sharedLocations").child(uuid).removeValue();
    }

    static void deleteLocation(String username, String uuid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(username).child("locations").child(uuid).removeValue();
        ref.child("sharedLocations").child(uuid).removeValue();
    }

    static void getAllUserLocations(String username, final LocationList uiReference) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("locations");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Location> locations = new ArrayList<>();
                        for (DataSnapshot location : dataSnapshot.getChildren()) {
                            Location testing = new Location(location, location.getKey());
                            locations.add(testing);
                        }

                        uiReference.updateUI(locations);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    static void getAllSharedLocations(final LocationList uiReference) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("sharedLocations");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("test", dataSnapshot.getValue().toString());
                        ArrayList<Location> locations = new ArrayList<>();

                        for (DataSnapshot location : dataSnapshot.getChildren()) {
                            Location testing = new Location(location, location.getKey());
                            testing.setAuthorUsername(location.child("authorUsername").getValue().toString());
                            locations.add(testing);
                        }
                        Log.e("test", String.valueOf(locations.size()));
                        uiReference.updateUI(locations);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    static void signUp (final String email, final String password, final String username, final String fullName, final SignUp uiReference) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(uiReference, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("test", "created new user");
                            User user = new User(username, email.toLowerCase(), fullName, password);
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                            myRef.child("users").child(username.toLowerCase()).setValue(user);
                            Intent intent = new Intent(uiReference, WelcomeActivity.class);
                            intent.putExtra("username", username);
                            uiReference.startActivity(intent);
                        } else {
                            Toast.makeText(uiReference, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                uiReference.error.setText(R.string.password_too_weak);

                            } catch (FirebaseAuthInvalidCredentialsException e) {

                                if (e.getMessage().equals("The email address is badly formatted.")) {
                                    uiReference.error.setText(R.string.improper_email);
                                }
                            } catch (FirebaseAuthUserCollisionException e) {
                                uiReference.error.setText(R.string.email_used);
                            } catch (Exception e) {

                            }
                        }
                    }
                });
    }


}
