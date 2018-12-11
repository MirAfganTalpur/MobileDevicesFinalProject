package com.mirafgantalpur.onset;



import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public final class FirebaseHelper {

    static void addLocation(String username, Location location) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> locationInfo = new HashMap<>();
        locationInfo.put(location.getUuid().toString(), location);
        ref.child("users").child(username).child("locations").updateChildren(locationInfo);
        if (!location.isOnlyForMe()) {
            location.setAuthorUsername(username);
            ref.child("sharedLocations").updateChildren(locationInfo);
            ref.child("users").child(username).child("sharedLocations").updateChildren(locationInfo);
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
            ref.child("users").child(username).child("sharedLocations").child(location.getUuid().toString()).removeValue();
        } else {
            location.setAuthorUsername(username);
            ref.child("sharedLocations").updateChildren(locationInfo);
            ref.child("users").child(username).child("sharedLocations").updateChildren(locationInfo);
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
        ref.child("users").child(username).child("sharedLocations").child(uuid).removeValue();
        ref.child("sharedLocations").child(uuid).removeValue();
    }


}
