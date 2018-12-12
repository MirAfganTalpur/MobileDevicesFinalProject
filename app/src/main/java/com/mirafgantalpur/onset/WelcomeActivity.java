package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private TextView welcomeUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeUserName = findViewById(R.id.welcomeUserName);
        welcomeUserName.setText(getIntent().getStringExtra("username").toUpperCase());

    }

    public void viewMyLocations(View view) {
        Intent intent = new Intent(WelcomeActivity.this, LocationList.class);
        intent.putExtra("choice", "myLocations");
        intent.putExtra("username", (String) getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    public void viewSharedLocations(View view) {
        Intent intent = new Intent(WelcomeActivity.this, LocationList.class);
        intent.putExtra("choice", "sharedLocations");
        intent.putExtra("username", (String) getIntent().getExtras().get("username"));
        startActivity(intent);
    }
}