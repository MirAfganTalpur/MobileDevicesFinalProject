package com.mirafgantalpur.onset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class EditLocation extends AppCompatActivity {

    private EditText name, type, address, filmPerm, feat;

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
        location = (Location) intent.getSerializableExtra("location");


        name = findViewById(R.id.edit_name_ET);
        name.setText(location.getName());

        type = findViewById(R.id.edit_type_ET);
        type.setText(location.getType());

        address = findViewById(R.id.edit_address_ET);
        address.setText(location.getAddress());
        address.setFocusable(false);

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
        location.setAddress(address.getText().toString());
        location.setFilmPermissions(filmPerm.getText().toString());
        location.setFeatures(feat.getText().toString());
        location.setPrivate(isPrivate);
        location.setOnlyForMe(isOnlyForMe);
        location.setYoutubeLinks(getIntent().getStringArrayListExtra("youtubeLinks"));

        if (!isValidInput()) {
            return;
        }
        FirebaseHelper.updateLocation(username, location);

        Intent intent = new Intent(this, LocationList.class);
        intent.putExtra("username", username);
        intent.putExtra("choice", getIntent().getStringExtra("choice"));
        startActivity(intent);

    }

    private boolean isValidInput() {
        EditText locationName = findViewById(R.id.edit_name_ET);
        EditText locationType = findViewById(R.id.edit_type_ET);
        EditText permissions = findViewById(R.id.edit_permissions_ET);
        EditText features = findViewById(R.id.edit_features_ET);
        RadioButton isPrivate = findViewById(R.id.is_private_button);
        RadioButton isPublic = findViewById(R.id.is_public_button);
        RadioButton notOnlyForMe = findViewById(R.id.only_me_false);
        RadioButton isOnlyForMe = findViewById(R.id.only_me_true);

        EditText[] editTexts = new EditText[]{locationName, locationType, permissions, features};
        for (EditText editText : editTexts) {
            if (editText.getText().toString().length() == 0) {
                Toast.makeText(this, R.string.please_fill_out_all_fields, Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (!isPrivate.isChecked() && !isPublic.isChecked()) {
            Toast.makeText(this, R.string.select_private_or_public_space,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isOnlyForMe.isChecked() && !notOnlyForMe.isChecked()) {
            Toast.makeText(this, R.string.select_personal_or_shared,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public void editBack(View view) {
        finish();
    }
}
