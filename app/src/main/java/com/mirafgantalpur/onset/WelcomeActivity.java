package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private TextView welcomeUserName;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // greet the user by their username
        welcomeUserName = findViewById(R.id.welcome_username);
        username = getIntent().getStringExtra("username");
        welcomeUserName.setText(username.toUpperCase());
    }

    public void viewMyLocations(View view) {
        Intent intent = new Intent(WelcomeActivity.this, LocationList.class);
        intent.putExtra("choice", "myLocations");
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void viewSharedLocations(View view) {
        Intent intent = new Intent(WelcomeActivity.this, LocationList.class);
        intent.putExtra("choice", "sharedLocations");
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
