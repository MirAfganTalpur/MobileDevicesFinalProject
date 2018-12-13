package com.mirafgantalpur.onset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        userActivityChoice = (String) getIntent().getExtras().get("choice");
        username = getIntent().getStringExtra("username");

        if (userActivityChoice.equals("myLocations")) {
            FirebaseHelper.getAllUserLocations(username, this);
        } else if (userActivityChoice.equals("sharedLocations")) {
            FirebaseHelper.getAllSharedLocations(this);
        }

        setupSpinner(this);

    }

    public void setupSpinner(Context context) {
        keySpinner = findViewById(R.id.keySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.keyList, android.R.layout.simple_spinner_item);
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

    public void search(View view) {
        editText_key = findViewById(R.id.editText_key);
        keyEntered = editText_key.getText().toString();
        String keyType = keySpinner.getSelectedItem().toString();
        Boolean isViewingSharedLocations = getIntent().getStringExtra("choice").equals("sharedLocations");
        // TODO fix UI so we can see the key type
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
                Log.e("test", "nothing selected");
                //TODO change spinner font colour
            }
        }
    }

    public void addLocation(View view) {
        Intent intent = new Intent(this, AddLocation.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void updateUI(final ArrayList<Location> locations) {
        locationListView = (ListView)findViewById(R.id.locationLV);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LocationList.this, LocationInfo.class);
                Location location = locations.get(position);
                intent.putExtra("username", username);
                intent.putExtra("selectedLocation",location);
                startActivity(intent);
            }
        });
        locationAdapter = new LocationAdapter(this, locations);
        locationListView.setAdapter(locationAdapter);

    }
}
