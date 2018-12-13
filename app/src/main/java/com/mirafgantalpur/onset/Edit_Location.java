package com.mirafgantalpur.onset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class Edit_Location extends AppCompatActivity {

    private EditText name, type, addr, filmPerm, feat;

    private String username;
    private Location location;
    private boolean isPrivate;
    private boolean isOnlyForMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__location);

        // Hides the soft keyboard auto show when activity starts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // get username and location from LocationInfo
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        location = (Location)intent.getSerializableExtra("location");

        name = findViewById(R.id.edit_name_ET);
        name.setText(location.getType());

        type = findViewById(R.id.edit_type_ET);
        type.setText(location.getType());

        addr = findViewById(R.id.edit_address_ET);
        addr.setText(location.getAddress());

        filmPerm = findViewById(R.id.edit_permissions_ET);
        filmPerm.setText(location.getFilmPermissions());

        feat = findViewById(R.id.edit_features_ET);
        feat.setText(location.getFeatures());

    }

    public void onPrivateClicked(View view) {
        isPrivate = true;
    }

    public void onPublicClicked(View view) {
        isPrivate = false;
    }

    public void onForMeClicked(View view) {
        isOnlyForMe = true;
    }

    public void onSharedClicked(View view) {
        isOnlyForMe = false;
    }

    public void submitUpdate(View view) {

        location.setName(name.getText().toString());
        location.setType(type.getText().toString());
        location.setAddress(addr.getText().toString());
        location.setFilmPermissions(filmPerm.getText().toString());
        location.setFeatures(feat.getText().toString());
        location.setPrivate(isPrivate);
        location.setOnlyForMe(isOnlyForMe);

        FirebaseHelper.updateLocation(username, location);

        finish();

    }

    public void editBack(View view) {
        finish();
    }
}
