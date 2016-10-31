package com.eniac.eniacs.realidadaumentadaucr;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static android.os.Build.VERSION_CODES.M;
import static com.eniac.eniacs.realidadaumentadaucr.R.id.fab;
import static com.eniac.eniacs.realidadaumentadaucr.R.id.map;
import static com.google.android.gms.internal.zznu.it;

/**
 * Esta clase representa un mapa de Google. Contiene metodos para solicitar y manejar permisos de localizacion, para luego con ellos ayudar
 * al usuario de la aplicacion a moverse mas facilmente en el mundo real a partir de la informacion presentada en el mapa (i.e. puntos de interes).
 *
 * @author  EniacsTeam
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, SensorEventListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private static final String TAG = "MapsActivity";
    private Rutas mRuta;
    private final String[] StringPermisos = {android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int iconVec[] = new int[28];
    private final String wordVec[] = {"derecho", "oficbecas", "biblio", "arqui", "comedor", "inge", "fisicamate", "generales", "biblio", "preescolar",
            "letras", "centinform", "geologia", "economicas", "ecci", "odonto", "medicina", "farmacia", "microbiologia", "biolo", "quimica", "musica",
            "artes", "educa", "bosque", "mariposario", "plaza", "pretil"};//28
    private Paint paint;

    private Marker marcasTodas[] = new Marker[28];
    private Map<Integer, Location> res;
    private int apuntAnterior = -1;
    private boolean correrApuntado = false;
    FloatingActionButton fab;
    Animation cargafab;
    Animation quitafab;


    /*para los sensores*/
    private float[] rotationMatrix;
    private float[] orientationVals;
    private SensorManager mSensorManager;//control de sensores
    private Sensor distanceVector;

    /**
     * Este metodo es usado para inicializar la actividad. Se define la interfaz de usuario, se instancian clases auxiliares, se crea un
     * servicio de solicitud de localizacion, se inicializan referencias a los iconos de los marcadores para los edificios, se obtiene el
     * mapa listo para usarlo, se recuperan las herramientas de interfaz de usuario con las que se quieren interactuar y se inicializan
     * las variables necesarias para manejar el sensor de rotacion del dispositivo.
     *
     * @param savedInstanceState estado guardado de la aplicacion un valor {@code null} indica que la actividad no debe ser recreada a partir de información previa.
     */
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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        cargafab = AnimationUtils.loadAnimation(this, R.anim.fab_show);
        fab.startAnimation(cargafab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(MapsActivity.this, WikitudeActivity.class));

                quitafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);
                fab.startAnimation(quitafab);
                startActivity(new Intent(MapsActivity.this ,WikitudeActivity.class));

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        rotationMatrix = new float[9];
        orientationVals = new float[3];
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);//obtenemos el servicio
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        distanceVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        paint  = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP));
    }

    /**
     * Si la instancia de {@code GoogleApiClient} tiene un valor distinto de {@code null} es necesario reconectar la aplicación
     * a los servicios de Google.
     */
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    /**
     * Cuando la actividad ya no es visible al usuario detiene la conexión a los servicios de Google.
     */
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * Construye una instancia de {@code GoogleApiClient} el cual provee un punto de entrada a todos los servicios de Google Play.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    /**
     * Permite definir parametro de calidad de servicio para las solicitudes de localizacion.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Manipula el mapa una vez que esta disponible. Se habilitan herramientas como myLocation y el compass.
     * <p>
     * Esta retrollamada es desencadenada cuando el mapa esta listo para ser usado.
     * <p>
     * Si los Google Play services no estan instalados en el dispositivo, se le solicitara al usuario instalarlo dentro del
     * SupportMapFragment. Este metodo solo se desencadenara una vez que el usuario ha instalado los Google Play services
     * y ha retornado a la aplicacion.
     *
     * @param googleMap una instancia no nula de un GoogleMap
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
        mMap.setMinZoomPreference(13);

        for (int i = 0; i < mRuta.edificios.length; ++i) {
            LatLng pos = new LatLng(mRuta.elatitud[i], mRuta.elonguitud[i]);
            marcasTodas[i] = mMap.addMarker(new MarkerOptions().position(pos).visible(false)
                    .title(mRuta.edificios[i]).icon(BitmapDescriptorFactory.fromResource(iconVec[i])));

        }
    }


    /**
     * Metodo encargado de pedir que se le conceda a la aplicacion ciertos permisos.
     */
    private void requestPermission() {
        //Preguntar por permiso
        ActivityCompat.requestPermissions(this, StringPermisos, 0);
    }


    /**
     * Verifica que tenga los permisos apropiados para acceder a la ubicación de usuario.
     *
     * @param  requestCode  codigo del permiso
     * @param  permissions  los permisos que se solicitan
     * @param  grantResults  indica si permiso es concedido o no
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your location", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }


    /**
     * Llena el vector con los identificadores de cada icono que se utiliza para representar a cada marcador.
     */
    private void llenarIconVec() {
        for (int i = 0; i < 28; ++i) {
            iconVec[i] = getResources().getIdentifier(wordVec[i], "drawable", getPackageName());
        }
    }


    /**
     * Este metodo se encarga de agregar los marcadores respectivos a los 3 edificios mas cercanos a la posicion del usuario.
     */
    private void addMarkers() {
        res = mRuta.edificiosMasCercanos(mCurrentLocation);

        Iterator<Integer> it = res.keySet().iterator();

        int indice;
        while (it.hasNext())
        {   indice = it.next();
            marcasTodas[indice].setVisible(true);
        }
        correrApuntado = true;
    }

    /**
<<<<<<< HEAD
     * Actualiza posicion de usuario y actualiza vista de usuario.
=======
     * Actualiza posición de usuario y actualiza
     * vista de usuario
     * <p>
>>>>>>> a4bcef7dca3a315ed285c069c5d67a42566cc828
     *
     * @param  bundle Conjunto de datos proveidos a los clientes por los Google Play services.
     *                Podria ser {@code null} si ningun contenido es brindado por el servicio.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = MapsActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            mCurrentLocation = mLastLocation;
            if (mLastLocation != null) {
                LatLng posicion = new LatLng(9.937604, -84.050606);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15.7f));
            } else {
                Toast.makeText(this, "Se cayó en el onConnected", Toast.LENGTH_LONG).show();
            }
        }

        startLocationUpdates();
    }


    /**
     * Si se han concedido los permisos de acceso a la localizacion permite iniciar el rastreo de la posición del usuario.
     */
    protected void startLocationUpdates() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = MapsActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

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
     * Actualiza la vista cada vez que se cambia la posicion del usuario. Constantemente actualiza los marcadores que deben ser presentados.
     *
     * @param  location  ubicacion del usuario
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (res != null) {
            Iterator<Integer> it = res.keySet().iterator();
            int indice;
            while (it.hasNext())
            {   indice = it.next();
                marcasTodas[indice].setVisible(false);
            }
        }
        addMarkers();
    }

    /**
     * Este metodo para las actualizaciones de localizacion pues la actividad sera pausada, ya que esta se ira al "background".
     */
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        mSensorManager.unregisterListener(this);
    }


    /**
     * Metodo para detener el rastreo de posicion y de esta manera no incurrir en un gasto de procesamiento y bateria innecesario.
     */
    protected void stopLocationUpdates() {

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }


    /**
     * Metodo para resumir la actividad.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        mSensorManager.registerListener(this, distanceVector, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Este metodo es llamado cuando hay un nuevo evento del sensor.
     * <p>
     * Se encarga de constantemente escuchar cambios en la rotacion del dispositivo y revisar si en la direccion apuntada existe
     * un marcador en las cercanias para resaltarlo dentro del mapa.
     *
     * @param event el evento ocurrido
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (correrApuntado) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrix);
            SensorManager.getOrientation(rotationMatrix, orientationVals);
            orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
            float azimuth = (orientationVals[0] + 360) % 360;

            int indice = mRuta.edificioApuntado(azimuth);

            if (apuntAnterior != -1 && indice != apuntAnterior) {
                marcasTodas[apuntAnterior].setIcon(BitmapDescriptorFactory.fromResource(iconVec[apuntAnterior]));

            }
            if (indice != -1) {
                Bitmap ob = BitmapFactory.decodeResource(this.getResources(), iconVec[indice]);
                Bitmap obm = Bitmap.createBitmap(ob.getWidth(), ob.getHeight(), ob.getConfig());
                Canvas canvas = new Canvas(obm);
                canvas.drawBitmap(ob, 0f, 0f, paint);
                marcasTodas[indice].setIcon(BitmapDescriptorFactory.fromBitmap(obm));
                apuntAnterior = indice;
            }
        }
    }

    /**
     * Metodo llamado cuando la precision del sensor registrado ha cambiado.
     *
     * @param sensor el sensor registrado
     * @param i la nueva precision del sensor
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

