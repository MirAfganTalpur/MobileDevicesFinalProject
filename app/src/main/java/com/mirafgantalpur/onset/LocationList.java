package com.mirafgantalpur.onset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        keySpinner = findViewById(R.id.keySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.keyList, android.R.layout.simple_spinner_item);
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
    }

    public void addLocation(View view) {
        Intent intent = new Intent(this, AddLocation.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void updateUI(ArrayList<Location> locations) {
        locationListView = findViewById(R.id.locationLV);

        locationAdapter = new LocationAdapter(this, locations);
        locationListView.setAdapter(locationAdapter);

    }
}
