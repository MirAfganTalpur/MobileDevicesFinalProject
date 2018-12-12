package com.mirafgantalpur.onset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Edit_Location extends AppCompatActivity {

    private String username;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__location);

        // get username and location from LocationInfo
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        location = (Location)intent.getSerializableExtra("location");

        EditText name = findViewById(R.id.edit_nameET);
        name.setText(location.getType());

        EditText type = findViewById(R.id.edit_typeET);
        type.setText(location.getType());

        EditText addr = findViewById(R.id.edit_addrET);
        addr.setText(location.getAddress());

        EditText privPub = findViewById(R.id.edit_privPubET);
        if(location.isPrivate()) {
            privPub.setText("PRIVATE");
        } else {
            privPub.setText("PUBLIC");
        }

        EditText filmPerm = findViewById(R.id.edit_filmPermET);
        filmPerm.setText(location.getFilmPermissions());

        EditText feat = findViewById(R.id.edit_featET);
        feat.setText(location.getFeatures());

//        FirebaseHelper.updateLocation();

    }

    public void editBack(View view) {
        finish();
    }
}
