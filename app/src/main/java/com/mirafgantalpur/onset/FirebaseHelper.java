package com.mirafgantalpur.onset;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class FirebaseHelper {


    static void addLocation(String username, Location location) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> locationInfo = new HashMap<>();
        locationInfo.put(location.getUuid().toString(), location);
        ref.child("users").child(username.toLowerCase()).child("locations").updateChildren(locationInfo);
        if (!location.isOnlyForMe()) {
            location.setAuthorUsername(username);
            ref.child("sharedLocations").updateChildren(locationInfo);
        }
    }

    // updates a location in the database. If the user specifies it isOnlyForMe, removes it from shared
    static void updateLocation(String username, Location location) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> locationInfo = new HashMap<>();
        locationInfo.put(location.getUuid().toString(), location);
        ref.child("users").child(username.toLowerCase()).child("locations").updateChildren(locationInfo);
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
        ref.child("users").child(username.toLowerCase()).child("locations").child(uuid).removeValue();
        ref.child("sharedLocations").child(uuid).removeValue();
    }

    // gathers all the current users locations and uses the uiReference to update the UI
    // with the information when done
    static void getAllUserLocations(String username, final LocationList uiReference) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(username.toLowerCase()).child("locations");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Location> locations = new ArrayList<>();
                        for (DataSnapshot location : dataSnapshot.getChildren()) {
                            Location tmpLocation = new Location(location, location.getKey());
                            locations.add(tmpLocation);
                        }

                        uiReference.updateUI(locations);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    // same as above but shared locations
    static void getAllSharedLocations(final LocationList uiReference) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("sharedLocations");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Location> locations = new ArrayList<>();

                        for (DataSnapshot location : dataSnapshot.getChildren()) {
                            Location tmpLocation = new Location(location, location.getKey());
                            tmpLocation.setAuthorUsername(location.child("authorUsername").getValue().toString());
                            locations.add(tmpLocation);
                        }
                        uiReference.updateUI(locations);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    // creates an ArrayList<String> of all youtube links for a given location. Once complete, uses
    // the uiReference to call the method to post the youtubeLinks
    static void getLocationsYoutubeLinks(String username, Location location, final LocationInfo uiReference) {
        DatabaseReference ref;
        if (location.isOnlyForMe()) {
            ref = FirebaseDatabase.getInstance().getReference().child("users").child(username.toLowerCase()).child("locations").child(location.getUuid().toString()).child("youtubeLinks");
        } else {
            ref = FirebaseDatabase.getInstance().getReference().child("sharedLocations").child(location.getUuid().toString()).child("youtubeLinks");
        }
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> links = new ArrayList<>();
                        for (DataSnapshot link : dataSnapshot.getChildren()) {
                            links.add(link.getValue().toString());
                        }
                        Log.e("test", String.valueOf(links.size()));
                        uiReference.getYouTubeLinks(links);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    // simple search functionality to narrow down the lists shown to the user. Takes the keyType (name, address, city etc.)
    // and iterates over locations to see if that tag value contains the searchTerm
    // For example tag "feature" searchTerm is "pool", it will search through locations and updated the UI with  all locations which
    // contain a "pool" as one of its features
    static void getSearchResults(String username, final String tag, final String searchTerm, Boolean isViewingSharedLocations, final LocationList uiReference) {
        DatabaseReference ref;

        if (isViewingSharedLocations) {
            ref = FirebaseDatabase.getInstance().getReference().child("sharedLocations");
        } else {
            ref = FirebaseDatabase.getInstance().getReference().child("users").child(username.toLowerCase()).child("locations");
        }
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Location> locations = new ArrayList<>();
                        for (DataSnapshot location : dataSnapshot.getChildren()) {
                            String examinedValue = String.valueOf(location.child(tag).getValue());
                            if (examinedValue.toLowerCase().contains(searchTerm.toLowerCase())) {
                                locations.add(new Location(location, location.getKey()));
                            }
                        }
                        uiReference.updateUI(locations);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    // uses FirebaseAuth to ensure no account has been created with the email address and password
    // Then stores the users and their info directly in the database for reference when logging in
    // Stores passwords as plaintext rather than a hashed password
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
