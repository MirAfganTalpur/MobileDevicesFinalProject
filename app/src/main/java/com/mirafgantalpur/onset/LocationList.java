package com.mirafgantalpur.onset;

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

    EditText editText_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        String userChoice = (String) getIntent().getExtras().get("choice");
        if (userChoice.equals("myLocations")) {
            FirebaseHelper.getAllUserLocations("zxc", this);
        } else if (userChoice.equals("sharedLocations")) {
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

        // Using firebase: obtain an arraylist for all locations in the database..



    }

    public void search(View view) {
        editText_key = findViewById(R.id.editText_key);
        keyEntered = editText_key.getText().toString();
        String keyType = keySpinner.getSelectedItem().toString();
        switch (keyType) {
            case "name": {
                Log.e("test", "name selected");
                break;
            }
            case "type": {
                Log.e("test", "type selected");
                break;
            }
            case "Address":
            case "City":
            case "Country":
            case "Filming Permissions":
            case "Features":
            case "Private or Public":
            case "Only for me or For Everyone":
            default: {
                Log.e("test", "nothing selected");
            }
        }
    }

    public void addLocation(View view) {
        Intent intent = new Intent(this, AddLocation.class);
        startActivity(intent);
    }

    public void updateUI(ArrayList<Location> locations) {
        locationListView = findViewById(R.id.locationLV);

        locationAdapter = new LocationAdapter(this, locations);
        locationListView.setAdapter(locationAdapter);

    }
}
