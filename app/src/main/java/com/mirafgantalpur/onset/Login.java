package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view)
    {
        Intent intent = new Intent(Login.this, LocationList.class);
        startActivity(intent);
    }

    public void onBack(View view)
    {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}
