package com.example.gh.mapdemo;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class GetCityAddress extends AppCompatActivity {

    EditText startPoint;
    TextView latitude,longitude;
    Button cnvert;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_city_address);
        startPoint=(EditText) findViewById(R.id.satrtDate);
        address=startPoint.getText().toString();
        latitude=(TextView) findViewById(R.id.latitude);
        longitude=(TextView)findViewById(R.id.longitude);
        cnvert=(Button)findViewById(R.id.convert);
        cnvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder coder=new Geocoder(GetCityAddress.this);
                List<Address> address1;
                try {
                    address1=coder.getFromLocationName(address,1);
                    if (address1.size()==0){
                        Toast.makeText(GetCityAddress.this,"Address is not Found",Toast.LENGTH_LONG);
                    }else if(address1.size()>0)
                    {
                    Address location=address1.get(0);
                    double lat=  location.getLatitude();
                    double lon=  location.getLongitude();
                    latitude.setText(String.valueOf(lat));
                    longitude.setText(String.valueOf(lon));
                    }
                } catch (Exception e) {
                    Toast.makeText(GetCityAddress.this,"Address is not valid",Toast.LENGTH_LONG);
                }
            }
        });
    }
}
