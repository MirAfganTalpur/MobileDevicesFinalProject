package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {
    private FirebaseHelper firebaseHelper;
    private FirebaseAuth mAuth;
    private TextView error;
    private EditText fullName;
    private EditText username;
    private EditText email;
    private EditText password1;
    private EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseHelper = new FirebaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);
    }

    public void onSignUp(View view) {
        EditText[] fields = new EditText[5];

        fullName = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.password_check);
        error = findViewById(R.id.error);

        fields[0] = fullName;
        fields[1] = username;
        fields[2] = email;
        fields[3] = password1;
        fields[4] = password2;

        String password_entry = password1.getText().toString();
        String password_check = password2.getText().toString();

        if (isEmpty(fields)) {
            error.setText("Please fill out all fields.");
        } else {
            if (!password_check.equals(password_entry)) {
                error.setText("Passwords do not match, please try again.");
                password1.getText().clear();
                password2.getText().clear();
            } else {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("users").child(this.username.getText().toString().toLowerCase())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    error.setText("Username is already taken. Please choose something else");
                                } else {
                                    firebaseSignUp(email.getText().toString(), password1.getText().toString(),
                                            username.getText().toString(), fullName.getText().toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


            }
        }
    }

    public void onBack(View view) {
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
    }


    public boolean isEmpty(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().toString().length() == 0) {
                return true;
            }
        }
        return false;
    }

    private void firebaseSignUp(final String email, final String password, final String username, final String fullName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("test", "created new user");
                            User user = new User(username, email.toLowerCase(), fullName);
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                            myRef.child("users").child(username.toLowerCase()).setValue(user);
                            Intent intent = new Intent(SignUp.this, LocationInfo.class);
                            startActivity(intent);
                        } else {
                            Log.w("test", "did not create new user", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                error.setText("Password too weak - must be at least six characters");

                            } catch (FirebaseAuthInvalidCredentialsException e) {

                                if (e.getMessage().equals("The email address is badly formatted.")) {
                                    error.setText("Email is not formatted properly");
                                }
                            } catch (FirebaseAuthUserCollisionException e) {
                                error.setText("Email address already in use");
                            } catch (Exception e) {

                            }
                        }
                    }
                });
    }
}
