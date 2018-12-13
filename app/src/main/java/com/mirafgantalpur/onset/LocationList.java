package com.mirafgantalpur.onset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class LocationList extends AppCompatActivity {
    private String keyTypeSelected;
    private String keyEntered;
    private ArrayList<Location> locationList = new ArrayList<Location>();
    private LocationAdapter locationAdapter;
    private ListView locationListView;
    private Spinner keySpinner;
    private String username;
    private String userActivityChoice;
    EditText editText_key;
    private boolean userChoseMyLocations;
    TextView userViewOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        userViewOption = findViewById(R.id.user_view_option);

        // hide the soft keyboard auto show when activity starts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        userActivityChoice = (String) getIntent().getStringExtra("choice");
        username = getIntent().getStringExtra("username");

        // depending on the choice made on the previous page, return the appropriate list of
        // locaitons
        if (userActivityChoice.equals("myLocations")) {
            FirebaseHelper.getAllUserLocations(username, this);
            userChoseMyLocations = true;
            userViewOption.setText(R.string.choice_my_locations);
        } else if (userActivityChoice.equals("sharedLocations")) {
            FirebaseHelper.getAllSharedLocations(this);
            userChoseMyLocations = false;
            userViewOption.setText(R.string.choice_shared_locations);
        }
        setupSpinner(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (userChoseMyLocations){
            FirebaseHelper.getAllUserLocations(username, this);
        } else {
            FirebaseHelper.getAllSharedLocations(this);
        }
    }

    public void setupSpinner(Context context) {
        keySpinner = findViewById(R.id.key_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                                                R.array.keyList,
                                                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keySpinner.setAdapter(adapter);
        keySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                keyTypeSelected = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                keyTypeSelected = null;
            }
        });
    }

    // set up a search based on the category chosen from spinner and the text entered
    public void search(View view) {
        editText_key = findViewById(R.id.edittext_key);
        keyEntered = editText_key.getText().toString();
        String keyType = keySpinner.getSelectedItem().toString();
        Boolean isViewingSharedLocations =
                            getIntent().getStringExtra("choice").equals("sharedLocations");
        switch (keyType.toLowerCase()) {
            case "name": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "name", keyEntered, isViewingSharedLocations, this);
                break;
            }
            case "type": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "type", keyEntered, isViewingSharedLocations, this);
                break;
            }
            case "address": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "address", keyEntered, isViewingSharedLocations, this);
                break;
            }
            case "city": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "city", keyEntered, isViewingSharedLocations, this);
                break;
            }
            case "country": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "country", keyEntered, isViewingSharedLocations, this);
                break;
            }
            case "filming permissions": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "filmPermissions", keyEntered, isViewingSharedLocations, this);
                break;
            }
            case "features": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "features", keyEntered, isViewingSharedLocations, this);
                break;
            }
            case "private or public": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "private", keyEntered, isViewingSharedLocations, this);
                break;
            }
            case "only for me or for everyone": {
                FirebaseHelper.getSearchResults(getIntent().getStringExtra("username"),
                        "onlyForMe", keyEntered, isViewingSharedLocations, this);
                break;
            }
            default: {
                break;
            }
        }
    }

    public void addLocation(View view) {
        Intent intent = new Intent(this, AddLocation.class);
        intent.putExtra("username", username);
        intent.putExtra("choice", getIntent().getStringExtra("choice"));
        startActivity(intent);
    }

    // populate listview with location list and go to detailed activity activity on click
    public void updateUI(final ArrayList<Location> locations) {
        locationListView = (ListView)findViewById(R.id.location_list_view);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LocationList.this, LocationInfo.class);
                Location location = locations.get(position);
                intent.putExtra("username", username);
                intent.putExtra("selectedLocation",location);
                intent.putExtra("choice", getIntent().getStringExtra("choice"));
                startActivity(intent);
            }
        });
        locationAdapter = new LocationAdapter(this, locations);
        locationListView.setAdapter(locationAdapter);

    }
}
