package com.mirafgantalpur.onset;

import android.content.Context;
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
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    protected TextView error;
    private EditText fullName;
    private EditText username;
    private EditText email;
    private EditText password1;
    private EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onSignUp(View view) {
        EditText[] fields = new EditText[5];

        fullName = findViewById(R.id.full_name);
        username = findViewById(R.id.loginUsername);
        email = findViewById(R.id.email);
        password1 = findViewById(R.id.loginPassword);
        password2 = findViewById(R.id.password_check);
        error = findViewById(R.id.error);
        String failedLogin = "Login Failed.";
        Context mContext = this;

        fields[0] = fullName;
        fields[1] = username;
        fields[2] = email;
        fields[3] = password1;
        fields[4] = password2;

        String password_entry = password1.getText().toString();
        String password_check = password2.getText().toString();

        if (isEmpty(fields)) {
            error.setText(R.string.please_fill_out_all_fields);
            Toast.makeText(mContext, failedLogin, Toast.LENGTH_LONG).show();
        } else {
            if (!password_check.equals(password_entry)) {
                Toast.makeText(mContext, failedLogin, Toast.LENGTH_LONG).show();
                password1.getText().clear();
                password2.getText().clear();
            } else {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("users").child(this.username.getText().toString().toLowerCase())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    error.setText(R.string.username_taken);
                                } else {
//                                    firebaseSignUp(email.getText().toString(), password1.getText().toString(),
//                                            username.getText().toString(), fullName.getText().toString());
                                    FirebaseHelper.signUp(email.getText().toString(), password1.getText().toString(),
                                            username.getText().toString(), fullName.getText().toString(), SignUp.this);
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
}
