package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.loginPassword);
        password = findViewById(R.id.loginUsername);
    }

    public void onLogin(View view) {
        Intent intent = new Intent(Login.this, AddLocation.class);

        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    public void authenticateUserInfo() {
        
    }
}
