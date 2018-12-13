package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        error = findViewById(R.id.login_error_text);
    }

    // when login is clicked, get username and password from edittexts, send to database and verify
    // credentials
    public void onLogin(View view) {
        final String username = this.username.getText().toString();
        final String password = this.password.getText().toString();
        // create instance of database helper
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        if (username.equals("")) {
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_LONG).show();
            error.setText(R.string.enter_username);
        } else {
            ref.child("users").child(username.toLowerCase())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("password").getValue().equals(password)) {
                            error.setText("");
                            // continue on if credentials are verified
                            Intent intent = new Intent(Login.this,
                                                                WelcomeActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, R.string.login_failed,
                                    Toast.LENGTH_LONG).show();
                            error.setText(R.string.wrong_password);
                        }
                    } else {
                        Toast.makeText(Login.this, R.string.login_failed,
                                Toast.LENGTH_LONG).show();
                        error.setText(R.string.invalid_username);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
                });
        }
    }

    public void onBack(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}
