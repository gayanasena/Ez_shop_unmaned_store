package com.example.ezstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocateActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng Ez_Maill_kurunegala = new LatLng(7.476664, 80.355995);
        googleMap.addMarker(new MarkerOptions().position(Ez_Maill_kurunegala).title("Ez Maill kurunegala"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Ez_Maill_kurunegala));

        LatLng Ez_Maill_kandy = new LatLng(7.297219, 80.636512);
        googleMap.addMarker(new MarkerOptions().position(Ez_Maill_kandy).title("Ez Maill kandy"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Ez_Maill_kandy));
    }
}