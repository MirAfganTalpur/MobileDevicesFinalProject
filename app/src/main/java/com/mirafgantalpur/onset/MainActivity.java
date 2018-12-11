package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseHelper test = new FirebaseHelper(this);
        if (user != null) {
            Log.e("test", "user is signed in");
            FirebaseAuth.getInstance().signOut();
            Log.e("test", "user signed out");
            setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.activity_main);
            Log.e("test", "user is not signed in");
        }

    }

    public void onLogin(View view)
    {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
    public void onSignUp(View view)
    {
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }
}
