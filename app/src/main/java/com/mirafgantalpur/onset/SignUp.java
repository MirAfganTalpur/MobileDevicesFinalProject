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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);
    }

    public void onSignUp(View view) {
        Intent intent = new Intent(SignUp.this, LocationInfo.class);
        startActivity(intent);
    public void onSignUp(View view)
    {
        EditText [] fields = new EditText[5];

        EditText full_name = findViewById(R.id.full_name);
        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.email);
        EditText password1 = findViewById(R.id.password);
        EditText password2 = findViewById(R.id.password_check);
        TextView error = findViewById(R.id.error);

        fields[0] = full_name;
        fields[1] = username;
        fields[2] = email;
        fields[3] = password1;
        fields[4] = password2;

        String password_entry = password1.getText().toString();
        String password_check = password2.getText().toString();

        if (isEmpty(fields)) {
            error.setText("Please fill out all fields.");
        } else {
            if (password_check!=password_entry) {
                error.setText("Passwords do not match, please try again.");
                password1.getText().clear();
                password2.getText().clear();
            } else {
                Intent intent = new Intent(SignUp.this, LocationInfo.class);
                startActivity(intent);
            }
        }
    }

    public void onBack(View view) {
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
    }

    private void firebaseSignUp(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("test", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("test", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public boolean isEmpty (EditText[] fields){
        for (int i = 0; i < fields.length; i++ ){
            if (fields[i].getText().toString().length() == 0) {
                return true;
            }
        }
        return false;
    }
}
