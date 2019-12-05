package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;

/**
 * A demo class that stores and retrieves data objects with each marker.
 */
public class MapaServico extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    Button addLocalServico;
    String localServico;

    private static final LatLng CHALE = new LatLng(-4.971568, -39.014376);
    private static final LatLng UECE = new LatLng(-4.968680, -39.024433);
    private static final LatLng DISK = new LatLng(-4.970332, -39.020900);
    private static final LatLng HOSPITAL = new LatLng(-4.966866, -39.024646);
    private static final LatLng UFC = new LatLng(-4.979365, -39.056408);



    private Marker mChale;
    private Marker mUece;
    private Marker mDisk;
    private Marker mHospital;
    private Marker mUfc;

    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String TAG = "MapaServico";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_servico);


        addLocalServico = findViewById(R.id.addLocalServico);
        addLocalServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getExtras().getString("required").equals("servicos"))
                    Servicos.localServico.setText(localServico);
                else
                    Carrinho.localCompra.setText(localServico);
                finish();
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaServico);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "1 - getLocationPermission: ");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            Task location = mFusedLocationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Log.e(TAG, "1 - onComplete: found location");
                    Location currentLocation = (Location) task.getResult();
                    moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f);

                }
            });

        }catch (SecurityException e){
            Log.e(TAG, "1 - getLocationPermission: Segurity" + e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom){
        Log.e(TAG,"1 - MoveCamera: movendo a camera");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom ));
    }

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        getLocationPermission();
        mMap.setMyLocationEnabled(true);

        // Add some markers to the map, and add a data object to each marker.
        mChale = mMap.addMarker(new MarkerOptions()
                .position(CHALE)
                .title("Praça do Chalé"));
        mChale.setTag(0);

        mUece = mMap.addMarker(new MarkerOptions()
                .position(UECE)
                .title("Universidade do Ceará"));
        mUece.setTag(0);

        mDisk = mMap.addMarker(new MarkerOptions()
                .position(DISK)
                .title("Disk pão de quixada"));
        mDisk.setTag(0);

        mHospital = mMap.addMarker(new MarkerOptions()
                .position(HOSPITAL)
                .title("Hospital Central"));
        mHospital.setTag(0);

        mUfc = mMap.addMarker(new MarkerOptions()
                .position(UFC)
                .title("Universidade Federal do Ceará"));
        mUfc.setTag(0);

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() + " foi selecionado",
                    Toast.LENGTH_SHORT).show();
            localServico = marker.getTitle();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}