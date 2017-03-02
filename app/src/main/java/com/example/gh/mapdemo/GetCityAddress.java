package com.example.gh.mapdemo;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetCityAddress extends AppCompatActivity {
    Button addressButton,btnMap;
    TextView addressTV;
    TextView latLongTV,latitu;
    String lt,lg;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    private static final String TAG = "GeocodingLocation";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_city_address);
        addressTV = (TextView) findViewById(R.id.addressTV);
        latLongTV = (TextView) findViewById(R.id.latLongTV);
        latitu=(TextView)findViewById(R.id.latitude);
        btnMap=(Button)findViewById(R.id.GetMAp);
        databaseReference=FirebaseDatabase.getInstance().getReference();
        final GeoFire geoFire=new GeoFire(databaseReference);
        addressButton = (Button) findViewById(R.id.addressButton);
        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=(EditText)findViewById(R.id.addressET);
                final String addressgiven = editText.getText().toString();
                            Geocoder geocoder=new Geocoder(getBaseContext(), Locale.getDefault());
                            //String result=null;
                            try {
                                List addressList = geocoder.getFromLocationName(addressgiven, 1);
                                if (addressList != null && addressList.size() > 0) {
                                    Address address = (Address) addressList.get(0);
                                    lt=String.valueOf(address.getLatitude());
                                    lg=String.valueOf(address.getLongitude());
                                    latitu.setText(lt);
                                    latLongTV.setText(lg);
                                    geoFire.setLocation("firebase-hq", new GeoLocation(address.getLatitude(), address.getLongitude())
                                            , new GeoFire.CompletionListener() {
                                                @Override
                                                public void onComplete(String key, DatabaseError error) {
                                                    if (error != null) {
                                                        Toast.makeText(GetCityAddress.this,"There was an error saving the location to GeoFire: ",Toast.LENGTH_LONG).show();
                                                    } else {
                                                        btnMap.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                startActivity(new Intent(GetCityAddress.this,MapsActivity.class));
                                                            }
                                                        });
                                                        Toast.makeText(GetCityAddress.this,"\"Location saved on server successfully!\"",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            } catch (IOException e) {
                                Log.e(TAG, "Unable to connect to Geocoder", e);
                            }
                        }
            });
      }
       }