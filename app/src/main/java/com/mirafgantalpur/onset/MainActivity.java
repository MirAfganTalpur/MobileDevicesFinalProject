package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.e("test", "user is signed in");
            FirebaseAuth.getInstance().signOut();
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
