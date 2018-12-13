package com.mirafgantalpur.onset;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddLocation extends FragmentActivity implements OnMapReadyCallback,
                                                        GoogleMap.OnMyLocationButtonClickListener,
                                                        GoogleMap.OnMyLocationClickListener {
    LocationManager locationManager;
    Context mContext;
    private GoogleMap mMap;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    EditText locationName;
    EditText locationAddress;
    EditText locationType;
    EditText filmingPermissions;
    EditText features;
    RadioGroup isPrivate;
    RadioButton privatelyOwned;
    RadioButton publicSpace;
    RadioGroup isForMe;
    RadioButton personalOnly;
    RadioButton shareEveryone;
    EditText youtubeLink;
    Marker marker;
    ArrayList<String> youTubeList = new ArrayList<>();
    private static final String TAG = "AddLocation";

    PlaceAutocompleteFragment placeAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        // Hides the soft keyboard auto show when activity starts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mContext = this;
        locationAddress = findViewById(R.id.location_address_editText);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        placeAutoComplete = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                locationAddress.setText(place.getAddress());
                toAddress(mContext,place.getAddress().toString());            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                25, locationListenerGPS);

        locationAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String address = locationAddress.getText().toString();
                    toAddress(mContext,address);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                25, locationListenerGPS);
        return false;
    }

    public void toAddress (Context context, String inputtedAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        try {
            address = coder.getFromLocationName(inputtedAddress, 5);

            Address location = address.get(0);
            double latitude = location.getLatitude();
            double longitude =location.getLongitude();

            findLocation(latitude,longitude);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="Location Updated!";
            Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
            findLocation(latitude, longitude);
            locationManager.removeUpdates(this);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    public void findLocation(double latitude, double longitude){
        final LatLng newLocation = new LatLng(latitude, longitude);

        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this,
                    Locale.getDefault());
            try {
                List<Address> ls = geocoder.getFromLocation(latitude, longitude, 1);
                for (Address addr: ls) {
                    String address = addr.getAddressLine(0);
                    locationAddress.setText(address);

                    String city = addr.getLocality();

                    String prov = addr.getAdminArea();

                    String country = addr.getCountryName();

                    String postalCode = addr.getPostalCode();

                    String phone = addr.getPhone();

                    String url = addr.getUrl();

                    mMap.clear();

                    marker = mMap.addMarker(new MarkerOptions().position(newLocation).title(
                                            address).snippet(city + ", " + prov + ", " + country +
                                            ", " + postalCode).draggable(true));

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            marker.setPosition(latLng);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 21);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng ));
                            mMap.animateCamera(cameraUpdate);
                            double newLat = latLng.latitude;
                            double newLong = latLng.longitude;
                            findLocation(newLat,newLong);
                        }
                    });

                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker arg0) {}

                        @SuppressWarnings("unchecked")
                        @Override
                        public void onMarkerDragEnd(Marker arg0) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                            double newLong = arg0.getPosition().longitude;
                            double newLat = arg0.getPosition().latitude;
                            findLocation(newLat,newLong);
                        }

                        @Override
                        public void onMarkerDrag(Marker arg0) {}
                    });

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLocation, 15);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                    mMap.animateCamera(cameraUpdate);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void addVideo (){
        youtubeLink = findViewById(R.id.youtube_link_editText);
        if (youtubeLink.getText().toString().length() > 0) {
            youTubeList.add(youtubeLink.getText().toString());
            youtubeLink.getText().clear();
        }
    }
    public void onVideoAdd (View view){
        addVideo();
    }

    public void onSubmitLocation(View view) {
        locationName = findViewById(R.id.location_name_editText);
        locationAddress = findViewById(R.id.location_address_editText);
        locationType = findViewById(R.id.location_type_editText);
        filmingPermissions = findViewById(R.id.filming_permissions_editText);
        features = findViewById(R.id.location_features_editText);
        isPrivate = findViewById(R.id.is_private_group);
        privatelyOwned  = findViewById(R.id.is_private_button);
        publicSpace = findViewById(R.id.is_public_button);
        isForMe = findViewById(R.id.is_only_me_group);
        personalOnly = findViewById(R.id.only_me_true);
        shareEveryone = findViewById(R.id.only_me_false);
        Boolean isPrivate = null;
        Boolean isOnlyMe = null;

        String city = null;
        String country = null;
        LatLng latLng = marker.getPosition();
        List<Address> addresses = null;
        EditText[] fields = new EditText[5];

        fields [0] = locationName;
        fields [1] = locationAddress;
        fields [2] = locationType;
        fields [3] = filmingPermissions;
        fields [4] = features;

        if (isEmpty(fields)){
            Toast.makeText(mContext,R.string.please_fill_out_all_fields, Toast.LENGTH_LONG).show();
        } else {
            if (checkShareable() && checkPrivate()) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(
                            latLng.latitude,
                            latLng.longitude,
                            1);
                    city = addresses.get(0).getLocality();
                    country = addresses.get(0).getCountryName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (privatelyOwned.isChecked()) {
                    isPrivate = true;
                } else if (publicSpace.isChecked()) {
                    isPrivate = false;
                }
                if (personalOnly.isChecked()) {
                    isOnlyMe = true;
                } else if (shareEveryone.isChecked()){
                    isOnlyMe = false;
                }

                addVideo();

                com.mirafgantalpur.onset.Location newlocation =
                        new com.mirafgantalpur.onset.Location(locationName.getText().toString(),
                                locationType.getText().toString(),
                                locationAddress.getText().toString(),
                                city,
                                country,
                                filmingPermissions.getText().toString(),
                                features.getText().toString(),
                                isPrivate,
                                isOnlyMe,
                                UUID.randomUUID());
                newlocation.setYoutubeLinks(youTubeList);

                FirebaseHelper.addLocation("youtube",newlocation);

                Intent intent = new Intent(AddLocation.this, LocationList.class);
                startActivity(intent);
            }
        }
    }

    public boolean isEmpty(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().toString().length() == 0) {
                return true;
            }
        }
        return false;
    }
        public boolean checkPrivate() {
            if (privatelyOwned.isChecked()) {
                return true;
            } else if (publicSpace.isChecked()) {
                return true;
            } else {
                Toast.makeText(this, "Please check either privately owned or public space.",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        }
        public boolean checkShareable() {
            if (personalOnly.isChecked()) {
                return true;
            } else if (shareEveryone.isChecked()) {
                return true;
            } else {
                Toast.makeText(this, "Please check either personal or shared location.",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        }
}