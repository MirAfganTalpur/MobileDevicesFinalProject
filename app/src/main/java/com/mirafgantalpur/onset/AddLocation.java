package com.mirafgantalpur.onset;

import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class AddLocation extends FragmentActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    Context mContext;
    private GoogleMap mMap;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        mContext = this;
        FragmentManager fragManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListenerGPS);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="Location Updated!";
            Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
            findLocation(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void findLocation(double latitude, double longitude){
        LatLng newLocation = new LatLng(latitude, longitude);


        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this,
                    Locale.getDefault());
            try {
                List<Address> ls = geocoder.getFromLocation(latitude, longitude, 1);
                for (Address addr: ls) {
                    String address = addr.getAddressLine(0);

                    String city = addr.getLocality();

                    String prov = addr.getAdminArea();

                    String country = addr.getCountryName();

                    String postalCode = addr.getPostalCode();

                    String phone = addr.getPhone();

                    String url = addr.getUrl();
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(newLocation).title("Marker:"+address).snippet(city + ", " + prov + ", " + country + ", " + postalCode + ", " + phone + ", " + url));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}