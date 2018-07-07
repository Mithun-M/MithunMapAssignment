package com.example.mithun.mithunmapassignment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Toast.makeText(MapsActivity.this, "Latitude = " + latitude, Toast.LENGTH_SHORT).show();
            Toast.makeText(MapsActivity.this, "Longitude = " + longitude, Toast.LENGTH_SHORT).show();
            //Reverse Geo coding
            Geocoder geocoder = new Geocoder(MapsActivity.this);
            StringBuilder stringBuilder = new StringBuilder();
            try {
                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 10);
                Address address = addressList.get(0);
                String address_line1 = address.getAddressLine(0);
                String country = address.getCountryName();
                String locality = address.getLocality();
                String zipCode = address.getPostalCode();
                String premises = address.getPremises();
                stringBuilder.append(address_line1 + "\n" + country + "\n" + locality + "\n" + zipCode + "\n" + premises);
                Toast.makeText(MapsActivity.this, "ADDRESS: " + stringBuilder.toString(), Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MapsActivity.this, "Poor Internet. Try again..", Toast.LENGTH_SHORT).show();
            }
            //end
//            Display marker
            LatLng sydney = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title(stringBuilder.toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomBy(16));
            Toast.makeText(MapsActivity.this, "ADDRESS: " + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            //17 is max zoom
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MapsActivity.this, "No satellite", Toast.LENGTH_SHORT).show();
            Toast.makeText(MapsActivity.this, "No cell tower, No wifi", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MapsActivity.this, "Wait..fetching", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MapsActivity.this, "GPS is off, Please turn on", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "USER DENIED PERMISSION", Toast.LENGTH_SHORT).show();
            return;

        }
        mMap.setMyLocationEnabled(true);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, locationListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
