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

    EditText editText_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        // Spinner Set up
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


        // For now manually enter arraylist:
        locationList.add(new Location("UOIT", "Campus", "2000 Simcoe St. N", "Oshawa", "Canada","Everyday","Bus Loop, Reflecting Pool, Field", true, false));
        locationList.add(new Location("UOIT", "Campus", "2000 Simcoe St. N", "Oshawa", "Canada","Everyday","Bus Loop, Reflecting Pool, Field", true, false));
        locationList.add(new Location("UOIT", "Campus", "2000 Simcoe St. N", "Oshawa", "Canada","Everyday","Bus Loop, Reflecting Pool, Field", true, false));

        locationListView = findViewById(R.id.locationLV);

        locationAdapter = new LocationAdapter(this, locationList);
        locationListView.setAdapter(locationAdapter);
    }

    public void search(View view) {
        editText_key = findViewById(R.id.editText_key);
        keyEntered = editText_key.getText().toString();
    }

    public void addLocation(View view) {
        Intent intent = new Intent(this, AddLocation.class);
        startActivity(intent);
    }
}
