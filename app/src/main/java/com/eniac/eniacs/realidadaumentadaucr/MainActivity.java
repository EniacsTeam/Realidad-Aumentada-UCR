package com.eniac.eniacs.realidadaumentadaucr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.StartupConfiguration;
import android.Manifest;
import android.view.Window;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import static android.os.Build.VERSION_CODES.M;
import static com.eniac.eniacs.realidadaumentadaucr.R.id.map;
import static com.wikitude.architect.CameraPreviewBase.m;

/**
 * Created by Francisco on 10/1/2016.
 */

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArchitectView architectView;
    String [] StringPermisos = {Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wikimaps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapC);
        mapFragment.getMapAsync(this);

        this.architectView = (ArchitectView)this.findViewById( R.id.architectViewC );
        /*Clave de licencia Wikitude*/
        final StartupConfiguration config = new StartupConfiguration("E2+3OVGKbQcv6JrSgriX+czMlLGUGf9lfex/+wxB6b1Tbo7RyIoccealOwt4Kl1mAf5QBel8wQ818kRAeJfbHB/K5XO12aC0cptyGTO8NEDypO87dn19fTA3Hx7ULASgd4zhwStTH42bsGIEMsL4SPUNd2ucuE4R15y+4kJzQFNTYWx0ZWRfX3rT9ifimN5EoNAvY/2QDMkmhwD57VrGVyq6Y8lgwAQTwocP+1IW4choWR9mq3J7yJI2wMozzbp8kB2v7thM71zjTV42qnP6WdY13rCm3Vj5EuqCRdYOTNgPlUoLwSgTVIBSDwD9Jh984S0Zlr9TDw8Yn5il6OhlLYTug9dYV2PMQzt/uQ9ukisOGt0B49iEjJn1flOmx5PCjxo8+Vcm8xtM/1Nen79Ifrf63itCHjDZaPT37qW0Tm52KZJiGo9CoeMkDfWSGuv1hrZ0sABIFciRROmaF+tf//5C+FQAt8eE6TJzcCsenx9qwyMNAgdD+RORiZqF9mnzJaIUOvBB4TrzxmU2bAVgwd5TgYylm+itjUigNNfnD8z2i/cF0w7Z77W+ZUfEgfSEPfOLCrXg16PnHXrLxKw7FK2LR6txrAPmcKrHqUAUikFBWrwTqMK3nFPti0ksA2kYpRiCOklybe74Q6jvq3OXyzb8tSEDXXeC1RJw3oiUOiA=");
        this.architectView.onCreate( config );
        boolean check = askCompatibility();
        if(check == true)
        {
            requestPermission();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this ,MapsActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        //Disable Map Toolbar:
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.biolo)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Carga la carpeta donde se tienen los archivos de imagenes para
     * la Realidad Aumentada.
     * <p>
     *
     * @param  savedInstanceState  instancia del estado de la vista
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        architectView.onPostCreate();

        try {
            this.architectView.load("");/*Hay que poner ruta de directorio aqui*/
        } catch (Exception e){

        }
    }

    /**
     * Método para reanudar la actividad
     */
    @Override
    protected void onResume(){
        super.onResume();
        architectView.onResume();
    }

    /**
     * Método para destruir la actividad
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        architectView.onDestroy();
    }

    /**
     * Método para pausar la actividad
     */
    @Override
    protected void onPause(){
        super.onPause();
        architectView.onPause();
    }

    /**
     * Comprueba que los permisos fueron aprobados. Si este
     * no es el caso, despliega un mensaje al usuario.
     *
     * @param  requestCode  codigo de solicitud
     * @param permissions los permisos solicitados
     * @param grantResults permiso si es concedido o denegado
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Tiene permiso
                } else {
                    Toast.makeText(this, "Por favor conceda los permisos necesarios", Toast.LENGTH_SHORT).show();
                }
        }
    }

    /**
     * Verifica que la version de android sea M o mayor para
     * preguntar por permisos.
     *
     * @return   true/false  que indica si se puede preguntar por permisos
     */
    private boolean askCompatibility(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }
    /**
     * Método para preguntar permisos
     */
    private void requestPermission(){
        //Preguntar por permiso
        ActivityCompat.requestPermissions(this, StringPermisos, 0);
    }
}
