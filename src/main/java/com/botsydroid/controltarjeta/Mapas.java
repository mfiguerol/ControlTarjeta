package com.botsydroid.controltarjeta;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;

public class Mapas extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(getApplicationContext(), "Aqui...",
                Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        double latitud=0,longitud=0;

        mMap = googleMap;

        LatLng inicial = new LatLng(latitud,longitud);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(inicial).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(inicial));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    public void busqueda(View view)
    {
        EditText usuario=(EditText)findViewById(R.id.Direccion);
        String locacion=(String)usuario.getText().toString();
        List<android.location.Address> lista_direcciones = null;
        if(locacion!=null ||!locacion.equals(""))
        {
            Geocoder ubica=new Geocoder(this);
            try {
                lista_direcciones=ubica.getFromLocationName(locacion,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            android.location.Address direc=lista_direcciones.get(0);
            LatLng punto=new LatLng(direc.getLatitude(),direc.getLongitude());
            mMap.addMarker(new MarkerOptions().position(punto).title("Marker"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
        }
    }


}
