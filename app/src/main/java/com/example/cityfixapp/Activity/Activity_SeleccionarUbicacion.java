package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.cityfixapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Activity_SeleccionarUbicacion extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng ubicacionSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_ubicacion);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Ubicación inicial
        LatLng inicial = new LatLng(40.4168, -3.7038); // Madrid por defecto
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicial, 15));

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            ubicacionSeleccionada = latLng;
            mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));

            // Devolver ubicación cuando se toca
            Intent resultIntent = new Intent();
            resultIntent.putExtra("lat", latLng.latitude);
            resultIntent.putExtra("lng", latLng.longitude);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
