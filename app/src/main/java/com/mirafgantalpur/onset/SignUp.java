package com.mirafgantalpur.onset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
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
            if (!password_check.equals(password_entry)) {
                error.setText("Passwords do not match, please try again.");
                password1.getText().clear();
                password2.getText().clear();
            } else {
                Intent intent = new Intent(SignUp.this, LocationInfo.class);
                startActivity(intent);
            }
        }
    }

    public void onBack(View view)
    {
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
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
