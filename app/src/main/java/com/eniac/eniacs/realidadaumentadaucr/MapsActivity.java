package com.eniac.eniacs.realidadaumentadaucr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.Map;

import static com.eniac.eniacs.realidadaumentadaucr.R.id.fab;
import static com.eniac.eniacs.realidadaumentadaucr.R.id.map;
import static com.wikitude.architect.CameraPreviewBase.m;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Rutas mRuta;
    Location mCurrentLocation;
    private int iconVec[] = new int[28];
    private String wordVec[] = {"derecho","oficBecas","biblio","arqui","comedor","inge","fisicamate","generales","biblio","preescolar",
    "letras","centInform","geologia","economicas","ECCI","odonto","medicina","farmacia","microbiologia","biolo","quimica","musica",
    "artes","educa","bosque","mariposario","plaza","plaza"};
    private Marker marcas[] = new Marker[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        llenarIconVec();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(MapsActivity.this ,WikitudeActivity.class));

            }
        });
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
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.biolo)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void llenarIconVec()
    {
        for(int i=0; i<28;++i)
        {
            iconVec[i] = getResources().getIdentifier(wordVec[i], "drawable", getPackageName());
        }
    }

    public void addMarkers()
    {
        int indice;
        int i=0;
        Map<Integer,Location> res = mRuta.edificiosMasCercanos(mCurrentLocation);
        for (Map.Entry<Integer,Location> entry : res.entrySet())
        {
            indice = entry.getKey();
            LatLng pos = new LatLng(entry.getValue().getLatitude(), entry.getValue().getLongitude());
            marcas[i] = mMap.addMarker(new MarkerOptions().position(pos).title(mRuta.edificios[indice]).icon(BitmapDescriptorFactory.fromResource(iconVec[indice])));
            ++i;
            /*else //Si es indice
            {
                // Read your drawable from somewhere
                Drawable dr = ResourcesCompat.getDrawable(getResources(),iconVec[indice],null);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                // Scale it to 50 x 50
                Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 160, 160, false);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Prueba").icon(BitmapDescriptorFactory.fromBitmap(bitmapResized))); //Cambiar titulo
            }*/

        }
    }

}

