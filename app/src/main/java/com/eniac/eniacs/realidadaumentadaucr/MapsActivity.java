package com.eniac.eniacs.realidadaumentadaucr;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
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
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.wikitude.architect.CameraPreviewBase.m;
import static java.lang.Thread.sleep;


/**
 * Esta clase representa un mapa de Google. Contiene metodos para solicitar y manejar permisos de localizacion, para luego con ellos ayudar
 * al usuario de la aplicacion a moverse mas facilmente en el mundo real a partir de la informacion presentada en el mapa (i.e. puntos de interes).
 *
 * @author  EniacsTeam
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, SensorEventListener {



    SearchView searchView;
    SearchManager searchManager;
    DrawerLayout mDrawerLayout;
    private boolean isFirst = true;

    List<Polyline> rutas=new ArrayList<>();
    boolean flag=false;

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
    private JSONObject rutasDetalle[] = new JSONObject[3];
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
    private int indice_actual = -1;
    private int cantidad_rutas = 0;

    //para navegar prueba
    private int rutaElegida=0;
    private int pasoSgt=0;
    private boolean navegar= false;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


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

        init();            // call init method
       // setListview();    // call setListview method
        panelListener(); // Call paneListener method
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.setAnchorPoint(0.3f); //Para que solo se vea las 3 rutas y no se expanda completamente el panel
        mLayout.setTouchEnabled(false); //Para que el usuario no pueda deslizar el panel


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if(isFirst) {
                    isFirst = false;
                    configureSearch();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void configureSearch() {
        // Get the SearchView and set the searchable configuration
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search);
        // Current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast t = Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT);
                t.show();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


       // String [] parameter = {"9.937886, -84.052016","9.936089, -84.051115"};
       // GetDirection gd = new GetDirection();
       // String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + "9.937886, -84.052016" + ",&destination=" + "9.936089, -84.051115"+ "&sensor=false";
        //gd.execute(stringUrl);

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

                rutaElegida=0;
                pasoSgt=0;
                navegar= false;

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

        /*if(!flag){
            flag=true;
            LatLng start = new LatLng(9.937886, -84.052016);
            LatLng end = new LatLng(9.936089, -84.051115);
            getURL(start,end);
        }*/

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

        if(navegar) {

            Location pasoSgte = new Location("Paso Sgt");
            String[] resp;
            if(pasoSgt==0) {
                resp = instruccionesRuta(rutaElegida, pasoSgt);
                pasoSgte.setLatitude(Double.parseDouble(resp[2]));
                pasoSgte.setLongitude(Double.parseDouble(resp[3]));
                Toast.makeText(MapsActivity.this, resp[4], Toast.LENGTH_SHORT).show();

            }


            if (mCurrentLocation.distanceTo(pasoSgte) < 10 || pasoSgt==0 ) {

                resp = instruccionesRuta(rutaElegida, pasoSgt);
                ++pasoSgt; //hay que ponerlo en cero cuando se haga el borrar rutas
                pasoSgte.setLatitude(Double.parseDouble(resp[2]));
                pasoSgte.setLongitude(Double.parseDouble(resp[3]));

                Toast.makeText(MapsActivity.this, resp[4], Toast.LENGTH_SHORT).show();
               // LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                //actualizarRuta(current);


            }
        }

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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //instruccionesRuta(position);
                rutaElegida=position;
                navegar= true;
                Toast.makeText(MapsActivity.this, "posicion " + position, Toast.LENGTH_SHORT).show();
            }
        });
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
                indice_actual = cont;

                List <String[]> result = datosRuta(indice_actual);
                setListview(result);

               // datosRuta(cont);
                /*while (!flag)
                {
                    //flagRutas == false
                }*/

               /* try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
    public void setListview(List <String[]> result){

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        if(indice_actual != -1)
        {
            CustomListAdapter adapter=new CustomListAdapter(this, rutas_list(3), descripcion_list(),duracion_list());
            listview.setAdapter(adapter);
        }
       /* else
        {
            CustomListAdapter adapter=new CustomListAdapter(this, rutas_list(cantidad_rutas), result.get(0),result.get(1));
            listview.setAdapter(adapter);
        }*/

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
    public String[] rutas_list(int cantidad){
        String[] array_list = new String[cantidad];
        switch (cantidad){
           case 1:
               array_list[0] = "Ruta 1";
               return array_list;
           case 2:
              array_list[0] = "Ruta 1";
              array_list[1] = "Ruta 2";
               return array_list;
           case 3:
               array_list[0] = "Ruta 1";
               array_list[1] = "Ruta 2";
               array_list[2] = "Ruta 3";
               return array_list;

       }
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


    public void getURL(LatLng startL, LatLng endL){
        String start = startL.latitude+","+startL.longitude;
        String end = endL.latitude+","+endL.longitude;
        String stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="+start+"&destination="+end+"&alternatives=true&mode=walking&key=AIzaSyCljYcjcbR69841xYHr5kTcuPfmQ_2qWZE";
        ;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stringUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dibujar(response);
                        //instruccionesRuta(0,1);
                        //datosRuta(3);
                        flag = true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dibujar(error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void dibujar(String jsonRuta){
        try {
            JSONObject jsonObject = new JSONObject(jsonRuta);
            // routesArray contains ALL routes
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            int longitud = routesArray.length();
            cantidad_rutas = longitud;
            //cantidad_rutas = 2;
            // Grab the first route
            for (int i = 0; i < longitud;i++){
                JSONObject route = routesArray.getJSONObject(i);
                rutasDetalle[i] = route;
                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                List<LatLng> polyz= decodePoly(polyline);//decodificación de la polilinea
                String color = "0";
                if(i==0){
                    color = "#008000"; //Verde
                } else if(i==1){
                    color = "#800080"; //Morado
                } else {
                    color = "#0000FF"; //Azul
                }

                for (int j = 0; j < polyz.size() - 1; j++) {
                    LatLng src = polyz.get(j);
                    LatLng dest = polyz.get(j + 1);

                    rutas.add(i,mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(4).color(Color.parseColor(color)).geodesic(true)));

                }
            }

        } catch (Exception e) {

        }
    }

    /* Method to decode polyline points */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    /**
     * Metodo llamado para obtener la informacion sobre las rutas disponibles
     *
     * @param puntoElegido  El indice de la ruta seleccionada por el usuario
     */
    private List < String[] > datosRuta(int puntoElegido) {
        LatLng inicioRuta = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        LatLng destinoRuta = new LatLng(mRuta.elatitud[puntoElegido], mRuta.elonguitud[puntoElegido]);
        getURL(inicioRuta, destinoRuta);

        /*while (!flag)
            {
                    //flagRutas == false
            }*/
       /* try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
        flag = false;
        cantidad_rutas = 2;
        String[] distancias = new String[cantidad_rutas];
        String[] duraciones = new String[cantidad_rutas];

        List < String[] > distanciaDuracion = new ArrayList < String[] > ();
        try {
            for (int i = 0; i < cantidad_rutas; i++) { //cantidadRutas
                JSONObject distanciaJson = rutasDetalle[i].getJSONArray("legs").getJSONObject(0).getJSONObject("distance");
                String distancia = distanciaJson.getString("text");
                distancias[i] = distanciaJson.getString("text");
                JSONObject duracionJson = rutasDetalle[i].getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
                String duracion = duracionJson.getString("text");
                duraciones[i] = duracionJson.getString("text");
                Log.i(TAG, "Distancia: " + distancia + " Duracion: " + duracion);
            }
            distanciaDuracion.add(distancias);
            distanciaDuracion.add(duraciones);
        } catch (Exception e) {
            rutaElegida=0;
            navegar= true;
            pasoSgt= 0;
        }
        return distanciaDuracion;
    }

    /**
     * Metodo llamado para obtener la informacion sobre una ruta seleccionada.
     *
     * @param rutaElegida  El indice de la ruta seleccionada por el usuario
     */
    private String[] instruccionesRuta(int rutaElegida, int pasoSolicitado) {
        if (pasoSolicitado == 0) {
            borrarRutas(rutaElegida);
        }
        String[] detallesPaso = new String[5];
        try {


            JSONArray pasos = rutasDetalle[rutaElegida].getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            JSONObject paso = pasos.getJSONObject(pasoSolicitado);
            String distanciaPaso = paso.getJSONObject("distance").getString("text");
            detallesPaso[0] = distanciaPaso;
            String duracionPaso = paso.getJSONObject("duration").getString("text");
            detallesPaso[1] = duracionPaso;
            String latEndPaso = paso.getJSONObject("end_location").getString("lat");
            detallesPaso[2] = latEndPaso;
            String lonEndPaso = paso.getJSONObject("end_location").getString("lng");
            detallesPaso[3] = lonEndPaso;
            String mensajePaso = paso.getString("html_instructions");
            detallesPaso[4] = mensajePaso;
            //Log.i(TAG, "Distancia Paso: " + distanciaPaso + " Duracion Paso: " + duracionPaso + " Latitud final: " + latEndPaso + " Longitud final: " + lonEndPaso + " Mensaje: " + mensajePaso);



        } catch (Exception e) {

        }
        return detallesPaso;
    }

    /**
     * Metodo llamado para borrar rutas no requeridas.
     *
     * @param index  El indice de la ruta seleccionada por el usuario
     */
    private void borrarRutas(int index) {
        Polyline tempPoli = rutas.get(0);
        for (int i = 0; i < rutas.size(); i++) {
            if (i == index) {
                tempPoli = rutas.get(i);
            } else {
                rutas.get(i).remove();
            }
        }
        rutas.clear();
        rutas.add(tempPoli);
    }

    public void actualizarRuta(LatLng current) {
        rutas.get(0).getPoints().set(0,current);
        LatLng inicio = rutas.get(0).getPoints().get(0);//falta crear control de null
        Location primeroL= new Location("currentL");
        primeroL.setLatitude(current.latitude);
        primeroL.setLongitude(current.longitude);

        LatLng segundo = rutas.get(0).getPoints().get(1);//falta crear control de null
        Location segundoL= new Location("currentL2");
        segundoL.setLatitude(inicio.latitude);
        segundoL.setLongitude(inicio.longitude);

        if(primeroL.distanceTo(segundoL)<3) {//cambiar 3 por la distancia que deseamos utilizar de cercanía
            rutas.get(0).getPoints().remove(0);//falta crear control de null
        }
    }

}
