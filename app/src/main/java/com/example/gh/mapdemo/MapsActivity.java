package com.example.gh.mapdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //private GoogleMap mMap;
    GoogleMap mMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    TextView latitude,longitude;
    double lat=0.0,log=0.0;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        GeoFire geoFire=new GeoFire(databaseReference);
        geoFire.getLocation("firebase-hq", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    Toast.makeText(MapsActivity.this,"The location for key %s is [%f,%f]",Toast.LENGTH_LONG).show();
                   // System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                    lat=location.latitude;
                    log=location.longitude;

                } else {
                    Toast.makeText(MapsActivity.this,"There is no location for key %s in GeoFire",Toast.LENGTH_LONG).show();
                   // System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this,"There was an error getting the GeoFire location:",Toast.LENGTH_LONG).show();
                //System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        LatLng sydney = new LatLng(lat,log);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
