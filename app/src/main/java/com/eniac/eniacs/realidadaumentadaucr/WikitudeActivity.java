package com.eniac.eniacs.realidadaumentadaucr;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.StartupConfiguration;
import android.Manifest;
import java.util.Map;

/**
 * Esta clase representa la camara de Wikitude. Contiene metodos para solicitar y manejar permisos de localizacion, para luego con ellos brindarle
 * al usuario una experiencia de realidad aumentada donde puede observar e interactuar con puntos de interes mediante su dispositivo movil.
 *
 * @author  EniacsTeam
 */
public class WikitudeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private ArchitectView architectView;
    private Rutas mRuta;
    private final String [] StringPermisos = {Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private static final String TAG = "Wikitude";
    private Integer id1,id2,id3;

    /**
     * Este metodo es usado para inicializar la actividad. Se define la interfaz de usuario, se instancian clases auxiliares, se crea un
     * servicio de solicitud de localizacion, se inicializa el componente de interfaz de usuario que encapsula la camara y superficie de renderizacion.
     *
     * @param savedInstanceState estado guardado de la aplicacion un valor {@code null} indica que la actividad no debe ser recreada a partir de información previa.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRuta = new Rutas();
        setContentView(R.layout.activity_wikitude);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        createLocationRequest();
        this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
        final StartupConfiguration config = new StartupConfiguration("E2+3OVGKbQcv6JrSgriX+czMlLGUGf9lfex/+wxB6b1Tbo7RyIoccealOwt4Kl1mAf5QBel8wQ818kRAeJfbHB/K5XO12aC0cptyGTO8NEDypO87dn19fTA3Hx7ULASgd4zhwStTH42bsGIEMsL4SPUNd2ucuE4R15y+4kJzQFNTYWx0ZWRfX3rT9ifimN5EoNAvY/2QDMkmhwD57VrGVyq6Y8lgwAQTwocP+1IW4choWR9mq3J7yJI2wMozzbp8kB2v7thM71zjTV42qnP6WdY13rCm3Vj5EuqCRdYOTNgPlUoLwSgTVIBSDwD9Jh984S0Zlr9TDw8Yn5il6OhlLYTug9dYV2PMQzt/uQ9ukisOGt0B49iEjJn1flOmx5PCjxo8+Vcm8xtM/1Nen79Ifrf63itCHjDZaPT37qW0Tm52KZJiGo9CoeMkDfWSGuv1hrZ0sABIFciRROmaF+tf//5C+FQAt8eE6TJzcCsenx9qwyMNAgdD+RORiZqF9mnzJaIUOvBB4TrzxmU2bAVgwd5TgYylm+itjUigNNfnD8z2i/cF0w7Z77W+ZUfEgfSEPfOLCrXg16PnHXrLxKw7FK2LR6txrAPmcKrHqUAUikFBWrwTqMK3nFPti0ksA2kYpRiCOklybe74Q6jvq3OXyzb8tSEDXXeC1RJw3oiUOiA=");
        this.architectView.onCreate( config );
        boolean check = askCompatibility();
        if(check)
        {
            requestPermission();
        }

        //setToolbar();

    }


    /**
     * Permite mostrar la barra superior de la aplicacion.
     */
    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mapa UCR");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Permite identificar cual item se pulso de la barra superior de la aplicacion.
     *
     * @param  item  item seleccionado.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Define que al clicar el boton "back" de android en esta actividad se debe retornar a la actividad del mapa.
     *
     * @param  keyCode  codigo del boton pulsado.
     * @param  event  evento del boton pulsado.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * Carga la carpeta donde se tienen los archivos de imagenes para la Realidad Aumentada.
     *
     * @param  savedInstanceState  instancia del estado de la vista.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        architectView.onPostCreate();

        try {
            this.architectView.load("file:///android_asset/poi_1/index.html");/*Hay que poner ruta de directorio aqui*/
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Se cayó", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Metodo para reanudar la actividad.
     */
    @Override
    protected void onResume(){
        super.onResume();
        architectView.onResume();
    }

    /**
     * Metodo para destruir la actividad.
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        architectView.onDestroy();
    }

    /**
     * Metodo para pausar la actividad.
     */
    @Override
    protected void onPause(){
        super.onPause();
        architectView.onPause();
    }

    /**
     * Comprueba que los permisos fueron aprobados. Si este no es el caso, despliega un mensaje al usuario.
     *
     * @param  requestCode  codigo de solicitud.
     * @param permissions los permisos solicitados.
     * @param grantResults permiso si es concedido o denegado.
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
     * Verifica que la version de android sea M o mayor para preguntar por permisos.
     *
     * @return {@code true}/{@code false} que indica si se puede preguntar por permisos.
     */
    private boolean askCompatibility(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    /**
     * Metodo para preguntar permisos.
     */
    private void requestPermission(){
        //Preguntar por permiso
        ActivityCompat.requestPermissions(this, StringPermisos, 0);
    }


// Api de Google


    /**
     * Conecta la aplicacion a los servicios de google.
     */
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    /**
     * Detiene la conexion a los servicios de google.
     */
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }



    /**
     * Permite localizar la posicion del usuario cada segundo.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Actualiza posicion de usuario y actualiza vista de usuario.
     *
     * @param  bundle Conjunto de datos proveidos a los clientes por los Google Play services.
     *                Podria ser {@code null} si ningun contenido es brindado por el servicio.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = WikitudeActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED)
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            mCurrentLocation = mLastLocation;
        }

        startLocationUpdates();
    }

    /**
     * Permite iniciar el rastreo de la posicion del usuario.
     */
    protected void startLocationUpdates() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = WikitudeActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

    }

    /**
     * Actualiza la vista cada vez que se cambia la posición del usuario.
     *
     * @param  location  ubicacion del usuario.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        makeUseOfNewLocation(location);
    }

    /**
     * Permite re-conectarse a los servicios de google en caso de perder conexion.
     *
     * @param  i la razon de la desconexion.
     */
    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * Este metodo es llamado cuando hubo un error conectando el cliente a los servicios de Google.
     *
     * @param  connectionResult  resultado de la conexion
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /**
     * Este metodo obtiene el identificador de los 3 edificios mas cercanos con respecto al parametro de tipo {@code Location} recibido
     * y luego a partir de esto carga los puntos de interes para que sean visibles a traves de la camara.
     *
     * @param location localizacion actual
     */
    private void makeUseOfNewLocation(Location location) {

        Map<Integer, Location> res = mRuta.edificiosMasCercanos(location);

        Integer[] ids = new Integer[3];
        int cont = 0;

        for (Map.Entry<Integer, Location> entry : res.entrySet()) {
            ids[cont] = entry.getKey();
            cont++;
        }

        id1 = ids[0];
        id2 = ids[1];
        id3 = ids[2];

       // try {

            architectView.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
           // this.architectView.load("poi_1/index.html");
            architectView.callJavascript("World.loadPoisFromJsonData(" + id1 +"," + id2 + "," + id3 +")");

        /*} catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Se cayó", Toast.LENGTH_SHORT).show();

        }*/


      //  res.clear();

    }





















}