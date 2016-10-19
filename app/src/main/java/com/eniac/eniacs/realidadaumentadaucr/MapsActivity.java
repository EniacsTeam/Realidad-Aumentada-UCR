package com.eniac.eniacs.realidadaumentadaucr;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import static android.os.Build.VERSION_CODES.M;
import static com.eniac.eniacs.realidadaumentadaucr.R.id.map;
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Location mCurrentLocation;
    private static final String TAG = "MapsActivity";
    private Rutas mRuta;
    String [] StringPermisos = {android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int iconVec[] = new int[28];
    private String wordVec[] = {"derecho","oficbecas","biblio","arqui","comedor","inge","fisicamate","generales","biblio","preescolar",
    "letras","centinform","geologia","economicas","ecci","odonto","medicina","farmacia","microbiologia","biolo","quimica","musica",
    "artes","educa","bosque","mariposario","plaza","plaza"};
    private Marker marcas[] = new Marker[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mRuta = new Rutas();
        llenarIconVec();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //requestPermission();
        createLocationRequest();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MapsActivity.this ,WikitudeActivity.class));

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });
    }

    private void expand(){


    }


    /**
     * Conecta la aplicación a los servicios
     * de google
     */
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }


    /**
     * Detiene la conexión a los servicios
     * de google
     */
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * Construye el googleApiClient para poder empezar a
     * utilizarlo
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks( this)
                .addOnConnectionFailedListener( this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    /**
     * Permite localizar la posición del usuario cada segundo
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = MapsActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
      //  addMarkers();
    }


    /**
     * Método para preguntar permisos
     */
    private void requestPermission(){
        //Preguntar por permiso
        ActivityCompat.requestPermissions(this, StringPermisos, 0);
    }


    /**
     * Verifica que tenga los permisos apropiados
     * para acceder a la ubicación de usuario
     * <p>
     *
     * @param  requestCode  codigo del permiso
     * @param  permissions  los permisos que se solicitan
     * @param  grantResults  indica si permiso es concedido o no
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //se crea bien
                }else{
                    Toast.makeText(this,"Need your location", Toast.LENGTH_SHORT).show();
                }
              break;

        }

    }


    /**
     * Llena el vector que contiene el id de cada ícono
     * que se utiliza para usar de marcador
     */
    public void llenarIconVec()
    {
        for(int i=0; i<28;++i)
        {
            iconVec[i] = getResources().getIdentifier(wordVec[i], "drawable", getPackageName());
        }
    }


    /**
     * Agrega los marcadores a los 3 edificios más cercanos
     */
    public void addMarkers() {
        int indice;
        int i = 0;
        Map<Integer, Location> res = mRuta.edificiosMasCercanos(mCurrentLocation);
        for (Map.Entry<Integer, Location> entry : res.entrySet()) {
            indice = entry.getKey();
            LatLng pos = new LatLng(entry.getValue().getLatitude(), entry.getValue().getLongitude());
            marcas[i] = mMap.addMarker(new MarkerOptions().position(pos).title(mRuta.edificios[indice]).icon(BitmapDescriptorFactory.fromResource(iconVec[indice])));
            ++i;
        }
        res.clear();
    }


    /**
     * Actualiza posición de usuario y actualiza
     * vista de usuario
     * <p>
     *
     * @param  bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = MapsActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED)
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            mCurrentLocation = mLastLocation;
            if (mLastLocation != null) {
                LatLng posicion = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion,18));
            }else{
                Toast.makeText(this, "Se cayó en el onConnected",Toast.LENGTH_LONG).show();
            }
        }

        startLocationUpdates();
    }


    /**
     * Permite iniciar el rastreo de la posición
     * del usuario
     */
    protected void startLocationUpdates() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = MapsActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

    }


    /**
     * Permite re conectarse a los servicios de google
     * en caso de perder conexión
     * <p>
     * @param  i
     */
    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    /**
     * Permite identificar si la conexión a los servicios
     * de google falló
     * <p>
     *
     * @param  connectionResult  resultado de la conexión
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    /**
     * Actualiza la vista cada vez que se cambia
     * la posición del usuario
     * <p>
     *
     * @param  location  ubicación del usuario
     */
    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        mCurrentLocation = location;
        addMarkers();
        //Location apuntado= mRuta.edificioApuntado();//enviar el angulo como parametro




    }

    /**
     * Método para pausar la actividad
     */
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    /**
     * Método para detener la actividad
     */
    protected void stopLocationUpdates() {

       if(mGoogleApiClient != null) {
           LocationServices.FusedLocationApi.removeLocationUpdates(
                   mGoogleApiClient, this);
       }
    }


    /**
     * Método para resumir la actividad
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }


}

