package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        username = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        error = findViewById(R.id.loginErrorText);
    }

    public void onLogin(View view) {
        final Intent intent = new Intent(Login.this, AddLocation.class);
        final String username = this.username.getText().toString();
        final String password = this.password.getText().toString();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.child("password").getValue().equals(password)) {
                                error.setText("");
                                startActivity(intent);
                            } else {
                                error.setText("Invalid password");

                            }
                        } else {
                           error.setText("Not a valid username");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void onBack(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    public void authenticateUserInfo() {
        
    }
}
