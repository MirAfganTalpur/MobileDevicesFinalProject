package com.mirafgantalpur.onset;

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

    static void getLocationsYoutubeLinks(String username, Location location) { // TODO , final DetailedLocationActivity uiReference) {
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
                        // uiReference.postYoutubeLinks(links); TODO add this method to the DetailedLocationActivity
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


}
