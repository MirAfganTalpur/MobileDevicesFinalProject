package com.mirafgantalpur.onset;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.mirafgantalpur.onset.AddLocation.PERMISSIONS_MULTIPLE_REQUEST;

public class MainActivity extends AppCompatActivity {
    Button login;
    Button signup;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.signup_button);
        error = findViewById(R.id.permissions_error_edittext);


        // Check if app has permission to use location features, if not, ask for permission
        if(!checkPermissions()){
         requestLocationPermissions();
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

    public boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestLocationPermissions() {
        requestPermissions(new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_MULTIPLE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (checkPermissions()){
            login.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            error.setText("");
        } else {
            login.setVisibility(View.GONE);
            signup.setVisibility(View.GONE);
            error.setText(R.string.permissions_error);
            requestLocationPermissions();
        }
    }
}
