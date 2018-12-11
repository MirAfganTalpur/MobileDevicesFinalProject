package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        Location location = new Location("naaaaaaa", "type", "addssssssress", "city", "country",
//                "permission", "feauture", true, false, UUID.fromString("2bf07803-5d84-4d65-a931-a0482d46c824"));
        Location location = new Location("naaaaaaa", "type", "addssssssress", "city", "country",
            "permission", "feauture", true, false, UUID.fromString("842c8a70-d9a3-49cf-bfa9-a6d5618cfdef"));
//        FirebaseHelper.addLocation("testing",location);
        FirebaseHelper.updateLocation("testing",location);
//        FirebaseHelper.deleteLocation("testing", "5b1c517a-8d45-43b3-8639-aba468bdae89");
        if (user != null) {
            Log.e("test", "user is signed in");
            FirebaseAuth.getInstance().signOut();
            Log.e("test", "user signed out");
            setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.activity_main);
            Log.e("test", "user is not signed in");
            Log.e("test", location.getUuid().toString());
        }

    }

    public void onLogin(View view) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    public void onSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }
}
