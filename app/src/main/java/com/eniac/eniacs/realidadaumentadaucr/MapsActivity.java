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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static android.os.Build.VERSION_CODES.M;
import static com.eniac.eniacs.realidadaumentadaucr.R.id.fab;
import static com.eniac.eniacs.realidadaumentadaucr.R.id.map;
import static com.google.android.gms.internal.zznu.it;
import static com.google.android.gms.internal.zzsr.MA;
import static com.wikitude.architect.CameraPreviewBase.m;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Arrays;
import java.util.List;

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

    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "MapsActivity";
    List<String> array_list;
    TextView textView ;
    ListView listview;
    ImageButton imageButton;
    private  Marker marcador_actual;


    /**
     * Este metodo es usado para inicializar la actividad. Se define la interfaz de usuario, se instancian clases auxiliares, se crea un
     * servicio de solicitud de localizacion, se inicializan referencias a los iconos de los marcadores para los edificios, se obtiene el
     * mapa listo para usarlo, se recuperan las herramientas de interfaz de usuario con las que se quieren interactuar y se inicializan
     * las variables necesarias para manejar el sensor de rotacion del dispositivo.
     *
     * @param savedInstanceState estado guardado de la aplicacion un valor {@code null} indica que la actividad no debe ser recreada a partir de información previa.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

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

        init();            // call init method
        setListview();    // call setListview method
        panelListener(); // Call paneListener method
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.setAnchorPoint(0.3f); //Para que solo se vea las 3 rutas y no se expanda completamente el panel
        mLayout.setTouchEnabled(false); //Para que el usuario no pueda deslizar el panel

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
            marcasTodas[i] = mMap.addMarker(new MarkerOptions().position(pos).alpha(0.3f)
                    .title(mRuta.edificios[i]).icon(BitmapDescriptorFactory.fromResource(iconVec[i])));

        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                quitafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);
                fab.startAnimation(quitafab);
                fab.setVisibility(View.GONE);
                textView.setText(marker.getTitle());
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                marker.showInfoWindow();
                marcador_actual = marker;
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                if(fab.getVisibility() != View.VISIBLE){
                    cargafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show);
                    fab.startAnimation(cargafab);
                    fab.setVisibility(View.VISIBLE);
                }

            }
        });
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
            marcasTodas[indice].setAlpha(3);
        }
        correrApuntado = true;
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
                marcasTodas[indice].setAlpha(0.3f);
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

    /**
     * Initialization of the textview and SlidingUpPanelLayout
     */
    public void init(){

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        listview = (ListView) findViewById(R.id.soy_lista);
        textView = (TextView) findViewById(R.id.name);
        imageButton = (ImageButton) findViewById(R.id.direction_go);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = marcador_actual.getPosition().latitude;
                Boolean condicion = false;
                int cont = -1;
                while (condicion==false)
                {
                    ++cont;
                    if(mRuta.elatitud[cont] == lat)
                    {
                        condicion = true;
                    }
                }
                //datoRuta(cont);
                /*while (!flagRutas)
                {
                    //flagRutas == false
                }*/
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                Toast.makeText(MapsActivity.this, "Boton funciona y marcador es indice " + cont, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *  in this method, we set array adapter to display list of item
     *  within this method, call callback setOnItemClickListener method
     *  It call when use click on the list of item
     *  When user click on the list of item, slide up layout and display item of the list
     */
    public void setListview(){

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //instruccionesRuta(position);
                Toast.makeText(MapsActivity.this, "posicion " + position, Toast.LENGTH_SHORT).show();
            }
        });


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        CustomListAdapter adapter=new CustomListAdapter(this, rutas_list(), descripcion_list(),duracion_list());
        listview.setAdapter(adapter);
        ColorDrawable grey = new ColorDrawable(this.getResources().getColor(R.color.grey));
        listview.setDivider(grey);

        listview.setDividerHeight(1);

    }

    /**
     * this method call setPanelSlidelistener method to listen open and close of slide panel
     */
    public void panelListener(){
        SlidingUpPanelLayout.PanelSlideListener listener = new SlidingUpPanelLayout.PanelSlideListener() {

            // During the transition of expand and collapse onPanelSlide function will be called.
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }

        };
        mLayout.addPanelSlideListener(listener);
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                           }
    });
    }

    /**
     * This is return type method.
     * With in this method, we create array list
     * @return array list
     */
    public String[] rutas_list(){
        String[] array_list = {
                "Ruta 1",
                "Ruta 2",
                "Ruta 3"
        };
        return array_list;
    }

    public String[]descripcion_list(){
        String[] array_list = {
                "Ejemplo 1",
                "Ejemplo 2",
                "Ejemplo 3"
        };
        return array_list;
    }

    public String[] duracion_list(){
        String[] array_list = {
                "5 min",
                "7 min",
                "10 min"
        };
        return array_list;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLayout != null &&
                    (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                return true;
            }

            else if (mLayout != null &&
                    mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                marcador_actual.hideInfoWindow();
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }
}
