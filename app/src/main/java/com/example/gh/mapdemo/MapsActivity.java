package com.example.gh.mapdemo;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    //private GoogleMap mMap;
    GoogleMap mMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    TextView latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       /* if (googleServicesAvailable()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {

        }
    }

   /* private boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannoy Connect To Play Services", Toast.LENGTH_LONG).show();
        }
        return false;
    }*/

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }else{
            if(!mMap.isMyLocationEnabled()){
                mMap.setMyLocationEnabled(true);
                LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Location mylocation=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(mylocation==null){
                    Criteria criteria=new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                    String provider=lm.getBestProvider(criteria,true);
                    mylocation=lm.getLastKnownLocation(provider);
                }
                if (mylocation!=null){
                    LatLng userLocation=new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,mMap.getMaxZoomLevel()-5));
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Umair Ali Don"));
                    mMap.setIndoorEnabled(true);
                    mMap.setMapType(googleMap.MAP_TYPE_NORMAL);
                }
            }*/
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        Button btn_find = (Button) findViewById(R.id.btn_find);
        latitude=(TextView) findViewById(R.id.latitude);
        longitude=(TextView)findViewById(R.id.longitude);

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etLocation = (EditText) findViewById(R.id.et_location);

                // Getting user input location
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        });
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            mMap.clear();

            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                int lat= (int) address.getLatitude();
                int lon= (int) address.getLongitude();
                latitude.setText(String.valueOf(lat));
                longitude.setText(String.valueOf(lon));
//                Toast.makeText(MapsActivity.this,lat,Toast.LENGTH_LONG).show();
             //   Toast.makeText(MapsActivity.this,lon,Toast.LENGTH_LONG).show();
                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                mMap.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    }
}
